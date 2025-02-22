package com.example.twittercountertask.data.service

import com.example.twittercountertask.data.model.TweetResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TwitterService {

    @POST("tweets")
    @FormUrlEncoded
    suspend fun postTweet(
        @Field("text") tweet: String,
    ): Response<TweetResponseDto>
}