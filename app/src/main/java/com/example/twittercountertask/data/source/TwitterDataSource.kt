package com.example.twittercountertask.data.source

import com.example.network.utils.Resource
import com.example.twittercountertask.domain.entity.TweetModel

interface TwitterDataSource {
    suspend fun postTweet(text : String ) : Resource<TweetModel>

}