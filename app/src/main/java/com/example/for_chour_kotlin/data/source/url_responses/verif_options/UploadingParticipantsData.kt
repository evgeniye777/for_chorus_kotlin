package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL

class UploadingParticipantsData {
    fun start(hash_name_group: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (AccountHolder.hashName?.isNotEmpty() == true && AccountHolder.hashPassword?.isNotEmpty() == true && hash_name_group.isNotEmpty()) {
            ThreadURL().start(buildPostParams(hash_name_group),onSuccess,onFailure)
        }
        else {onFailure()}
    }
    private fun buildPostParams(hash_name_group: String): String {
        val params = mapOf(
            "Participants" to "",
            "UploadingParticipantsData" to "",
            "hash_name" to AccountHolder.hashName,
            "hash_password" to AccountHolder.hashPassword,
            "hash_name_group" to hash_name_group
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}