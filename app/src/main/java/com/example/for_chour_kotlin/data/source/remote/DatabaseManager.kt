package com.example.for_chour_kotlin.data.source.remote
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.model.AppAllGroups
import com.example.for_chour_kotlin.data.model.AppAllPersons
import com.example.for_chour_kotlin.data.model.DataChangeListener
import com.example.for_chour_kotlin.data.model.Shared_View_Model
import com.example.for_chour_kotlin.data.model.Triger
import com.example.for_chour_kotlin.data.model.Type
import com.example.for_chour_kotlin.data.source.local.DataBases
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.sql.Types

class DatabaseManager(private val context: Context, private val viewModel: Shared_View_Model) :
    DataChangeListener {
    private val databaseHelper: DataBases = DataBases(context)
    private var database: SQLiteDatabase

    init {
        viewModel.listener = this
        updateDatabase()
        database = databaseHelper.writableDatabase
        loadData()
    }

    private fun updateDatabase() {
        try {
            databaseHelper.updateDataBase()
        } catch (e: IOException) {
            throw Error("Unable to update database")
        }
    }

    private fun loadData() {

    }

    //Локальная выгрузка всех групп
    @SuppressLint("Range")
    private fun loadGroups() {
        val cursor: Cursor = database.rawQuery("SELECT * FROM app_all_groups", null)
        val groups = mutableListOf<AppAllGroups>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hashName = cursor.getString(cursor.getColumnIndex("hash_name"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val location = cursor.getString(cursor.getColumnIndex("location"))
                val creator = cursor.getString(cursor.getColumnIndex("creator"))
                val bases = cursor.getString(cursor.getColumnIndex("bases"))

                groups.add(AppAllGroups(id, version, hashName, name, date, location, creator, jsonToList(bases)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setGroups(groups) // Предполагается, что у вас есть метод setGroups в вашем ViewModel
    }

    //Локальная выгрузка всех пользователей
    @SuppressLint("Range")
    private fun loadPersons() {
        val cursor: Cursor = database.rawQuery("SELECT * FROM app_all_persons", null)
        val persons = mutableListOf<AppAllPersons>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val version = cursor.getInt(cursor.getColumnIndex("version"))
                val hashName = cursor.getString(cursor.getColumnIndex("hash_name"))
                val login = cursor.getString(cursor.getColumnIndex("login"))
                val password = cursor.getString(cursor.getColumnIndex("password"))
                val hashPassword = cursor.getString(cursor.getColumnIndex("hash_password"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val vName = cursor.getInt(cursor.getColumnIndex("v_name"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val vEmail = cursor.getInt(cursor.getColumnIndex("v_email"))
                val dateReg = cursor.getString(cursor.getColumnIndex("date_reg"))
                val vDateReg = cursor.getInt(cursor.getColumnIndex("v_date_reg"))
                val dateLast = cursor.getString(cursor.getColumnIndex("date_last"))
                val vDateLast = cursor.getInt(cursor.getColumnIndex("v_date_last"))
                val birthDay = cursor.getString(cursor.getColumnIndex("birth_day"))
                val vBirthDay = cursor.getInt(cursor.getColumnIndex("v_birth_day"))
                val gender = cursor.getString(cursor.getColumnIndex("gender"))
                val vGender = cursor.getInt(cursor.getColumnIndex("v_gender"))
                val access = cursor.getString(cursor.getColumnIndex("access"))
                val groups = cursor.getString(cursor.getColumnIndex("groups"))

                persons.add(AppAllPersons(
                    id,
                    version,
                    hashName,
                    login,
                    password,
                    hashPassword,
                    name,
                    vName,
                    email,
                    vEmail,
                    dateReg,
                    vDateReg,
                    dateLast,
                    vDateLast,
                    birthDay,
                    vBirthDay,
                    gender,
                    vGender,
                    access,
                    jsonToList(groups) // Преобразуем строку JSON в List<String>
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setPersons(persons) // Предполагается, что у вас есть метод setPersons в вашем ViewModel
    }

    //Локальная выгрзука для всех тригеров
    @SuppressLint("Range")
    private fun loadTriggers() {
        val cursor: Cursor = database.rawQuery("SELECT * FROM app_triger", null)
        val triggers = mutableListOf<Triger>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val znak = cursor.getString(cursor.getColumnIndex("znak"))

                triggers.add(Triger(
                    id,
                    name,
                    znak
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setTriggers(triggers) // Предполагается, что у вас есть метод setTriggers в вашем ViewModel
    }

    //Локальная выгрузка всех типов данных
    @SuppressLint("Range")
    private fun loadTypes() {
        val cursor: Cursor = database.rawQuery("SELECT * FROM type", null)
        val types = mutableListOf<Type>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))

                // Инициализация списка значений
                val values = mutableListOf<String?>()
                for (i in 1..9) {
                    val columnName = "value$i" // Предполагается, что названия колонок - value1, value2, ..., value9
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    values.add(value) // Добавляем значение в список
                }

                // Создание объекта Type и добавление его в список
                types.add(Type(id, name, values))
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setTypes(types) // Предполагается, что у вас есть метод setTypes в вашем ViewModel
    }





    //Метод для преобразования строки из Json в List<String>
    private fun jsonToList(json: String): List<String> {
        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, listType)
    }

    override fun <T> onDataChanged(data: T, action: Int): Int {
        TODO("Not yet implemented")
    }
}