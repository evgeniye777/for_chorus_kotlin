package com.example.for_chour_kotlin.data.typeData._interfaces

interface DataOperations<T> {
    fun setItems(itemList: List<T>)
    fun addItem(item: T): Int
    fun updateItem(item: T)
    fun deleteItem(item: T)
}