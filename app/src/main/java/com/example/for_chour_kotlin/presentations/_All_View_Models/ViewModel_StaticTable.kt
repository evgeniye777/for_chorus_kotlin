package com.example.for_chour_kotlin.presentations._All_View_Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.for_chour_kotlin.data.model.AppOneGroupModel
import com.example.for_chour_kotlin.data.model.DataChangeListener
import com.example.for_chour_kotlin.data.model.Triger
import com.example.for_chour_kotlin.data.model.Type
import com.example.for_chour_kotlin.data.repository.StaticDataRepository
import com.example.for_chour_kotlin.data.typeData.OnePersons.AppAllPersons

class ViewModel_StaticTable : ViewModel() {

    //Поля
        var listener: DataChangeListener? = null // Сделаем его публичным

        //List для статичных таблиц
        private val _groups = MutableLiveData<List<AppOneGroupModel>>()
        val groups: LiveData<List<AppOneGroupModel>> get() = _groups

        private val _persons = MutableLiveData<List<AppAllPersons>>()
        val persons: LiveData<List<AppAllPersons>> get() = _persons

        private val _trigers = MutableLiveData<List<Triger>>()
        val trigers: LiveData<List<Triger>> get() = _trigers

        private val _types = MutableLiveData<List<Type>>()
        val types: LiveData<List<Type>> get() = _types

        // Создаем экземпляр репозитория для таблиц
        private val staticDataRepository = StaticDataRepository(_groups,_persons,_trigers,_types)

    //Операции с таблицами

        // Метод для установки списка групп
        fun setGroups(groupList: List<AppOneGroupModel>) {
            staticDataRepository.setGroups(groupList)
        }

        // Метод для установки списка персон
        fun setPersons(personList: List<AppAllPersons>) {
            staticDataRepository.setPersons(personList)
        }

        // Метод для установки списка триггеров
        fun setTriggers(trigerList: List<Triger>) {
            staticDataRepository.setTriggers(trigerList)
        }

        // Метод для установки списка типов
        fun setTypes(typeList: List<Type>) {
            staticDataRepository.setTypes(typeList)
        }

        //Метод для добавления данных таблиц
        fun <T> addItem(item: T): Int {
            var newId: Int = listener?.onStaticDataChanged(item, 0) ?: -1
            if (newId >= 0) {
                staticDataRepository.addItem(item)
            }
            return newId
        }

        //Метод для редактирования данных данных таблиц
        fun <T> updateItem(item: T) {
            val n: Int = listener?.onStaticDataChanged(item, 1) ?: -1
            if (n > 0) { // Проверяем, что обновление прошло успешно
                staticDataRepository.updateItem(item)
            }
        }

        //Метод для удаления данных из таблиц
        fun <T> deleteItem(item: T) {
            val n: Int = listener?.onStaticDataChanged(item, 2) ?: -1
            if (n > 0) { // Проверяем, что удаление прошло успешно
                staticDataRepository.deleteItem(item)
            }
        }
}