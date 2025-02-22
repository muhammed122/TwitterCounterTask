package com.example.twittercountertask.data.mapper

import com.example.twittercountertask.data.model.TweetResponseDto
import com.example.twittercountertask.domain.entity.TweetModel


fun TweetResponseDto.mapToTweetModel() = TweetModel(
    id = this.data.id,
    text = this.data.text,
    editHistoryTweetIds = this.data.editHistoryTweetIds
)