package com.example.for_chour_kotlin.data.source.url_responses.verif_options

import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.ThreadURL
import com.example.for_chour_kotlin.data.typeData.appStPersons.AppStPersons
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class SendStPersonData {
    fun start(hash_name_group: String, list_versions:String, appStPersons: AppStPersons, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (AccountHolder.hashName?.isNotEmpty() == true && AccountHolder.hashPassword?.isNotEmpty() == true && hash_name_group.isNotEmpty()) {
            ThreadURL().start(buildPostParams(hash_name_group,list_versions,appStPersons),onSuccess,onFailure)
        }
        else {onFailure()}
    }
    private fun buildPostParams(hash_name_group: String,list_versions:String,appStPersons: AppStPersons): String {
        val params = mapOf(
            "StatisticsPerson" to "",
            "AddRecordStPersonsSinch" to "",
            "hash_name" to AccountHolder.hashName,
            "hash_password" to AccountHolder.hashPassword,
            "hash_name_group" to hash_name_group,
            "last_index" to list_versions,
            "data" to appStPersons.toJson()
        )
        return params.entries.joinToString("&") { "${it.key}=${it.value}" }
    }

    fun AppStPersons.toJson(): String {
        val gson = Gson()
        val jsonObject = JsonObject()
        jsonObject.addProperty("committer", this.committer)
        jsonObject.addProperty("date", this.date)
        jsonObject.addProperty("purpose", this.purpose.toString())

        val dataArray = JsonArray()
        this.data?.split(",")?.forEach { dataArray.add(it.trim()) }
        jsonObject.add("data", dataArray)

        val commentsArray = JsonArray()
        this.comments?.split(",")?.forEach { commentsArray.add(it.trim()) }
        jsonObject.add("comments", commentsArray)

        jsonObject.add("c", gson.toJsonTree(this.c))

        val jsonArray = JsonArray()
        jsonArray.add(jsonObject)
        return gson.toJson(jsonArray)
    }
}