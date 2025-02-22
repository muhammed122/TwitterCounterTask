package com.example.network.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.math.BigInteger
import java.net.URLEncoder
import java.security.SecureRandom
import java.util.Base64
import java.util.Date
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * OAuth1Interceptor adds OAuth 1.0 authentication headers to HTTP requests.
 *
 * @param consumerKey Twitter API Consumer Key
 * @param consumerSecret Twitter API Consumer Secret
 * @param token OAuth Token
 * @param tokenSecret OAuth Token Secret
 */
class OAuth1Interceptor(
    private val consumerKey: String,
    private val consumerSecret: String,
    private val token: String,
    private val tokenSecret: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        // Generate OAuth parameters
        val nonce = generateNonce()
        val timestamp = generateTimestamp()

        // Prepare OAuth parameters map
        val oauthParams = mapOf(
            "oauth_consumer_key" to consumerKey,
            "oauth_token" to token,
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to timestamp,
            "oauth_nonce" to nonce,
            "oauth_version" to "1.0"
        )

        // Generate signature
        val signatureBaseString = createSignatureBaseString(originalRequest.method, originalRequest.url.toString(), oauthParams)
        val signingKey = createSigningKey(consumerSecret, tokenSecret)
        val signature = generateSignature(signatureBaseString, signingKey)

        // Generate OAuth Authorization header
        val authorizationHeader = createOAuthHeader(oauthParams, signature)

        // Attach headers to request
        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", authorizationHeader)
            .build()

        return chain.proceed(requestWithAuth)
    }

    /**
     * Generates a unique nonce (random string) for each request.
     */
    private fun generateNonce(): String {
        val random = SecureRandom()
        return BigInteger(130, random).toString(32)
    }

    /**
     * Generates the current timestamp in seconds.
     */
    private fun generateTimestamp(): String {
        return (Date().time / 1000).toString()
    }

    /**
     * Constructs the OAuth Authorization header.
     */
    private fun createOAuthHeader(params: Map<String, String>, signature: String): String {
        return "OAuth " + params.entries.joinToString(", ") { "${encode(it.key)}=\"${encode(it.value)}\"" } +
                ", oauth_signature=\"${encode(signature)}\""
    }

    /**
     * Encodes a value according to OAuth 1.0 specifications.
     */
    private fun encode(value: String): String {
        return URLEncoder.encode(value, "UTF-8")
            .replace("+", "%20")
            .replace("*", "%2A")
            .replace("%7E", "~")
    }

    /**
     * Creates the base string for signature generation.
     */
    private fun createSignatureBaseString(method: String, url: String, params: Map<String, String>): String {
        val sortedParams = params.entries.sortedBy { it.key }
            .joinToString("&") { "${encode(it.key)}=${encode(it.value)}" }
        return "$method&${encode(url)}&${encode(sortedParams)}"
    }

    /**
     * Generates the HMAC-SHA1 signature.
     */
    private fun generateSignature(baseString: String, signingKey: String): String {
        val mac = Mac.getInstance("HmacSHA1")
        val keySpec = SecretKeySpec(signingKey.toByteArray(Charsets.UTF_8), "HmacSHA1")
        mac.init(keySpec)
        val rawSignature = mac.doFinal(baseString.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(rawSignature)
    }

    /**
     * Creates the signing key for OAuth 1.0 signature.
     */
    private fun createSigningKey(consumerSecret: String, tokenSecret: String): String {
        return "${encode(consumerSecret)}&${encode(tokenSecret)}"
    }
}
