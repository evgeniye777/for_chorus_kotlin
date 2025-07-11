package com.example.for_chour_kotlin.data.typeData.appStPersonsSinch

data class AppStPersonsSinch(
    var id: Int? = null,
    var committer: String? = null,
    var dateWrite: String? = null,
    var date: String? = null,
    var purpose: Int? = null,
    var data: String? = null,
    var comments: String? = null,
    var c: MutableList<String> = mutableListOf()
)