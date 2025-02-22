package com.example.twittercountertask.domain.usecase

import com.example.twittercountertask.domain.repository.TwitterRepository
import javax.inject.Inject

class PostTweetUseCase @Inject constructor(private val repository: TwitterRepository) {

    suspend operator fun invoke(text: String) =
        repository.postTweet(text)

}