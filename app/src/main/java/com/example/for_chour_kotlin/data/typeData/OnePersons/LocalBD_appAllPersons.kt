package com.example.for_chour_kotlin.data.typeData.OnePersons
import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData._interfaces.AppDataListener
import com.example.for_chour_kotlin.data.typeData._interfaces.LocalBdListener

class LocalBD_appAllPersons(private val viewModel: ViewModel_appAllPersons, private var database: SQLiteDatabase) :
    AppDataListener<AppAllPersons>, LocalBdListener<AppAllPersons> {
        val jsW: JsonWork = JsonWork()
    init {
        viewModel.listener = this
    }

    //Локальная выгрузка всех пользователей
    @SuppressLint("Range")
    override fun readItems(nameTable: String) {
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

                persons.add(
                    AppAllPersons(
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
                        jsW.jsonToList(groups) // Преобразуем строку JSON в List<String>
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        viewModel.setItems(persons) // Предполагается, что у вас есть метод setPersons в вашем ViewModel
    }

    // Добавление нового пользователя в таблицу AppAllPersons
    override fun addItem(item: AppAllPersons): Int {
        val values = ContentValues().apply {
            put("hashName", item.hashName)
            put("login", item.login)
            put("password", item.password)
            put("hashPassword", item.hashPassword)
            put("name", item.name)
            put("vName", item.vName)
            put("email", item.email)
            put("vEmail", item.vEmail)
            put("dateReg", item.dateReg)
            put("vDateReg", item.vDateReg)
            put("dateLast", item.dateLast)
            put("vDateLast", item.vDateLast)
            put("birthDay", item.birthDay)
            put("vBirthDay", item.vBirthDay)
            put("gender", item.gender)
            put("vGender", item.vGender)
            put("access", item.access)
            // Преобразуем список групп в строку, если необходимо
            put("groups",jsW.listToJson(item.groups))
        }
        return database.insert("app_all_persons", null, values).toInt() // Возвращаем новый ID
    }

    // Обновление данных пользователя в таблице AppAllPersons
    override fun updateItem(item: AppAllPersons) {
        val values = ContentValues().apply {
            put("hashName", item.hashName)
            put("login", item.login)
            put("password", item.password)
            put("hashPassword", item.hashPassword)
            put("name", item.name)
            put("vName", item.vName)
            put("email", item.email)
            put("vEmail", item.vEmail)
            put("dateReg", item.dateReg)
            put("vDateReg", item.vDateReg)
            put("dateLast", item.dateLast)
            put("vDateLast", item.vDateLast)
            put("birthDay", item.birthDay)
            put("vBirthDay", item.vBirthDay)
            put("gender", item.gender)
            put("vGender", item.vGender)
            put("access", item.access)
            // Преобразуем список групп в строку, если необходимо
            put("groups", jsW.listToJson(item.groups))
        }
        database.update(
            "app_all_persons",
            values,
            "id=?",
            arrayOf(item.id.toString())
        ) // Обновляем запись по ID
    }

    // Удаление пользователя из таблицы AppAllPersons
    override fun deleteItem(item: AppAllPersons) {
        database.delete("app_all_persons", "id=?", arrayOf(item.id.toString()))
    }

    override fun onAppDataChanged(
        data: AppAllPersons,
        action: Int
    ): Int {
        var new_id: Int = -1
        when (action) {
            0 -> new_id = addItem(data) // Добавить
            1 -> updateItem(data) // Изменить
            2 -> deleteItem(data) // Удалить
        }
        return new_id
    }
}