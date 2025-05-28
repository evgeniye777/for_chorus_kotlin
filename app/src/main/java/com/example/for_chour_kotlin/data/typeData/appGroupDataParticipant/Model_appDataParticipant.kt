package com.example.for_chour_kotlin.data.typeData.appGroupDataParticipant

data class AppGroupDataParticipant(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String? = null,
    var date: String? = null,
    var pName: String? = null,
    var pGender: Int? = 0,
    var post: String? = null,
    var allowed: Int = 0,
    var access: Int = 0,
    var visible: Int = 1
)