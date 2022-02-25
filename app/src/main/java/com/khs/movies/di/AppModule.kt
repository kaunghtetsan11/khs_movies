package com.khs.movies.di

import android.app.Application
import androidx.room.Room
import com.khs.movies.api.ApiService
import com.khs.movies.api.AuthInterceptor
import com.khs.movies.model.common.BASE_URL
import com.khs.movies.model.common.DATABASE_NAME
import com.khs.movies.model.common.NETWORK_TIMEOUT
import com.khs.movies.persistance.AppDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase = Room
        .databaseBuilder(
            app,
            AppDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    @Singleton
    @Provides
    fun provideApiService(): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .connectTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build()
            )
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build().create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

}