package com.example.twittercountertask.presentation.state

import com.example.ui.base.ViewState

data class PostTweetScreenState(
    val isLoading: Boolean = false,
    val remaining: Int = 280,
    val typedChars: Int = 0,
    val text: String = "",
    val isTextValid: Boolean = false,
) : ViewState