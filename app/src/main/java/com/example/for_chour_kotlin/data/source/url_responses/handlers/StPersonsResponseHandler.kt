package com.example.for_chour_kotlin.data.source.url_responses.handlers

import com.example.for_chour_kotlin.data.typeData.appStPersons.AppStPersons
import android.util.Log
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData._cases.JsonWork
import org.json.JSONObject

class StPersonsResponseHandler {

    fun handle(jsonObject: JSONObject): Boolean {
        val success = jsonObject.optInt("success", -1)
        when (success) {
            0 -> {
                Log.d("StPersonsHandler", "StPersons response failed (success=0)")
                return false
            }
            1 -> {
                val message = jsonObject.optString("message", "")
                Log.d("StPersonsHandler", "StPersons response success (success=1): $message")
                return true
            }
            2 -> {
                val dataArray = jsonObject.optJSONArray("data")
                if (dataArray == null || dataArray.length() == 0) {
                    Log.w("StPersonsHandler", "No StPersons data in response")
                    return true
                }

                var allProcessedSuccessfully = true
                val stPersonsViewModel = AuthorizationState.stPersons

                for (i in 0 until dataArray.length()) {
                    val stPersonsJson = dataArray.getJSONObject(i)
                    try {
                        val parsedStPersons = parseStPersonsFromJson(stPersonsJson)
                        val result = stPersonsViewModel?.addOrUpdateItem(parsedStPersons)
                        if (result != null) {
                            if (result < 0) {
                                Log.e("StPersonsHandler", "Failed to add/update StPersons with id=${parsedStPersons.id}, dateWrite=${parsedStPersons.dateWrite}")
                                allProcessedSuccessfully = false
                            } else {
                                Log.d("StPersonsHandler", "Successfully added/updated StPersons with id=${parsedStPersons.id}, dateWrite=${parsedStPersons.dateWrite}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("StPersonsHandler", "Error parsing StPersons at index $i: ${e.message}", e)
                        allProcessedSuccessfully = false
                    }
                }

                if (allProcessedSuccessfully) {
                    Log.d("StPersonsHandler", "All StPersons processed successfully")
                    return true
                } else {
                    Log.e("StPersonsHandler", "Some StPersons failed to process")
                    return false
                }
            }
            else -> {
                Log.e("StPersonsHandler", "Unknown success value: $success")
                return false
            }
        }
    }

    private fun parseStPersonsFromJson(stPersonsJson: JSONObject): AppStPersons {
        val id = stPersonsJson.optString("id", "0").toIntOrNull() ?: 0
        val committer = stPersonsJson.optString("committer", null)
        val dateWrite = stPersonsJson.optString("date_write", "")
        val date = stPersonsJson.optString("date", "")
        val purpose = stPersonsJson.optString("purpose", "0").toIntOrNull() ?: 0
        val dataField = stPersonsJson.optString("data", null)
        val comments = stPersonsJson.optString("comments", null)

        val cJson = stPersonsJson.optJSONObject("c")
        val cMap: HashMap<String, String> = if (cJson != null) {
            JsonWork().jsonToListH(cJson.toString())
        } else {
            HashMap()
        }

        return AppStPersons(
            id = id,
            committer = committer,
            dateWrite = dateWrite,
            date = date,
            purpose = purpose,
            data = dataField,
            comments = comments,
            c = cMap,  // HashMap
            sinch = 2
        )
    }
}
