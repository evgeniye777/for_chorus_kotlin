package com.example.for_chour_kotlin.presentations._All_View_Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.model.*
import com.example.for_chour_kotlin.data.repository.DynamicDataRepository

class ViewModel_DynamicTable : ViewModel() {

    // Поля
    var listener: DataChangeListener? = null // Сделаем его публичным

    //Название группы
    private val _nameTable = MutableLiveData<String>()
    val nameTable: LiveData<String> get() = _nameTable

    // List для динамических таблиц
    private val _participants = MutableLiveData<List<AppGroupDataParticipant>>()
    val participants: LiveData<List<AppGroupDataParticipant>> get() = _participants

    private val _localTimes = MutableLiveData<List<AppStPersonsLocalTime>>()
    val localTimes: LiveData<List<AppStPersonsLocalTime>> get() = _localTimes

    private val _sinches = MutableLiveData<List<AppStPersonsSinch>>()
    val sinches: LiveData<List<AppStPersonsSinch>> get() = _sinches

    private val _songsHistory = MutableLiveData<List<AppStSongsHistory>>()
    val songsHistory: LiveData<List<AppStSongsHistory>> get() = _songsHistory

    private val _songsPlans = MutableLiveData<List<AppStSongsPlans>>()
    val songsPlans: LiveData<List<AppStSongsPlans>> get() = _songsPlans

    // Создаем экземпляр репозитория для динамических таблиц
    private val dynamicDataRepository = DynamicDataRepository(_participants, _localTimes, _sinches, _songsHistory, _songsPlans
    )

    // Операции с таблицами

    //Обновление списков (пре переключении с одной группы на другую или же если произведено удаление грппы то списки опустошаются)
    //Также данные метод всегда используется при открытии приложении и загрузки данных группы по умолчанию
    fun updateAllData(
        nameTable: String,
        participantsList: List<AppGroupDataParticipant> = emptyList(),
        localTimesList: List<AppStPersonsLocalTime> = emptyList(),
        sinchesList: List<AppStPersonsSinch> = emptyList(),
        songsHistoryList: List<AppStSongsHistory> = emptyList(),
        songsPlansList: List<AppStSongsPlans> = emptyList()
    ) {
        _nameTable.value = nameTable
        _participants.value = participantsList
        _localTimes.value = localTimesList
        _sinches.value = sinchesList
        _songsHistory.value = songsHistoryList
        _songsPlans.value = songsPlansList
    }

    // Метод для добавления данных таблиц
    fun <T> addItem(item: T): Int {
        var newId: Int = 0
        if (item !is AppStSongsPlans && item !is AppStPersonsSinch) { newId = listener?.onDynamicDataChanged(item, _nameTable.value,0) ?: -1}
        if (newId >= 0) {
            dynamicDataRepository.addItem(item)
        }
        return newId
    }
    // Метод для синхронизации добавленного объекта ViewModel (нужен только для AppStPersonsSinch и AppStSongsPlans)
    fun <T> sinchItem(item: T): Int {
        val newId: Int = listener?.onDynamicDataChanged(item, _nameTable.value,0) ?: -1
        if (newId >= 0) {
            if (item is sinchMark) {
                item.sinch = true
            }
            dynamicDataRepository.updateItem(item)
        }
        return newId
    }


    // Метод для редактирования данных таблиц
    fun <T> updateItem(item: T) {
        val n: Int = listener?.onDynamicDataChanged(item,_nameTable.value, 1) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            dynamicDataRepository.updateItem(item)
        }
    }

    // Метод для удаления данных из таблиц
    fun <T> deleteItem(item: T) {
        val n: Int = listener?.onDynamicDataChanged(item, _nameTable.value,2) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            dynamicDataRepository.deleteItem(item)
        }
    }
}
