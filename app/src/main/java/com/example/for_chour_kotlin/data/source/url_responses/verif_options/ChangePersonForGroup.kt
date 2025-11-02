package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL

class ChangePersonForGroup {
    fun start(hash_name_add: String,hash_name_group: String,name:String,gender:String,access:String,post:String,allowed:String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (AccountHolder.hashName?.isNotEmpty() == true && AccountHolder.hashPassword?.isNotEmpty() == true && hash_name_group.isNotEmpty()) {
            ThreadURL().start(buildPostParams(hash_name_add,hash_name_group,name,gender,access,post,allowed),onSuccess,onFailure)
        }
        else {onFailure()}
    }
    private fun buildPostParams(hash_name_add: String,hash_name_group: String,name:String,gender:String,access:String,post:String,allowed:String): String {
        val params = mapOf(
            "Participants" to "",
            "ChangePersonForGroup" to "",
            "hash_name" to AccountHolder.hashName,
            "hash_password" to AccountHolder.hashPassword,
            "hash_name_add" to hash_name_add,
            "hash_name_group" to hash_name_group,
            "name" to name,
            "gender" to gender,
            "access" to access,
            "post" to post,
            "allowed" to allowed,
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }
}