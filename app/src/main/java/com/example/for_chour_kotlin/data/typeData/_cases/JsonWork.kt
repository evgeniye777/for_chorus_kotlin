package com.example.for_chour_kotlin.data.typeData._cases

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonWork {
    //Метод для преобразования строки из Json в List<String>
    fun jsonToList(json: String): List<String> {
        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, listType)
    }

    // Метод для преобразования List<String> в JSON-строку
    fun listToJson(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list) // Преобразуем список в JSON-строку
    }
}