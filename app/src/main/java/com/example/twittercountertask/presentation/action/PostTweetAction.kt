package com.example.twittercountertask.presentation.action

import com.example.ui.base.ViewAction

sealed class PostTweetAction : ViewAction {
    data class CheckRemaining(val tweet: String) : PostTweetAction()
    data object SendTweet : PostTweetAction()
    data object Clear : PostTweetAction()
    data object Copy : PostTweetAction()
    data object ValidateTweet : PostTweetAction()
}