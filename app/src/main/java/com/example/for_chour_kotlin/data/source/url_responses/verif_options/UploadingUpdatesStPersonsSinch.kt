package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL

class UploadingUpdatesStPersonsSinch {
    fun start(hash_name_group: String,list_versions:String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (AccountHolder.hashName?.isNotEmpty() == true && AccountHolder.hashPassword?.isNotEmpty() == true && hash_name_group.isNotEmpty()) {
            ThreadURL().start(buildPostParams(hash_name_group,list_versions),onSuccess,onFailure)
        }
        else {onFailure()}
    }
    private fun buildPostParams(hash_name_group: String,list_versions:String): String {
        val params = mapOf(
            "StatisticsPerson" to "",
            "UploadingUpdatesStPersonsSinch" to "",
            "hash_name" to AccountHolder.hashName,
            "hash_password" to AccountHolder.hashPassword,
            "hash_name_group" to hash_name_group,
            "last_index" to list_versions
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}