package com.example.twittercountertask.data.model

import com.google.gson.annotations.SerializedName

data class TweetResponseDto(
    @SerializedName("data") val data: TweetDataDto,
)

data class TweetDataDto(
    @SerializedName("edit_history_tweet_ids") val editHistoryTweetIds: List<String>,
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
)