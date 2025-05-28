package com.example.for_chour_kotlin.data.typeData.appGroupDataParticipant

import androidx.lifecycle.MutableLiveData
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class Repository_appDataParticipant(private var _groups: MutableLiveData<List<AppGroupDataParticipant>>) :
    DataOperations<AppGroupDataParticipant> {

    // Метод для установки списка групп
    override fun setItems(itemList: List<AppGroupDataParticipant>) {
        _groups.value = itemList
    }

    // Метод для добавления данных в таблицы для AppOneGroup
    override fun addItem(item: AppGroupDataParticipant): Int {
        var newId: Int = -1 // Инициализируем id как -1
        val currentList = _groups.value ?: emptyList() // Получаем текущий список групп
        newId = currentList.size + 1 // Генерируем новый id
        _groups.value = currentList + item.apply { id = newId } // Добавляем группу с новым id
        return newId // Возвращаем новый id
    }

    // Метод для редактирования данных в таблицах для AppOneGroup
    override fun updateItem(item: AppGroupDataParticipant) {
        val currentList = _groups.value?.toMutableList() ?: mutableListOf() // Получаем текущий список групп
        val index = currentList.indexOfFirst { it.id == item.id } // Находим индекс элемента по ID
        if (index != -1) {
            currentList[index] = item // Обновляем элемент
            _groups.value = currentList // Обновляем список
        }
    }

    // Метод для удаления данных из таблиц для AppOneGroup
    override fun deleteItem(item: AppGroupDataParticipant) {
        val currentList = _groups.value?.toMutableList() ?: mutableListOf() // Получаем текущий список групп
        currentList.removeIf { it.id == item.id } // Удаляем элемент по ID
        _groups.value = currentList // Обновляем список
    }
}
