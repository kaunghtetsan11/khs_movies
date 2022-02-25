package com.khs.movies.di

import com.khs.movies.repository.CacheRepository
import com.khs.movies.repository.CacheRepositoryImpl
import com.khs.movies.repository.MainRepository
import com.khs.movies.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ActivityModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun bindCacheRepository(cacheRepositoryImpl: CacheRepositoryImpl): CacheRepository
}