package com.example.for_chour_kotlin.data.model

interface DataChangeListener {
    fun <T> onStaticDataChanged(data: T, action: Int) : Int

    fun <T> onDynamicDataChanged(data: T, nameTable: String? = null, action: Int) : Int
}

interface AppStPersons {
    var id: Int
    var committer: String?
    var dateWrite: String?
    var date: String?
    var purpose: Int
    var data: String?
    var comments: String?
    var c: List<String>
}