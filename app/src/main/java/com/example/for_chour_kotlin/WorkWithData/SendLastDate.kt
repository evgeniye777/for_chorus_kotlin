package com.example.for_chour_kotlin.WorkWithData

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.for_chour_kotlin.DataBases
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SendLastDate {
    val url = "https://chelny-dieta.ru/server.php" // URL вашего сервера
    private var basa: DataBases? = null
    var mdb: SQLiteDatabase? = null
    var context: Context? = null
    var activity: FragmentActivity? = null


    fun startSend( content: FragmentActivity,context0: Context) {
        context = context0
        basa = DataBases(content) //в первой БД хранятся данные которые я буду впоследствии обновлять, меняя версию БД
        try {
            basa!!.updateDataBase()
        } catch (mIOException: IOException) {
            vivodMes("basa")
            throw Error("UnableToUpdateDatabase")
        }
        try {
            mdb = basa!!.writableDatabase
        } catch (mSQLException: SQLException) {
            vivodMes("mdb")
            throw mSQLException
        }

        /// Запрос для получение последнего индекса
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response) // Создание JSONObject из строки
                    val lastId = jsonObject.getInt("last_id")
                    vivodMes(""+lastId) // Обработка успешного ответа
                    fetchLocalData(lastId) // Вызов метода для получения данных из локальной базы
                }catch (e:Exception) {vivodMes(e.toString())}
            },
            { error ->
                vivodMes("Ошибка Запроса для получение последнего индекса: $error")
                error.printStackTrace()
            }
        ) {
            // Переопределяем метод getParams для передачи параметров
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["LastIndex"] = "true" // Передаем параметр LastIndex
                return params
            }
        }


        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }


    @SuppressLint("Range")
    private fun fetchLocalData(lastId: Int) {
        val cursor = mdb?.rawQuery("SELECT * FROM st WHERE id > ?", arrayOf(lastId.toString()))

        val jsonArray = JSONArray()
        var writeR: Boolean = false
        if (cursor != null) {
            if (cursor.count > 0) {
                writeR = true
                if (cursor.moveToFirst()) {
                    do {
                        val jsonObject = JSONObject()
                        jsonObject.put("id", cursor.getInt(cursor.getColumnIndex("id")))
                        jsonObject.put(
                            "committer",
                            cursor.getString(cursor.getColumnIndex("committer"))
                        )
                        //jsonObject.put("shipped", cursor.getInt(cursor.getColumnIndex("shipped")))
                        jsonObject.put(
                            "date_write",
                            cursor.getString(cursor.getColumnIndex("date_write"))
                        )
                        jsonObject.put("date", cursor.getString(cursor.getColumnIndex("date")))
                        jsonObject.put("purpose", cursor.getInt(cursor.getColumnIndex("purpose")))
                        jsonObject.put("data", cursor.getString(cursor.getColumnIndex("data")))
                        jsonObject.put(
                            "comments",
                            cursor.getString(cursor.getColumnIndex("comments"))
                        )
                        for (i in 1..75) {
                            val columnName = "c$i"
                            val value = cursor.getString(cursor.getColumnIndex(columnName))
                            if (value != null && value.isNotEmpty()) {
                                jsonObject.put(columnName, value)
                            }
                        }

                        jsonArray.put(jsonObject)
                    } while (cursor.moveToNext())
                }
            }
            else {writeR = false}
            cursor.close()
        }

        if (writeR) {
            vivodMes(""+jsonArray)
            //sendDataToServer(jsonArray)
        }
        else {vivodMes("Данные уже синхронизированы")}
    }

    private fun sendDataToServer(jsonArray: JSONArray) {
        val jsonObject = JSONObject().apply {
            put("LastDate", true)
            put("data",jsonArray.toString() )
        }
        //Отправка новых данных
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            { response ->
                vivodMes(response.toString())
                Log.d("Response", response.toString())
            },
            { error ->
                vivodMes(error.toString())
                error.printStackTrace()
            }
        )
        // Добавьте запрос в очередь
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }

    fun vivodMes(text: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("info").setMessage(text)
        val dialog = builder.create()
        dialog.show()
    }
}