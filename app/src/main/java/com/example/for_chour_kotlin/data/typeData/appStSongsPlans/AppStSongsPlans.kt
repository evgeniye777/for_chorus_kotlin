package com.example.for_chour_kotlin.data.typeData.appStSongsPlans

data class AppStSongsPlans(
    var id: Int = 0,
    var version: Int = -1,
    var committer: String? = null,
    var dateWrite: String? = null,
    var date: String? = null,
    var purpose: Int = 0,
    var data: MutableList<HashMap<String, String>> = mutableListOf(),
    var comments: String? = null,
    var visible: Int = 1
)