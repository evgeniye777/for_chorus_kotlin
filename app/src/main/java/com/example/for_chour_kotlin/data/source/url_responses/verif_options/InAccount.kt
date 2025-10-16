package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL

class InAccount {
    fun start(loginIn: String, hash_password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        ThreadURL().start(buildPostParams(loginIn,hash_password),onSuccess,onFailure)
    }
    private fun buildPostParams(loginIn: String, hash_password: String): String {
        val params = mapOf(
            "Authorization" to "",
            "InAccount" to "",
            "login" to loginIn,
            "hash_password" to hash_password
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}