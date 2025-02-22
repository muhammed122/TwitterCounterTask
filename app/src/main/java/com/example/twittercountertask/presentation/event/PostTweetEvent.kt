package com.example.twittercountertask.presentation.event

import com.example.ui.base.ViewEvent

sealed class PostTweetEvent : ViewEvent {
    data object Clear : PostTweetEvent()
    data object TweetSent : PostTweetEvent()
    data class CopyText(val text: String) : PostTweetEvent()
    data class ShowErrorWithLocalMessage(val resourceId: Int) : PostTweetEvent()
    data class ShowError(val message: String) : PostTweetEvent()
}