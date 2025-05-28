package com.example.for_chour_kotlin.data.typeData._interfaces


interface AppDataListener<T> {
    fun onAppDataChanged(data: T, action: Int) : Int
}
interface LocalBdListener<T> {
    fun createTable(nameTable: String) {
        println("createTable")
    }
    fun deleteTable(nameTable: String) {
        println("delTable")
    }
    fun readItems(nameTable: String = "")
    fun addItem(item: T): Int
    fun updateItem(item: T)
    fun deleteItem(item: T)
}