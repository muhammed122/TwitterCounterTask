package com.example.twittercountertask.presentation

import androidx.lifecycle.viewModelScope
import com.example.network.utils.Resource
import com.example.twittercountertask.R
import com.example.twittercountertask.domain.usecase.CountTweetWordsUseCase
import com.example.twittercountertask.domain.usecase.PostTweetUseCase
import com.example.twittercountertask.presentation.action.PostTweetAction
import com.example.twittercountertask.presentation.event.PostTweetEvent
import com.example.twittercountertask.presentation.state.PostTweetScreenState
import com.example.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostTweetViewmodel @Inject constructor(
    private val postTweetUseCase: PostTweetUseCase,
    private val countTweetWordsUseCase: CountTweetWordsUseCase,
) :
    BaseViewModel<PostTweetScreenState, PostTweetEvent,
            PostTweetAction>(initialState = PostTweetScreenState()) {

    override fun handleAction(action: PostTweetAction) {
        when (action) {
            is PostTweetAction.CheckRemaining -> {
                setState {
                    copy(
                        text = action.tweet,
                        isTextValid = action.tweet.trim().isNotEmpty()
                    )
                }
                handleRemainingCharacter(action.tweet)
            }

            is PostTweetAction.SendTweet -> {
                if (validateTweet()) {
                    sendTweet()
                }
            }

            PostTweetAction.Clear -> {
                setEvent {
                    PostTweetEvent.Clear
                }
            }

            PostTweetAction.Copy -> {
                setEvent {
                    PostTweetEvent.CopyText(currentState.text)
                }
            }

            PostTweetAction.ValidateTweet -> {
                validateTweet()
            }
        }
    }


    private fun validateTweet(): Boolean {
        if (currentState.isTextValid) {
            return true
        }
        setEvent {
            PostTweetEvent.ShowErrorWithLocalMessage(R.string.tweet_can_t_be_empty)
        }
        return false
    }

    private fun handleRemainingCharacter(tweet: String) {
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        val charTyped = 280 - remaining
        setState { copy(remaining = remaining, typedChars = charTyped) }

    }

    private fun sendTweet() {
        setState { copy(isLoading = true) }
        viewModelScope.launch(IO) {
            val data = postTweetUseCase.invoke(currentState.text)
            setState { copy(isLoading = false) }
            when (data) {
                is Resource.Success -> {
                    setEvent {
                        PostTweetEvent.TweetSent
                    }
                    setEvent {
                        PostTweetEvent.Clear
                    }

                }

                is Resource.Failure -> {
                    setEvent {
                        PostTweetEvent.ShowError(data.getErrorMessage().toString())
                    }
                }
            }
        }
    }
}