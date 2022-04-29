package com.bink.localhero.di

import com.bink.localhero.BuildConfig
import com.bink.localhero.data.remote.ApiService
import com.bink.localhero.utils.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single(named(LOCAL_HERO_OKHTTP)) { provideDefaultOkHttpClient() }
    single(named(LOCAL_HERO_RETROFIT)) { provideRetrofit(get(named(LOCAL_HERO_OKHTTP))) }
    single { provideApiService(get(named(LOCAL_HERO_RETROFIT))) }
}

fun provideDefaultOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val headerAuthorizationInterceptor = Interceptor { chain ->
        val request = chain.request().url().newBuilder().build()

        val jwtToken =
            LocalStoreUtils.getAppSharedPref(
                LocalStoreUtils.KEY_TOKEN
            )?.replace("\n", "")?.trim()

        val newRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", " Bearer $jwtToken")

        if (BuildConfig.BUILD_TYPE != RELEASE_BUILD_TYPE) {
            newRequest.header("Bink-Test-Auth", Keys.binkTestAuthToken()).url(request)
        } else {
            newRequest.url(request)
        }
        val response = chain.proceed(newRequest.build())
        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(headerAuthorizationInterceptor)
        .addInterceptor(interceptor).build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)

    return retrofitBuilder.build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)