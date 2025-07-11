package com.example.for_chour_kotlin.data.typeData.appStPersonsLocalTime

data class AppStPersonsLocalTime(
    var id: Int? = null,
    var committer: String? = null,
    var dateWrite: String? = null,
    var date: String? = null,
    var purpose: Int = 0,
    var data: String? = null,
    var comments: String? = null,
    var c: MutableList<String> = mutableListOf()
)