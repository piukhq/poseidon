package com.bink.localhero.di

import com.bink.localhero.data.remote.SpreedlyService
import com.bink.localhero.utils.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val spreedlyModule = module {
    single { provideSpreedlyOkHttpClient() }
    single { provideSpreedlyRetrofit(get()) }
    single { provideSpreedlyService(get()) }
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

fun provideSpreedlyRetrofit(client: OkHttpClient): Retrofit {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)

    return retrofitBuilder.build()
}

fun provideSpreedlyService(retrofit: Retrofit): SpreedlyService =
    retrofit.create(SpreedlyService::class.java)