package com.example.twittercountertask.di

import com.example.twittercountertask.data.repository.TwitterRepositoryImpl
import com.example.twittercountertask.data.service.TwitterService
import com.example.twittercountertask.data.source.TwitterDataSource
import com.example.twittercountertask.data.source.TwitterDataSourceImpl
import com.example.twittercountertask.domain.repository.TwitterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TwitterAppModule {
    @Singleton
    @Provides
    fun provideTwitterService(retrofit: Retrofit): TwitterService {
        return retrofit.create(TwitterService::class.java)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindRepo {

    @Binds
    @ViewModelScoped
    abstract fun getRemoteDataSource(twitterDataSourceImpl: TwitterDataSourceImpl):
            TwitterDataSource

    @Binds
    @ViewModelScoped
    abstract fun getRepository(
        repositoryImpl: TwitterRepositoryImpl,
    ): TwitterRepository

}
