package com.example.twittercountertask.domain.entity

data class TweetModel(
    val editHistoryTweetIds: List<String>,
    val id: String,
    val text: String,
)

