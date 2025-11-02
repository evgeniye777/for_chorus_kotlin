package com.example.for_chour_kotlin.data.source.url_responses

import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import android.util.Log
import com.example.for_chour_kotlin.data.source.url_responses.handlers.AccountResponseHandler
import com.example.for_chour_kotlin.data.source.url_responses.handlers.GroupResponseHandler
import com.example.for_chour_kotlin.data.source.url_responses.handlers.ParticipantsResponseHandler
import com.example.for_chour_kotlin.data.source.url_responses.handlers.StPersonsResponseHandler

class HandlerAY(val localDataAY: LocalDataAY?) {


    fun handleServerResponse(response: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        try {
            val jsonArray = JSONArray(response)
            if (jsonArray.length() == 0) {
                Log.e("ServerResponse", "Empty response array")
                runOnUiThread { onFailure() }
                return
            }

            // Создаем обработчики (передаем localDataAY только в AccountHandler)
            val accountHandler = AccountResponseHandler(localDataAY)
            val groupHandler = GroupResponseHandler()
            val participantHandler = ParticipantsResponseHandler()
            val stPersonsHandler = StPersonsResponseHandler()

            var allSuccess = true // Флаг для проверки, что все части обработаны успешно
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val typedata = jsonObject.optString("typedata", "")
                when (typedata) {
                    "inAccount" -> {
                        if (!accountHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "checkUser" -> {
                        if (!accountHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "uploadingGroupData" -> {
                        if (!groupHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "uploadingUpdatesGroups" -> {
                        if (!groupHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "uploadingParticipantsData" -> {
                        if (!participantHandler.handle(jsonObject)) {
                            allSuccess = false
                        }

                    }
                    "uploadingUpdatesParticipants" -> {
                        if (!participantHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "uploadingStPersonsSinch" -> {
                        if (!stPersonsHandler.handle(jsonObject)) {
                            allSuccess = false
                        }

                    }
                    "uploadingUpdatesStPersonsSinch" -> {
                        if (!stPersonsHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "addRecordStPersonsSinch" -> {
                        if (!stPersonsHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    "changePersonForGroup" -> {
                        if (!stPersonsHandler.handle(jsonObject)) {
                            allSuccess = false
                        }
                    }
                    else -> {
                        Log.w("ServerResponse", "Unknown typedata: $typedata")
                        allSuccess = false
                    }
                }

            }

            Log.d("ServerResponse", "=== Loop complete, allSuccess=$allSuccess ===")
            // UI-вызовы ТОЛЬКО ЗДЕСЬ, на main thread
            Handler(Looper.getMainLooper()).post {
                if (allSuccess) {
                    Log.d("ServerResponse", "Calling onSuccess on main thread")
                    onSuccess() // Здесь recreate() или navigation — теперь на main thread
                } else {
                    Log.d("ServerResponse", "Calling onFailure on main thread")
                    onFailure()
                }
            }

        } catch (e: Exception) {
            Log.e("ServerResponse", "Error parsing response: ${e.message}")
            e.printStackTrace()
            runOnUiThread { onFailure() }
        }
    }




    private fun runOnUiThread(action: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.post { action() }
    }
}