package com.example.for_chour_kotlin.data.typeData.OnePersons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.typeData._interfaces.AppDataListener
import com.example.for_chour_kotlin.data.typeData._interfaces.DataOperations

class ViewModel_appAllPersons : ViewModel(), DataOperations<AppAllPersons> {

    //Поля
        var listener: AppDataListener<AppAllPersons>? = null // Сделаем его публичным

        private val _persons = MutableLiveData<List<AppAllPersons>>()
        val persons: LiveData<List<AppAllPersons>> get() = _persons

        // Создаем экземпляр репозитория для таблиц
        private val repositoryAppallpersons = Repository_appAllPersons(_persons)

        // Метод для установки списка персон
        override fun setItems(itemList: List<AppAllPersons>) {
            repositoryAppallpersons.setItems(itemList)
        }

        //Метод для добавления данных таблиц
        override fun addItem(item: AppAllPersons): Int {
            var newId: Int = listener?.onAppDataChanged(item, 0) ?: -1
            if (newId >= 0) {
                repositoryAppallpersons.addItem(item)
            }
            return newId
        }

        //Метод для редактирования данных данных таблиц
        override fun updateItem(item: AppAllPersons) {
            val n: Int = listener?.onAppDataChanged(item, 1) ?: -1
            if (n > 0) { // Проверяем, что обновление прошло успешно
                repositoryAppallpersons.updateItem(item)
            }
        }

        //Метод для удаления данных из таблиц
        override fun deleteItem(item: AppAllPersons) {
            val n: Int = listener?.onAppDataChanged(item, 2) ?: -1
            if (n > 0) { // Проверяем, что удаление прошло успешно
                repositoryAppallpersons.deleteItem(item)
            }
        }
}