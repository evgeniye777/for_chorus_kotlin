package com.example.for_chour_kotlin.data.source.url_responses.handlers

import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState

import org.json.JSONObject
import android.util.Log
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData.appAllGroups.AppAllGroups
import com.example.for_chour_kotlin.data.typeData.appAllGroups.TypeData

class GroupResponseHandler(
) {
    fun handle(jsonObject: JSONObject): Boolean {
        val success = jsonObject.optInt("success", -1)
        when (success) {
            0 -> {
                Log.d("GroupHandler", "Group response failed (success=0)")
                return false
            }
            1 -> {
                Log.d("GroupHandler", "Group response success (success=1)")
                return true
            }
            2 -> {
                val dataArray = jsonObject.optJSONArray("data")
                if (dataArray == null || dataArray.length() == 0) {
                    Log.w("GroupHandler", "No group data in response")
                    return true
                }

                var allProcessedSuccessfully = true
                val groupsViewModel = AuthorizationState.groups // Доступ к глобальному ViewModel

                for (i in 0 until dataArray.length()) {
                    val groupJson = dataArray.getJSONObject(i)
                    try {
                        val actions = groupJson.optString("actions", "")  // Получаем значение "actions", по умолчанию пустая строка

                        if (actions == "delete") {
                            // Обработка случая удаления
                            val hashName = groupJson.optString("hash_name")
                            if (hashName.isNotEmpty()) {
                                //deleteGroupByHashName(hashName)  // Вызываем метод удаления
                                Log.d("GroupHandler", "Successfully processed delete for hashName=$hashName")
                            } else {
                                Log.e("GroupHandler", "Hash name is missing for delete action at index $i")
                                allProcessedSuccessfully = false
                            }
                        } else {
                            // Обычная обработка: парсинг и добавление/обновление
                            val parsedGroup = parseGroupFromJson(groupJson)
                            val result = groupsViewModel?.addOrUpdateItem(parsedGroup)
                            if (result != null) {
                                if (result < 0) {
                                    Log.e("GroupHandler", "Failed to add/update group with id=${parsedGroup.id}, hashName=${parsedGroup.hashName}")
                                    allProcessedSuccessfully = false
                                } else {
                                    Log.d("GroupHandler", "Successfully added/updated group with id=${parsedGroup.id}, hashName=${parsedGroup.hashName}")
                                }
                            } else {
                                Log.e("GroupHandler", "groupsViewModel.addOrUpdateItem returned null at index $i")
                                allProcessedSuccessfully = false
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("GroupHandler", "Error processing group at index $i: ${e.message}", e)
                        allProcessedSuccessfully = false
                    }
                }

                if (allProcessedSuccessfully) {
                    Log.d("GroupHandler", "All groups processed successfully")
                    return true
                } else {
                    Log.e("GroupHandler", "Some groups failed to process")
                    return false
                }
            }
            else -> {
                Log.e("GroupHandler", "Unknown success value: $success")
                return false
            }
        }
    }

    // Вспомогательная функция для парсинга одного AppAllGroups из JSONObject
    private fun parseGroupFromJson(groupJson: JSONObject): AppAllGroups {
        val id = groupJson.optString("id", "0").toIntOrNull() ?: 0
        val version = groupJson.optString("version", "-1").toIntOrNull() ?: -1
        val hashName = groupJson.optString("hash_name", "")
        val name = groupJson.optString("name", null)
        val dateCreate = groupJson.optString("date_create", null)
        val location = groupJson.optString("location", null).takeIf { it != "null" } // Обработка null как строки
        val creator = groupJson.optString("creator", null)
        val visible = groupJson.optString("visible", "1").toIntOrNull() ?: 1

        val jsonWork = JsonWork()

        val listNameBasesJson = groupJson.optJSONObject("list_name_bases")
        val listNameBases = HashMap<String, String>()
        if (listNameBasesJson != null) {
            val listNameBasesStr = listNameBasesJson.toString()
            val parsedListNameBases = jsonWork.jsonToListH(listNameBasesStr)
            listNameBases.putAll(parsedListNameBases)
        }

        val dataArray = groupJson.optJSONArray("data")
        val dataList = mutableListOf<TypeData>()
        if (dataArray != null) {
            val dataStr = dataArray.toString()
            val parsedDataList = jsonWork.jsonToTypeDataList(dataStr)
            dataList.addAll(parsedDataList)
        }

        return AppAllGroups(
            id = id,
            version = version,
            hashName = hashName,
            name = name,
            date_create = dateCreate,
            location = location,
            creator = creator,
            listNameBases = listNameBases,
            data = dataList,
            nNotification = 0, // По умолчанию, как в модели
            visible = visible
        )
    }
}
