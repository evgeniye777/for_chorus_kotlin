package com.example.for_chour_kotlin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.for_chour_kotlin.data.model.AppOneGroupModel
import com.example.for_chour_kotlin.data.model.AppAllPersons
import com.example.for_chour_kotlin.data.model.Triger
import com.example.for_chour_kotlin.data.model.Type

class StaticDataRepository(
    private var _groups: MutableLiveData<List<AppOneGroupModel>>,
    private var _persons: MutableLiveData<List<AppAllPersons>>,
    private var _trigers: MutableLiveData<List<Triger>>,
    private var _types: MutableLiveData<List<Type>>
) {

    // Метод для установки списка групп
    fun setGroups(groupList: List<AppOneGroupModel>) {
        _groups.value = groupList
    }

    // Метод для установки списка персон
    fun setPersons(personList: List<AppAllPersons>) {
        _persons.value = personList
    }

    // Метод для установки списка триггеров
    fun setTriggers(trigerList: List<Triger>) {
        _trigers.value = trigerList
    }

    // Метод для установки списка типов
    fun setTypes(typeList: List<Type>) {
        _types.value = typeList
    }

    // Метод для добавления данных статичных таблиц
    fun <T> addItem(item: T): Int {
        var newId: Int = -1 // Инициализируем id как -1
        when (item) {
            is AppOneGroupModel -> {
                val currentList = _groups.value ?: emptyList()
                newId = currentList.size + 1 // Генерируем новый id
                _groups.value = currentList + item.apply { id = newId }
            }
            is AppAllPersons -> {
                val currentList = _persons.value ?: emptyList()
                newId = currentList.size + 1 // Генерируем новый id
                _persons.value = currentList + item.apply { id = newId }
            }
            is Triger -> {
                val currentList = _trigers.value ?: emptyList()
                newId = currentList.size + 1 // Генерируем новый id
                _trigers.value = currentList + item.apply { id = newId }
            }
            is Type -> {
                val currentList = _types.value ?: emptyList()
                _types.value = currentList + item // Для Type id не устанавливаем
            }
            else -> {
                throw IllegalArgumentException("Unsupported type")
            }
        }
        return newId
    }

    // Метод для редактирования данных статичных таблиц
    fun <T> updateItem(item: T) {
        when (item) {
            is AppOneGroupModel -> {
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
                val index = currentList.indexOfFirst { it.name == item.name } // Уникальность по имени
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

    // Метод для удаления данных из статичных таблиц
    fun <T> deleteItem(item: T) {
        when (item) {
            is AppOneGroupModel -> {
                val currentList = _groups.value?.toMutableList() ?: mutableListOf()
                currentList.removeIf { it.id == item.id } // Удаляем элемент
                _groups.value = currentList
            }
            is AppAllPersons -> {
                val currentList = _persons.value?.toMutableList() ?: mutableListOf()
                currentList.removeIf { it.id == item.id } // Удаляем элемент
                _persons.value = currentList
            }
            is Triger -> {
                val currentList = _trigers.value?.toMutableList() ?: mutableListOf()
                currentList.removeIf { it.id == item.id } // Удаляем элемент
                _trigers.value = currentList
            }
            is Type -> {
                val currentList = _types.value?.toMutableList() ?: mutableListOf()
                currentList.removeIf { it.name == item.name } // Удаляем элемент по имени
                _types.value = currentList
            }
            else -> {
                throw IllegalArgumentException("Unsupported type")
            }
        }
    }
}
