package com.example.for_chour_kotlin.data.typeData.appGroupDataParticipant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.AppDataListener
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModel_appDataParticipant : ViewModel(), DataOperations<AppGroupDataParticipant> {

    // Поля
    var listener: AppDataListener<AppGroupDataParticipant>? = null // Сделаем его публичным

    private val _groups = MutableLiveData<List<AppGroupDataParticipant>>()
    val groups: LiveData<List<AppGroupDataParticipant>> get() = _groups

    // Создаем экземпляр репозитория для таблиц
    private val repositoryAppOneGroup = Repository_appDataParticipant(_groups)

    // Метод для установки списка групп
    override fun setItems(itemList: List<AppGroupDataParticipant>) {
        repositoryAppOneGroup.setItems(itemList)
    }

    // Метод для добавления данных в таблицы
    override fun addItem(item: AppGroupDataParticipant): Int {
        val newId: Int = listener?.onAppDataChanged(item, 0) ?: -1
        if (newId >= 0) {
            repositoryAppOneGroup.addItem(item)
        }
        return newId
    }

    // Метод для редактирования данных в таблицах
    override fun updateItem(item: AppGroupDataParticipant) {
        val n: Int = listener?.onAppDataChanged(item, 1) ?: -1
        if (n > 0) { // Проверяем, что обновление прошло успешно
            repositoryAppOneGroup.updateItem(item)
        }
    }

    // Метод для удаления данных из таблиц
    override fun deleteItem(item: AppGroupDataParticipant) {
        val n: Int = listener?.onAppDataChanged(item, 2) ?: -1
        if (n > 0) { // Проверяем, что удаление прошло успешно
            repositoryAppOneGroup.deleteItem(item)
        }
    }
}
