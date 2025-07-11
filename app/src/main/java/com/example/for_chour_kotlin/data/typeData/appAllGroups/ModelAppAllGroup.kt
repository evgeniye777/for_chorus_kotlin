package com.example.for_chour_kotlin.data.typeData.appAllGroups

data class AppAllGroups(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String? = null,
    var name: String? = null,
    var date: String? = null,
    var location: String? = null,
    var creator: String? = null,
    var listNameBases: MutableList<String> = mutableListOf(),
    var data: HashMap<String,HashMap<String,String>>? = null,
    var nNotification: Int = 0,
    var visible: Int = 1
)