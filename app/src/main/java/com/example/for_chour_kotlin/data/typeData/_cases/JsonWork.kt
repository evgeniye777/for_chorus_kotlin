package com.example.for_chour_kotlin.data.typeData._cases

import com.example.for_chour_kotlin.data.typeData.appAllGroups.TypeData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonWork {

    // Метод для преобразования строки из Json в MutableList<String>
    fun jsonToList(json: String): MutableList<String> {
        return try {
            val gson = Gson()
            val listType = object : TypeToken<MutableList<String>>() {}.type
            gson.fromJson<MutableList<String>>(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    // Метод для преобразования строки из Json в MutableList<HashMap<String,String>>
    fun jsonToListMH(json: String): MutableList<HashMap<String, String>> {
        return try {
            val gson = Gson()
            val listType = object : TypeToken<MutableList<HashMap<String, String>>>() {}.type
            gson.fromJson<MutableList<HashMap<String, String>>>(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    // Метод для преобразования строки из Json в HashMap<String, HashMap<String,String>>
    fun jsonToListHH(json: String): HashMap<String, HashMap<String, String>> {
        return try {
            val gson = Gson()
            val mapType = object : TypeToken<HashMap<String, HashMap<String, String>>>() {}.type
            gson.fromJson<HashMap<String, HashMap<String, String>>>(json, mapType) ?: hashMapOf()
        } catch (e: Exception) {
            hashMapOf()
        }
    }

    // Метод для преобразования строки из Json в HashMap<String,String>
    fun jsonToListH(json: String): HashMap<String, String> {
        return try {
            val gson = Gson()
            val mapType = object : TypeToken<HashMap<String, String>>() {}.type
            gson.fromJson<HashMap<String, String>>(json, mapType) ?: hashMapOf()
        } catch (e: Exception) {
            hashMapOf()
        }
    }

    fun jsonToTypeDataList(json: String): MutableList<TypeData> {
        return try {
            val gson = Gson()
            val listType = object : TypeToken<MutableList<TypeData>>() {}.type
            gson.fromJson<MutableList<TypeData>>(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    // Методы для преобразования в JSON-строку

    fun listToJson(list: MutableList<String>): String {
        return try {
            val gson = Gson()
            gson.toJson(list)
        } catch (e: Exception) {
            ""
        }
    }

    fun listToJsonMH(list: MutableList<HashMap<String, String>>): String {
        return try {
            val gson = Gson()
            gson.toJson(list)
        } catch (e: Exception) {
            ""
        }
    }

    fun listToJsonHH(list: HashMap<String, HashMap<String, String>>): String {
        return try {
            val gson = Gson()
            gson.toJson(list)
        } catch (e: Exception) {
            ""
        }
    }

    fun listToJsonH(list: HashMap<String, String>?): String {
        return try {
            val gson = Gson()
            gson.toJson(list)
        } catch (e: Exception) {
            ""
        }
    }

    fun typeDataListToJson(list: MutableList<TypeData>): String {
        return try {
            val gson = Gson()
            gson.toJson(list)
        } catch (e: Exception) {
            ""
        }
    }
}
