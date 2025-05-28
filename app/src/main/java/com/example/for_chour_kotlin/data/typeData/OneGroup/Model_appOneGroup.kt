package com.example.for_chour_kotlin.data.typeData.OneGroup

data class AppOneGroup(
    var id: Int = 0,
    var version: Int = -1,
    var hashName: String /*уникальное*/,
    var name: String,
    var date: String,
    var location: String,
    var creator: String,
    //ссылки на базы данных
    var lisnNameBases: List<String>,
    var n_notification: Int = 0
)