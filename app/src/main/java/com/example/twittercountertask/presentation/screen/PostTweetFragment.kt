package com.example.twittercountertask.presentation.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.twittercountertask.R
import com.example.twittercountertask.databinding.FragmentPostTweetBinding
import com.example.twittercountertask.presentation.PostTweetViewmodel
import com.example.twittercountertask.presentation.action.PostTweetAction
import com.example.twittercountertask.presentation.event.PostTweetEvent
import com.example.twittercountertask.presentation.state.PostTweetScreenState
import com.example.ui.base.BasicFragment
import com.example.ui.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostTweetFragment :
    BasicFragment<FragmentPostTweetBinding, PostTweetScreenState, PostTweetEvent, PostTweetAction>(
        FragmentPostTweetBinding::inflate
    ) {
    override val viewModel: PostTweetViewmodel by viewModels()

    override fun onViewState(state: PostTweetScreenState) {
        if (state.isLoading) {
            showLoading()
        } else {
            hideLoading()
        }
        binding.characterRemaining.text = state.remaining.toString()
        binding.charactersTyped.text = "${state.typedChars} / 280"
    }

    override fun FragmentPostTweetBinding.initializeUI() {

        initViewEvent { event ->
            when (event) {
                is PostTweetEvent.Clear -> {
                    editTweet.setText("")
                }

                is PostTweetEvent.CopyText -> {
                    copyText(event.text)
                    showToast(getString(R.string.text_copied_to_clipboard))
                }

                is PostTweetEvent.ShowError -> {
                    showToast(event.message)
                }

                is PostTweetEvent.ShowErrorWithLocalMessage -> {
                    showToast(getString(event.resourceId))
                }

                PostTweetEvent.TweetSent -> {
                    showToast(getString(R.string.tweet_has_been_sent))
                }
            }
        }
    }

    override fun registerListeners() {
        binding.apply {
            editTweet.doAfterTextChanged {
                viewModel.handleAction(PostTweetAction.CheckRemaining(it.toString()))
            }
            btnSendTweet.setOnClickListener {
                viewModel.handleAction(PostTweetAction.SendTweet)
            }
            btnCopy.setOnClickListener {
                viewModel.handleAction(PostTweetAction.Copy)
            }
            btnClear.setOnClickListener {
                viewModel.handleAction(PostTweetAction.Clear)
            }
        }
    }

    private fun copyText(text : String){
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }

}