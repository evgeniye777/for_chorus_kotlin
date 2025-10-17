package com.example.for_chour_kotlin.data.typeData.appStPersons

import androidx.lifecycle.MutableLiveData
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class RepositoryAppStPersons(private var _groups: MutableLiveData<List<AppStPersons>?>) :
    DataOperations<AppStPersons> {

    // Метод для установки списка
    override fun setItems(list: MutableList<AppStPersons>?) {
        _groups.value = list
    }

    // Метод для добавления данных в таблицы 
    override fun addItem(item: AppStPersons): Int {
        var newId: Int = -1
        val currentList = _groups.value ?: emptyList()
        newId = currentList.size + 1
        _groups.postValue(currentList + item)
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStPersons): Int {
        val currentList = _groups.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList[index] = item
            _groups.value = currentList
        }
        return index
    }

    // Метод для удаления данных из таблиц
    override fun deleteItem(item: AppStPersons): Int {
        val currentList = _groups.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        currentList.removeIf { it.id == index }
        _groups.value = currentList
        return index
    }
}