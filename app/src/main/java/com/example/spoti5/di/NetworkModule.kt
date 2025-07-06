package com.example.spoti5.di

import com.example.spoti5.constants.Constants
import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.remote.interceptor.AuthorizationInterceptor
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //chung
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

//    @Provides
//    @Singleton
//    fun provideMoshiConverterFactory() : MoshiConverterFactory {
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        return MoshiConverterFactory.create(moshi)
//    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonConverterFactory.create()
        return gson
    }


    //rieng
    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor
                , authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authorizationInterceptor)
        return builder.build()
    }

//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit {
//        return Retrofit.Builder().addConverterFactory(moshiConverterFactory).baseUrl(Constants.BASE_URL).client(okHttpClient).build()
//    }

    @Provides
    @Singleton
    fun provideRetrofit2(okHttpClient: OkHttpClient, gson: GsonConverterFactory): Retrofit {
        return Retrofit.Builder().
        addConverterFactory(gson)
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideSpotifyApi(retrofit: Retrofit): SpotifyApi {
        return retrofit.create(SpotifyApi::class.java)
    }


}