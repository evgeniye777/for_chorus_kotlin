package com.example.for_chour_kotlin.data.model

interface DataChangeListener {
    fun <T> onDataChanged(data: T, action: Int) : Int
}