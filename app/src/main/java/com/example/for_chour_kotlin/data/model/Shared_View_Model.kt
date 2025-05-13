package com.example.for_chour_kotlin.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Shared_View_Model : ViewModel() {

    var listener: DataChangeListener? = null // Сделаем его публичным

    private val _groups = MutableLiveData<List<AppAllGroups>>()
    val groups: LiveData<List<AppAllGroups>> get() = _groups

    private val _persons = MutableLiveData<List<AppAllPersons>>()
    val persons: LiveData<List<AppAllPersons>> get() = _persons

    private val _appGroupsDataMain = MutableLiveData<List<AppGroupDataParticipants>>()
    val appGroupsDataMain: LiveData<List<AppGroupDataParticipants>> get() = _appGroupsDataMain

    private val _localTimes = MutableLiveData<List<AppStPersonsLocalTime>>()
    val localTimes: LiveData<List<AppStPersonsLocalTime>> get() = _localTimes

    private val _sinches = MutableLiveData<List<AppStPersonsSinch>>()
    val sinches: LiveData<List<AppStPersonsSinch>> get() = _sinches

    private val _songsHistories = MutableLiveData<List<AppStSongsHistory>>()
    val songsHistories: LiveData<List<AppStSongsHistory>> get() = _songsHistories

    private val _songsPlans = MutableLiveData<List<AppStSongsPlans>>()
    val songsPlans: LiveData<List<AppStSongsPlans>> get() = _songsPlans

    private val _trigers = MutableLiveData<List<Triger>>()
    val trigers: LiveData<List<Triger>> get() = _trigers

    private val _types = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>> get() = _types

    // Метод для установки списка групп
    fun setGroups(groupList: List<AppAllGroups>) {
        _groups.value = groupList
    }

    // Метод для установки списка персон
    fun setPersons(personList: List<AppAllPersons>) {
        _persons.value = personList
    }

    // Метод для установки списка групп
    fun setAppGroups(appGroupDataParticipantsList: List<AppGroupDataParticipants>) {
        _appGroupsDataMain.value = appGroupDataParticipantsList
    }

    // Метод для установки списка локальных времён
    fun setLocalTimes(localTimeList: List<AppStPersonsLocalTime>) {
        _localTimes.value = localTimeList
    }

    // Метод для установки списка синчей
    fun setSinches(sinchList: List<AppStPersonsSinch>) {
        _sinches.value = sinchList
    }

    // Метод для установки списка историй песен
    fun setSongsHistories(songsHistoryList: List<AppStSongsHistory>) {
        _songsHistories.value = songsHistoryList
    }

    // Метод для установки списка планов песен
    fun setSongsPlans(songsPlanList: List<AppStSongsPlans>) {
        _songsPlans.value = songsPlanList
    }

    // Метод для установки списка триггеров
    fun setTriggers(trigerList: List<Triger>) {
        _trigers.value = trigerList
    }

    // Метод для установки списка типов
    fun setTypes(typeList: List<Type>) {
        _types.value = typeList
    }

    //Метод для добавления
    fun <T> addItem(item: T): Int {
        var newId: Int = listener?.onDataChanged(item, 0) ?: -1
        if (newId >= 0) {
            when (item) {
                is AppAllGroups -> {
                    val currentList = _groups.value ?: emptyList()
                    _groups.value = currentList + item.apply { id = newId }
                }
                is AppAllPersons -> {
                    val currentList = _persons.value ?: emptyList()
                    _persons.value = currentList + item.apply { id = newId }
                }
                is AppGroupDataParticipants -> {
                    val currentList = _appGroupsDataMain.value ?: emptyList()
                    _appGroupsDataMain.value = currentList + item.apply { id = newId }
                }
                is AppStPersonsLocalTime -> {
                    val currentList = _localTimes.value ?: emptyList()
                    _localTimes.value = currentList + item.apply { id = newId }
                }
                is AppStPersonsSinch -> {
                    val currentList = _sinches.value ?: emptyList()
                    _sinches.value = currentList + item.apply { id = newId }
                }
                is AppStSongsHistory -> {
                    val currentList = _songsHistories.value ?: emptyList()
                    _songsHistories.value = currentList + item.apply { id = newId }
                }
                is AppStSongsPlans -> {
                    val currentList = _songsPlans.value ?: emptyList()
                    _songsPlans.value = currentList + item.apply { id = newId }
                }
                is Triger -> {
                    val currentList = _trigers.value ?: emptyList()
                    _trigers.value = currentList + item.apply { id = newId }
                }
                is Type -> {
                    val currentList = _types.value ?: emptyList()
                    _types.value = currentList + item.apply { id = newId }
                }
                else -> {
                    throw IllegalArgumentException("Unsupported type")
                }
            }
        } else {
            newId = -1 // Если добавление не произошло, возвращаем -1
        }
        return newId
    }


    //Метод для редактирования
    fun <T> updateItem(item: T) {
        val n: Int = listener?.onDataChanged(item, 1) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            when (item) {
                is AppAllGroups -> {
                    val currentList = _groups.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _groups.value = currentList
                    }
                }
                is AppAllPersons -> {
                    val currentList = _persons.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _persons.value = currentList
                    }
                }
                is AppGroupDataParticipants -> {
                    val currentList = _appGroupsDataMain.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _appGroupsDataMain.value = currentList
                    }
                }
                is AppStPersonsLocalTime -> {
                    val currentList = _localTimes.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _localTimes.value = currentList
                    }
                }
                is AppStPersonsSinch -> {
                    val currentList = _sinches.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _sinches.value = currentList
                    }
                }
                is AppStSongsHistory -> {
                    val currentList = _songsHistories.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _songsHistories.value = currentList
                    }
                }
                is AppStSongsPlans -> {
                    val currentList = _songsPlans.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _songsPlans.value = currentList
                    }
                }
                is Triger -> {
                    val currentList = _trigers.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _trigers.value = currentList
                    }
                }
                is Type -> {
                    val currentList = _types.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.name == item.name } // Предполагаем, что уникальность по имени
                    if (index != -1) {
                        currentList[index] = item // Обновляем элемент
                        _types.value = currentList
                    }
                }
                else -> {
                    throw IllegalArgumentException("Unsupported type")
                }
            }
        }
    }


    //Метод для удаления
    fun <T> deleteItem(item: T) {
        val n: Int = listener?.onDataChanged(item, 2) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            when (item) {
                is AppAllGroups -> {
                    val currentList = _groups.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _groups.value = currentList
                }
                is AppAllPersons -> {
                    val currentList = _persons.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _persons.value = currentList
                }
                is AppGroupDataParticipants -> {
                    val currentList = _appGroupsDataMain.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _appGroupsDataMain.value = currentList
                }
                is AppStPersonsLocalTime -> {
                    val currentList = _localTimes.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _localTimes.value = currentList
                }
                is AppStPersonsSinch -> {
                    val currentList = _sinches.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _sinches.value = currentList
                }
                is AppStSongsHistory -> {
                    val currentList = _songsHistories.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _songsHistories.value = currentList
                }
                is AppStSongsPlans -> {
                    val currentList = _songsPlans.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _songsPlans.value = currentList
                }
                is Triger -> {
                    val currentList = _trigers.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.id == item.id } // Удаляем элемент
                    _trigers.value = currentList
                }
                is Type -> {
                    val currentList = _types.value?.toMutableList() ?: mutableListOf()
                    currentList.removeIf { it.name == item.name } // Предполагаем, что уникальность по имени
                    _types.value = currentList
                }
                else -> {
                    throw IllegalArgumentException("Unsupported type")
                }
            }
        }
    }


}