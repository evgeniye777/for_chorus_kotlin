package com.example.for_chour_kotlin.data.typeData.appStPersons

data class AppStPersons(
    var id: Int? = null,
    var committer: String?,
    var dateWrite: String,
    var date: String,
    var purpose: Int,
    var data: String? = null,
    var comments: String? = null,
    var c: HashMap<String, String> = HashMap(),
    var sinch: Int = 0
)

data class AppStPersonsSimple(
    var date: String
)