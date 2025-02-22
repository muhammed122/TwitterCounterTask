package com.example.twittercountertask.data.source

import com.example.network.utils.Resource
import com.example.twittercountertask.data.mapper.mapToTweetModel
import com.example.twittercountertask.data.model.TweetResponseDto
import com.example.twittercountertask.data.service.TwitterService
import com.example.twittercountertask.domain.entity.TweetModel
import org.json.JSONObject
import javax.inject.Inject

class TwitterDataSourceImpl @Inject constructor(
    private val twitterService: TwitterService,
) : TwitterDataSource {
    override suspend fun postTweet(text: String): Resource<TweetModel> {
        return try {
            val data = twitterService.postTweet(text)
            if (data.body() != null && data.isSuccessful) {
                Resource.success(data.body()!!.mapToTweetModel())
            } else {
                val jsonObject = JSONObject(data.errorBody()?.string())
                Resource.error(Exception(jsonObject.getString("detail")))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

}