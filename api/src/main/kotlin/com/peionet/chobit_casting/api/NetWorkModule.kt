package com.peionet.chobit_casting.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetWorkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }).addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://apiv2.twitcasting.tv")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            ).addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
    }

    @Singleton
    @Provides
    fun provideChobitCastingApi(retrofit: Retrofit): ChobitCastingApi {
        return ChobitCastingApi(retrofit)
    }


    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            proceed(
                request()
                    .newBuilder()
                    .header(
                        "Authorization", "Basic " + convertBase64(
                            BuildConfig.CLIENT_ID,
                            BuildConfig.CLIENT_SECRET
                        )
                    ).build()
            )
        }

        private fun convertBase64(clientId: String, clientSecret: String): String {
            val target = "$clientId:$clientSecret"
            return Base64.getEncoder().encodeToString(target.toByteArray())
        }
    }
}
