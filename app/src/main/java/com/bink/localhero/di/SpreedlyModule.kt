package com.bink.localhero.di

import com.bink.localhero.data.remote.SpreedlyService
import com.bink.localhero.utils.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val spreedlyModule = module {
    single(named("spreedlyOkHttp")) { provideSpreedlyOkHttpClient() }
    single(named("spreedlyRetrofit")) { provideRetrofit(get(named("spreedlyOkHttp"))) }
    single { provideSpreedlyService(get(named("spreedlyRetrofit"))) }
}

fun provideSpreedlyOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val headerAuthorizationInterceptor = Interceptor { chain ->
        val request = chain.request().url().newBuilder().build()

        val newRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .url(request)
            .build()
        val response = chain.proceed(newRequest)
        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(headerAuthorizationInterceptor)
        .addInterceptor(interceptor).build()
}

fun provideSpreedlyService(retrofit: Retrofit): SpreedlyService =
    retrofit.create(SpreedlyService::class.java)