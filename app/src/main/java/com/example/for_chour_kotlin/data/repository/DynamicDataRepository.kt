package com.example.for_chour_kotlin.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.for_chour_kotlin.data.model.*
import com.example.for_chour_kotlin.data.typeData.appGroupDataParticipant.AppGroupDataParticipant

class DynamicDataRepository(
    private var _participants: MutableLiveData<List<AppGroupDataParticipant>>,
    private var _localTimes: MutableLiveData<List<AppStPersonsLocalTime>>,
    private var _sinches: MutableLiveData<List<AppStPersonsSinch>>,
    private var _songsHistory: MutableLiveData<List<AppStSongsHistory>>,
    private var _songsPlans: MutableLiveData<List<AppStSongsPlans>>
) {

    //Метод для добавления объектов(записей) динамических таблиц
    fun <T> addItem(item: T): Int {
        var newId = -1
        when (item) {
            is AppGroupDataParticipant -> {
                val currentList = _participants.value?.toMutableList() ?: mutableListOf()
                newId = currentList.size + 1
                currentList.add(item.apply { id = newId })
                _participants.value = currentList
            }
            is AppStPersonsLocalTime -> {
                val currentList = _localTimes.value?.toMutableList() ?: mutableListOf()
                newId = currentList.size + 1
                currentList.add(item.apply { id = newId })
                _localTimes.value = currentList
            }
            is AppStPersonsSinch -> {
                val currentList = _sinches.value?.toMutableList() ?: mutableListOf()
                newId = currentList.size + 1
                currentList.add(item.apply { id = newId })
                _sinches.value = currentList
            }
            is AppStSongsHistory -> {
                val currentList = _songsHistory.value?.toMutableList() ?: mutableListOf()
                newId = currentList.size + 1
                currentList.add(item.apply { id = newId })
                _songsHistory.value = currentList
            }
            is AppStSongsPlans -> {
                val currentList = _songsPlans.value?.toMutableList() ?: mutableListOf()
                newId = currentList.size + 1
                currentList.add(item.apply { id = newId })
                _songsPlans.value = currentList
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
        return newId
    }

    fun <T> updateItem(item: T) {
        when (item) {
            is AppGroupDataParticipant -> {
                val currentList = _participants.value?.toMutableList() ?: mutableListOf()
                val index = currentList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    currentList[index] = item
                    _participants.value = currentList
                }
            }
            is AppStPersonsLocalTime -> {
                val currentList = _localTimes.value?.toMutableList() ?: mutableListOf()
                val index = currentList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    currentList[index] = item
                    _localTimes.value = currentList
                }
            }
            is AppStPersonsSinch -> {
                val currentList = _sinches.value?.toMutableList() ?: mutableListOf()
                val index = currentList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    currentList[index] = item
                    _sinches.value = currentList
                }
            }
            is AppStSongsHistory -> {
                val currentList = _songsHistory.value?.toMutableList() ?: mutableListOf()
                val index = currentList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    currentList[index] = item
                    _songsHistory.value = currentList
                }
            }
            is AppStSongsPlans -> {
                val currentList = _songsPlans.value?.toMutableList() ?: mutableListOf()
                val index = currentList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    currentList[index] = item
                    _songsPlans.value = currentList
                }
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    fun <T> deleteItem(item: T) {
        when (item) {
            is AppGroupDataParticipant -> {
                val currentList = _participants.value?.toMutableList() ?: mutableListOf()
                if (currentList.removeAll { it.id == item.id }) {
                    _participants.value = currentList
                }
            }
            is AppStPersonsLocalTime -> {
                val currentList = _localTimes.value?.toMutableList() ?: mutableListOf()
                if (currentList.removeAll { it.id == item.id }) {
                    _localTimes.value = currentList
                }
            }
            is AppStPersonsSinch -> {
                val currentList = _sinches.value?.toMutableList() ?: mutableListOf()
                if (currentList.removeAll { it.id == item.id }) {
                    _sinches.value = currentList
                }
            }
            is AppStSongsHistory -> {
                val currentList = _songsHistory.value?.toMutableList() ?: mutableListOf()
                if (currentList.removeAll { it.id == item.id }) {
                    _songsHistory.value = currentList
                }
            }
            is AppStSongsPlans -> {
                val currentList = _songsPlans.value?.toMutableList() ?: mutableListOf()
                if (currentList.removeAll { it.id == item.id }) {
                    _songsPlans.value = currentList
                }
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}
