package com.app.mvvm.di

import android.util.Log
import com.app.mvvm.remote.ApiService
import com.app.mvvm.repository.Repository
import com.app.mvvm.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(provideTokenInterceptor())
            .addInterceptor(provideHttpLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }
    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
         GsonConverterFactory.create()
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
    @Singleton
    @Provides
    fun provideCurrencyService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    class ServiceInterceptor : Interceptor {
//        private var token : String = SplashActivity.jwtToken

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if(request.header("No-Authentication")==null){
//                if(!token.isNullOrEmpty())
//                {
//                    val finalToken = token
//                    Log.e("TAG", "intercept: >>>>>>>>>>>>>>>>>> $token")

                    request = request.newBuilder()
                        .addHeader("Authorization","finalToken")
                        .build()
//                }
            }
            return chain.proceed(request)
        }
    }
    @Singleton
    @Provides
    fun provideTokenInterceptor(): ServiceInterceptor {
        return ServiceInterceptor()
    }


}