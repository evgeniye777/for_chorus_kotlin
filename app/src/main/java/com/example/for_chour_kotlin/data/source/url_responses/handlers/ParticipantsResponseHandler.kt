package com.example.for_chour_kotlin.data.source.url_responses.handlers

import android.util.Log
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant
import org.json.JSONObject

class ParticipantsResponseHandler {

        fun handle(jsonObject: JSONObject): Boolean {
            val success = jsonObject.optInt("success", -1)
            when (success) {
                0 -> {
                    Log.d("ParticipantsHandler", "Participants response failed (success=0)")
                    return false
                }
                1 -> {
                    val message = jsonObject.optString("message", "")
                    Log.d("ParticipantsHandler", "Participants response success (success=1): $message")
                    return true
                }
                2 -> {
                    val dataArray = jsonObject.optJSONArray("data")
                    if (dataArray == null || dataArray.length() == 0) {
                        Log.w("ParticipantsHandler", "No participants data in response")
                        return true
                    }

                    var allProcessedSuccessfully = true
                    val participantsViewModel = AuthorizationState.participants  // Доступ к глобальному ViewModel (аналогично groups)

                    for (i in 0 until dataArray.length()) {
                        val participantJson = dataArray.getJSONObject(i)
                        try {
                            val parsedParticipant = parseParticipantFromJson(participantJson)
                            val result = participantsViewModel?.addOrUpdateItem(parsedParticipant)
                            if (result != null) {
                                if (result < 0) {
                                    Log.e("ParticipantsHandler", "Failed to add/update participant with id=${parsedParticipant.id}, hashName=${parsedParticipant.hashName}")
                                    allProcessedSuccessfully = false
                                } else {
                                    Log.d("ParticipantsHandler", "Successfully added/updated participant with id=${parsedParticipant.id}, hashName=${parsedParticipant.hashName}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("ParticipantsHandler", "Error parsing participant at index $i: ${e.message}", e)
                            allProcessedSuccessfully = false
                        }
                    }

                    if (allProcessedSuccessfully) {
                        Log.d("ParticipantsHandler", "All participants processed successfully")
                        return true
                    } else {
                        Log.e("ParticipantsHandler", "Some participants failed to process")
                        return false
                    }
                }
                else -> {
                    Log.e("ParticipantsHandler", "Unknown success value: $success")
                    return false
                }
            }
        }

    private fun parseParticipantFromJson(participantJson: JSONObject): AppDataParticipant {
        // Безопасное парсинг числовых полей (аналогично parseGroupFromJson)
        val id = participantJson.optString("id", "0").toIntOrNull() ?: 0
        val version = participantJson.optString("version", "-1").toIntOrNull() ?: -1
        val idC = participantJson.optString("id_c", "")  // String, как в примере
        val hashName = participantJson.optString("hash_name", "")
        val date = participantJson.optString("date", null).takeIf { it != "null" }  // Nullable, обработка "null" как строки (как location в группах)
        val pName = participantJson.optString("p_name", null).takeIf { it != "null" && it.isNotBlank() }  // Nullable, если пустая — null
        val pGender = participantJson.optString("p_gender", "0").toIntOrNull() ?: 0
        val post = participantJson.optString("post", "0")  // Сначала как строка
        val allowed = participantJson.optString("allowed", "0").toIntOrNull() ?: 0
        val access = participantJson.optString("access", "0").toIntOrNull() ?: 0
        val visible = participantJson.optString("visible", "1").toIntOrNull() ?: 1

        return AppDataParticipant(
            id = id,
            idC = idC,
            version = version,
            hashName = hashName,
            date = date,
            pName = pName,
            pGender = pGender,
            post = JsonWork().jsonToList(post),
            allowed = allowed,
            access = access,
            visible = visible
        )
    }


}