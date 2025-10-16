package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL

class UploadingUpdatesGroups  {
    fun start(versions:String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (AccountHolder.hashName?.isNotEmpty() == true && AccountHolder.hashPassword?.isNotEmpty() == true) {
            ThreadURL().start(buildPostParams(versions),onSuccess,onFailure)
        }
        else {onFailure()}
    }
    private fun buildPostParams(versions: String): String {
        val params = mapOf(
            "Allgroups" to "",
            "UploadingUpdatesGroups" to "",
            "hash_name" to AccountHolder.hashName,
            "hash_password" to AccountHolder.hashPassword,
            "versions" to versions
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}