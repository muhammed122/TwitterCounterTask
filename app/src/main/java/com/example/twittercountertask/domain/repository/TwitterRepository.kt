package com.example.twittercountertask.domain.repository

import com.example.network.utils.Resource
import com.example.twittercountertask.domain.entity.TweetModel

interface TwitterRepository {
    suspend fun postTweet(text: String): Resource<TweetModel>
}