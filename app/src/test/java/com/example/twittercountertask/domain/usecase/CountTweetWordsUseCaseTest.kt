package com.example.twittercountertask.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CountTweetWordsUseCaseTest {

    private lateinit var countTweetWordsUseCase: CountTweetWordsUseCase

    @Before
    fun setUp() {
        countTweetWordsUseCase = CountTweetWordsUseCase()
    }

    @Test
    fun `empty tweet should return 280 remaining characters`() {
        val remaining = countTweetWordsUseCase.remainingTweetCharacters("")
        assertThat(remaining).isEqualTo(280)
    }

    @Test
    fun `tweet with regular characters should return correct remaining count`() {
        val tweet = "Hello Twitter!"
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(280 - tweet.length)
    }

    @Test
    fun `tweet containing a single URL should reduce count by 23 characters`() {
        val tweet = "Check this out: https://example.com"
        val expectedLength = 280 - (23 + "Check this out: ".length)
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(expectedLength)
    }

    @Test
    fun `tweet with multiple URLs should count each as 23 characters`() {
        val tweet = "Visit https://example.com and http://google.com"
        val expectedLength = 280 - (23 + 23 + "Visit  and ".length)
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(expectedLength)
    }

    @Test
    fun `tweet with CJK characters should count them as 2 characters each`() {
        val tweet = "你好世界" // "Hello World" in Chinese (4 characters)
        val expectedLength = 280 - (4 * 2) // Each CJK character counts as 2
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(expectedLength)
    }

    @Test
    fun `tweet with mixed CJK and English characters should calculate correctly`() {
        val tweet = "你好 world" // "你好" (2 CJK) + " world" (6 ASCII)
        val expectedLength = 280 - ((2 * 2) + 6) // 2 CJK * 2 + 6 ASCII
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(expectedLength)
    }

    @Test
    fun `tweet at max limit should return zero remaining characters`() {
        val tweet = "a".repeat(280)
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(0)
    }

    @Test
    fun `tweet exceeding limit should return negative remaining characters`() {
        val tweet = "a".repeat(300)
        val remaining = countTweetWordsUseCase.remainingTweetCharacters(tweet)
        assertThat(remaining).isEqualTo(-20) // 300 - 280 = -20
    }
}
