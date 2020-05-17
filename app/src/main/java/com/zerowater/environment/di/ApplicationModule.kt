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
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zerowater.environment.BuildConfig
import com.zerowater.environment.data.source.DefaultRepository
import com.zerowater.environment.data.source.NetworkDataSource
import com.zerowater.environment.data.source.PreferencesDataSource
import com.zerowater.environment.data.source.Repository
import com.zerowater.environment.data.source.local.LocalPreferencesDataSource
import com.zerowater.environment.data.source.remote.NetworkService
import com.zerowater.environment.data.source.remote.RemoteDataSource
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
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
class ApplicationModule {


    @Singleton
    @Provides
    fun provideRemoteDataSource(
            networkService: NetworkService,
            ioDispatcher: CoroutineDispatcher
    ): NetworkDataSource {
        return RemoteDataSource(
                networkService, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideTasksLocalDataSource(
            sharedPreferences: SharedPreferences,
            ioDispatcher: CoroutineDispatcher
    ): PreferencesDataSource {
        return LocalPreferencesDataSource(
                sharedPreferences, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("test", Context.MODE_PRIVATE)
    }

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
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
                .create(NetworkService::class.java)
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
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(networkCheckInterceptor)
                .build()
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
    fun provideHeaderInterceptor(context: Context, localPreferencesDataSource: PreferencesDataSource): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val headers = original.headers
            if (headers.size == 0) {
                val builder = original.newBuilder()
                builder.header("Content-Type", "application/json")
                        .header("User-Agent", "Android Tablet")
                        .header("DEVICE-MODEL", Build.DEVICE)
                        .header("OS", "A")
                        .header("OS-VER", Build.VERSION.RELEASE)
                        .header("MARKET", Build.BRAND)
                        .method(original.method, original.body)
                val body = original.body
                Timber.i("body check %s", body)
//                var appId: String = preferencesHelper.getAppId()
//                val clientId: String = preferencesHelper.getClientId()
//                val authorization: String = preferencesHelper.getAuthToken()
//                if (appId.isNullOrBlank()) {
//                    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
//                    appId = UUID.nameUUIDFromBytes(androidId.toByteArray()).toString()
//                    preferencesHelper.putAppId(appId)
//                }
//                if (appId.isNullOrEmpty()) {
//                    builder.header("APP-ID", appId)
//                }
//                if (!clientId.isNullOrBlank()) {
//                    builder.header("CLIENT-ID", clientId)
//                }
//                if (!authorization.isNullOrBlank()) {
//                    builder.header("Authorization", authorization)
//                }
//                Timber.d("appId %s, clientId %s, authorization %s", appId, clientId, authorization)
                val request = builder.build()
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
            val original = chain.request()
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isConnected = connectivityManager.activeNetworkInfo?.isConnected ?: false
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
