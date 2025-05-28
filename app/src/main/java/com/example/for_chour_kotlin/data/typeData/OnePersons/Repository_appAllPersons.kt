package com.example.for_chour_kotlin.data.typeData.OnePersons

import androidx.lifecycle.MutableLiveData
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class Repository_appAllPersons(private var _persons: MutableLiveData<List<AppAllPersons>>):
    DataOperations<AppAllPersons> {

    // Метод для установки списка персон
    override fun setItems(itemList: List<AppAllPersons>) {
        _persons.value = itemList
    }

    // Метод для добавления данных статичных таблиц для AppAllPersons
    override fun addItem(item: AppAllPersons): Int {
        var newId: Int = -1 // Инициализируем id как -1
        val currentList = _persons.value ?: emptyList() // Получаем текущий список пользователей
        newId = currentList.size + 1 // Генерируем новый id
        _persons.value = currentList + item.apply { id = newId } // Добавляем пользователя с новым id
        return newId // Возвращаем новый id
    }

    // Метод для редактирования данных статичных таблиц для AppAllPersons
    override fun updateItem(item: AppAllPersons) {
        val currentList = _persons.value?.toMutableList() ?: mutableListOf() // Получаем текущий список пользователей
        val index = currentList.indexOfFirst { it.id == item.id } // Находим индекс элемента по ID
        if (index != -1) {
            currentList[index] = item // Обновляем элемент
            _persons.value = currentList // Обновляем список
        }
    }

    // Метод для удаления данных из статичных таблиц для AppAllPersons
    override fun deleteItem(item: AppAllPersons) {
        val currentList = _persons.value?.toMutableList() ?: mutableListOf() // Получаем текущий список пользователей
        currentList.removeIf { it.id == item.id } // Удаляем элемент по ID
        _persons.value = currentList // Обновляем список
    }
}