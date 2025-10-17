package com.example.for_chour_kotlin.data.source.url_responses

import android.os.Handler
import android.os.Looper
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ThreadURL {
    val localDataAY: LocalDataAY? = AuthorizationState.localDataAY
    fun start(postParams:String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        Thread {
            try {
                val url = URL("https://chelny-dieta.ru/googlenko/for_chorus/server.php")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

                    outputStream.use { os ->
                        OutputStreamWriter(os, Charsets.UTF_8).use { writer ->
                            writer.write(postParams)
                            writer.flush()
                        }
                    }

                    val responseCode = responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val response = inputStream.bufferedReader().use { it.readText() }
                        val handlerAY = HandlerAY(localDataAY)
                        AuthorizationState.mainActivity?.vivodMes(response)
                        handlerAY.handleServerResponse(response, onSuccess, onFailure)
                    } else {
                        runOnUiThread { onFailure() }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { onFailure() }
            }
        }.start()
    }

    private fun runOnUiThread(action: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.post { action() }
    }
}