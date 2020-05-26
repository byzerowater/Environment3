/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zerowater.environment.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zerowater.environment.BuildConfig
import com.zerowater.environment.data.source.*
import com.zerowater.environment.data.source.local.SharedPreferencesCache
import com.zerowater.environment.data.source.local.SharedPreferencesDataSource
import com.zerowater.environment.data.source.remote.NetworkDataSource
import com.zerowater.environment.data.source.remote.NetworkService
import com.zerowater.environment.data.source.resource.ContextDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Environment
 * Class: ApplicationModule
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Dagger module for the Application.
 */
@Module(includes = [ApplicationModuleBinds::class])
class ApplicationModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
            networkService: NetworkService,
            ioDispatcher: CoroutineDispatcher
    ): RemoteDataSource {
        return NetworkDataSource(
                networkService, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
            sharedPreferences: SharedPreferencesCache,
            ioDispatcher: CoroutineDispatcher
    ): LocalDataSource {
        return SharedPreferencesDataSource(
                sharedPreferences, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideResourceDataSource(
            context: Context
    ): ResourceDataSource {
        return ContextDataSource(
                context
        )
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferencesCache = SharedPreferencesCache(context, BuildConfig.APPLICATION_ID + ".SharedPreferencesCache")

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    @Named("baseUrl")
    fun provideBaseUrl() = BuildConfig.BASE_URL

    /**
     * Retrofit 제공자
     *
     * @param gson         Gson
     * @param okHttpClient OkHttpClient
     * @param baseUrl      서버 URL
     * @return Retrofit
     */
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient, @Named("baseUrl") baseUrl: String): NetworkService {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(gson))
            baseUrl(baseUrl)
            client(okHttpClient)
        }.build().create(NetworkService::class.java)
    }

    /**
     * OkHttpClient 제공자
     *
     * @param httpLoggingInterceptor  로그 Interceptor
     * @param headerInterceptor       헤더 Interceptor
     * @param networkCheckInterceptor 통신 연결 확인 Interceptor
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(@Named("httpLoggingInterceptor") httpLoggingInterceptor: Interceptor,
                            @Named("headerInterceptor") headerInterceptor: Interceptor,
                            @Named("networkCheckInterceptor") networkCheckInterceptor: Interceptor): OkHttpClient {

        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(headerInterceptor)
            addInterceptor(networkCheckInterceptor)
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                            }

                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                return arrayOf()
                            }
                        }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                if (sslSocketFactory != null && trustAllCerts != null && trustAllCerts.size > 0) {
                    sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.build()
    }


    /**
     * 헤더 Interceptor 제공자
     *
     * @param context           Constext
     * @param preferencesHelper PreferencesHelper 접근 및 관리
     * @return 헤더 Interceptor
     */
    @Singleton
    @Provides
    @Named("headerInterceptor")
    fun provideHeaderInterceptor(context: Context, localDataSource: LocalDataSource): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val headers = original.headers
            if (headers.size == 0) {
                val request = original.newBuilder().apply {
                    header("Content-Type", "application/json; charset=utf-8")
                    method(original.method, original.body)
                    val authorization = localDataSource.getAuthToken()
                    authorization?.let {
                        header("Authorization", authorization)
                        Timber.d("authorization $authorization")
                    }
                }.build()
                chain.proceed(request)
            } else chain.proceed(original)
        }
    }

    /**
     * 통신 연결 확인 Interceptor 제공자
     *
     * @param context Context
     * @return 통신 연결 확인 Interceptor
     */
    @Singleton
    @Provides
    @Named("networkCheckInterceptor")
    fun provideNetworkCheckInterceptor(context: Context): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var isConnected = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.activeNetwork?.let {
                    connectivityManager.getNetworkCapabilities(it)?.let {
                        isConnected = when {
                            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                isConnected = connectivityManager.activeNetworkInfo?.isConnected ?: false
            }

            val original = chain.request()
            if (isConnected) chain.proceed(original) else throw IOException("not contected")
        }
    }

    /**
     * 로그 Interceptor 제공자
     *
     * @return 로그 Interceptor
     */
    @Singleton
    @Provides
    @Named("httpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    /**
     * Gson 제공자
     *
     * @return Gson
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setLenient()
                .create()
    }

}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultRepository): Repository
}
