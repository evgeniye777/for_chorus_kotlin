package com.example.for_chour_kotlin.data.typeData.appStSongsPlans

import androidx.lifecycle.MutableLiveData
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class RepositoryAppStSongsPlans(private var _groups: MutableLiveData<List<AppStSongsPlans>?>) :
    DataOperations<AppStSongsPlans> {

    // Метод для установки списка
    override fun setItems(list: MutableList<AppStSongsPlans>?) {
        _groups.value = list
    }

    // Метод для добавления данных в таблицы 
    override fun addItem(item: AppStSongsPlans): Int {
        var newId: Int = -1
        val currentList = _groups.value ?: emptyList()
        newId = currentList.size + 1
        _groups.value = currentList + item
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppStSongsPlans): Int {
        val currentList = _groups.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList[index] = item
            _groups.value = currentList
        }
        return index
    }

    // Метод для удаления данных из таблиц
    override fun deleteItem(item: AppStSongsPlans): Int {
        val currentList = _groups.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        currentList.removeIf { it.id == index }
        _groups.value = currentList
        return index
    }
}