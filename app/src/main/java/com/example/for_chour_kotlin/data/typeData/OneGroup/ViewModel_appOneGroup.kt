package com.example.for_chour_kotlin.data.typeData.OneGroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.AppDataListener
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModel_appOneGroup : ViewModel(), DataOperations<AppOneGroup> {

    // Поля
    var listener: AppDataListener<AppOneGroup>? = null // Сделаем его публичным

    private val _groups = MutableLiveData<List<AppOneGroup>>()
    val groups: LiveData<List<AppOneGroup>> get() = _groups

    // Создаем экземпляр репозитория для таблиц
    private val repositoryAppOneGroup = Repository_appOneGroup(_groups)

    // Метод для установки списка групп
    override fun setItems(itemList: List<AppOneGroup>) {
        repositoryAppOneGroup.setItems(itemList)
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppOneGroup): Int {
        val newId: Int = listener?.onAppDataChanged(item, 0) ?: -1
        if (newId >= 0) {
            repositoryAppOneGroup.addItem(item)
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppOneGroup) {
        val n: Int = listener?.onAppDataChanged(item, 1) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppOneGroup.updateItem(item)
        }
    }

    // Метод для удаления данных из таблиц
    override fun deleteItem(item: AppOneGroup) {
        val n: Int = listener?.onAppDataChanged(item, 2) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppOneGroup.deleteItem(item)
        }
    }
}
