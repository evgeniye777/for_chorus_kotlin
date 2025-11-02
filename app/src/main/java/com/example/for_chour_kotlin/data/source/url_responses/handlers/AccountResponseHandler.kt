package com.example.for_chour_kotlin.data.source.url_responses.handlers
import org.json.JSONObject
import android.util.Log
import com.example.for_chour_kotlin.data.source.url_responses.AccountData
import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.LocalDataAY

class AccountResponseHandler(
    private val localDataAY: LocalDataAY?
) {
    fun handle(jsonObject: JSONObject): Boolean {
        val success = jsonObject.optInt("success", -1)
        when (success) {
            0 -> {
                Log.d("AccountHandler", "Account response failed (success=0)")
                return false
            }
            1 -> {
                Log.d("AccountHandler", "Account response success (success=1)")
                return true
            }
            2 -> {
                // Обновление данных (точно как в вашей функции)
                val data = jsonObject.optJSONObject("data")
                if (data != null) {
                    val newAccountData = parseAccountDataFromJson(data) // Ваша существующая функция парсинга
                    if (localDataAY?.saveAccountData(newAccountData) == true) {
                        // Обновляем синглтон (точно как в вашей функции)
                        AccountHolder.apply {
                            version = newAccountData.version
                            hashName = newAccountData.hashName
                            login = newAccountData.login
                            name = newAccountData.name
                            email = newAccountData.email
                            dateReg = newAccountData.dateReg
                            dateLast = newAccountData.dateLast
                            birthDay = newAccountData.birthDay
                            gender = newAccountData.gender
                            access = newAccountData.access
                            groups = newAccountData.groups
                        }
                        Log.d("AccountHandler", "Account data saved and singleton updated")
                        return true
                    } else {
                        Log.e("AccountHandler", "Failed to save account data locally")
                        return false
                    }
                } else {
                    Log.e("AccountHandler", "No data in account response")
                    return false
                }
            }
            else -> {
                Log.e("AccountHandler", "Unknown success value: $success")
                return false
            }
        }
    }


    private fun parseAccountDataFromJson(json: JSONObject): AccountData {
        return AccountData(
            version = json.optString("version", null),
            hashName = json.optString("hash_name", ""),
            login = json.optString("login", ""),
            name = json.optString("name", null),
            email = json.optString("email", null),
            dateReg = json.optString("date_reg", null),
            dateLast = json.optString("date_last", null),
            birthDay = json.optString("birth_day", null),
            gender = json.optString("gender", null),
            access = json.optString("access", null),
            groups = if (json.isNull("groups")) null else json.optString("groups", null),
            hashPassword = AccountHolder.hashPassword
        )
    }
}
