package com.example.twittercountertask.domain.usecase

import javax.inject.Inject

/**
 * Use case to count the remaining characters in a tweet,
 * considering Twitter's character limits and URL handling.
 */
class CountTweetWordsUseCase @Inject constructor() {

    private val maxCharacters = 280
    private val urlCharacterCount = 23 // URLs count as 23 characters

    /**
     * Calculates the remaining characters allowed in a tweet.
     * @param tweet The input tweet string.
     * @return Number of remaining characters.
     */
    fun remainingTweetCharacters(tweet: String): Int {
        return maxCharacters - countTweetCharacters(tweet)
    }

    /**
     * Counts the number of characters in a tweet, considering:
     * - URLs are treated as 23 characters.
     * - CJK (Chinese, Japanese, Korean) characters count as 2.
     * - Other characters follow their Unicode count.
     */
    private fun countTweetCharacters(tweet: String): Int {
        var characterCount = 0
        val words = tweet.split("\\s+".toRegex()) // Split by whitespace

        for (word in words) {
            when {
                word.startsWith("http://") || word.startsWith("https://") -> {
                    // Treat URLs as fixed-length (23 characters)
                    characterCount += urlCharacterCount
                }
                else -> {
                    word.forEach { char ->
                        characterCount += if (isCJKCharacter(char)) 2 else Character.charCount(char.code)
                    }
                }
            }
        }
        return characterCount
    }

    /**
     * Determines if a character belongs to the CJK (Chinese, Japanese, Korean) Unicode range.
     * CJK characters take up 2 character spaces in a tweet.
     */
    private fun isCJKCharacter(char: Char): Boolean {
        val codePoint = char.code
        return codePoint in 0x4E00..0x9FFF ||    // CJK Unified Ideographs
               codePoint in 0x3400..0x4DBF ||    // CJK Extension A
               codePoint in 0x20000..0x2A6DF ||  // CJK Extension B
               codePoint in 0x2A700..0x2B73F ||  // CJK Extension C
               codePoint in 0x2B740..0x2B81F ||  // CJK Extension D
               codePoint in 0x2B820..0x2CEAF     // CJK Extension E
    }
}
