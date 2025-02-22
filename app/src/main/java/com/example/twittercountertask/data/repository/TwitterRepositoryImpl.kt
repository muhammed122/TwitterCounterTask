package com.example.twittercountertask.data.repository

import com.example.network.utils.Resource
import com.example.twittercountertask.data.source.TwitterDataSource
import com.example.twittercountertask.domain.entity.TweetModel
import com.example.twittercountertask.domain.repository.TwitterRepository
import javax.inject.Inject

class TwitterRepositoryImpl @Inject constructor(private val dataSource: TwitterDataSource) : TwitterRepository {
    override suspend fun postTweet(text: String): Resource<TweetModel> {
        return dataSource.postTweet(text)
    }
}