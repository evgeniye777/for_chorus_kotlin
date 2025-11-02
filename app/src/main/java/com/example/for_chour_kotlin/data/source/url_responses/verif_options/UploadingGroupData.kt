package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL
import kotlin.plus

class UploadingGroupData  {
    fun start( onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (AccountHolder.hashName?.isNotEmpty() == true && AccountHolder.hashPassword?.isNotEmpty() == true) {
            ThreadURL().start(buildPostParams(),onSuccess,onFailure)
        }
        else { onFailure()}
    }
    private fun buildPostParams(): String {
        val params = mapOf(
            "Allgroups" to "",
            "UploadingGroupData" to "",
            "hash_name" to AccountHolder.hashName,
            "hash_password" to AccountHolder.hashPassword
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}