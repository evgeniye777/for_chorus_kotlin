package com.example.for_chour_kotlin.data.typeData._cases

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonWork {
    //Метод для преобразования строки из Json в List<String>
    fun jsonToList(json: String): MutableList<String> {
        val gson = Gson()
        val listType = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, listType)
    }

    fun jsonToListMH(json: String): MutableList<HashMap<String,String>> {
        val gson = Gson()
        val listType = object : TypeToken<MutableList<HashMap<String,String>>>() {}.type
        return gson.fromJson(json, listType)
    }

    fun jsonToListHH(json: String): HashMap<String,HashMap<String,String>> {
        val gson = Gson()
        val listType = object : TypeToken<HashMap<String,HashMap<String,String>>>() {}.type
        return gson.fromJson(json, listType)
    }

    // Методы для преобразования в JSON-строку
    fun listToJson(list: MutableList<String>): String {
        val gson = Gson()
        return gson.toJson(list) // Преобразуем список в JSON-строку
    }
    fun listToJsonMH(list: MutableList<HashMap<String,String>>): String {
        val gson = Gson()
        return gson.toJson(list) // Преобразуем список в JSON-строку
    }
    fun listToJsonHH(list: HashMap<String,HashMap<String,String>>): String {
        val gson = Gson()
        return gson.toJson(list) // Преобразуем список в JSON-строку
    }
}