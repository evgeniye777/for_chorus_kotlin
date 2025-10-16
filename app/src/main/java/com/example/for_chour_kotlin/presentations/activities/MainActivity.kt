package com.example.for_chour_kotlin.presentations.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.source.url_responses.TypeResponses
import com.example.for_chour_kotlin.data.typeData._cases.ConnectSQLite
import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData.appAllGroups.ViewModelAppAllGroups
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.ViewModelAppDataParticipant
import com.example.for_chour_kotlin.data.typeData.appStPersons.ViewModelAppStPersons
import com.example.for_chour_kotlin.databinding.ActivityMainBinding
import com.example.for_chour_kotlin.presentations._cases.ManagerFragments
import com.example.for_chour_kotlin.presentations._cases.ManagerNavView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private val groups: ViewModelAppAllGroups by viewModels()
    private val participant: ViewModelAppDataParticipant by viewModels()

    private val stPersons: ViewModelAppStPersons by viewModels()

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initUI()
        AuthorizationState.mainActivity = this
    }

    fun initUI() {
        AuthorizationState.clean()
        AccountHolder.clean()

        ConnectSQLite(this)

        val typeResponses = TypeResponses()
        AuthorizationState.typeResponses = typeResponses

        typeResponses.checkAuthorization(
            onSuccess = {
                vivodMes(AuthorizationState.dataAuthorization)
                val groupsList = groups.groups.value;
                if (groupsList!=null&& groupsList.isNotEmpty()) {
                    val versions: String = {
                        val map = groupsList.associate { it.hashName to it.version }
                        Gson().toJson(map)
                    }()
                    AuthorizationState.typeResponses?.uploadingUpdatesGroups(versions,{},{})
                }
                else {
                    AuthorizationState.typeResponses?.uploadingGroupData({vivodMes(AuthorizationState.dataAuthorization)},{vivodMes(AuthorizationState.dataAuthorization)})
                }
            },
            onFailure = {
                vivodMes(AuthorizationState.dataAuthorization)
            }
        )

        //Проверка актуальности логина и пароля
        //если всё ок

        groups.connection(AuthorizationState.database, "app_all_groups")

        AuthorizationState.groups = groups;
        AuthorizationState.participants = participant;

        ManagerNavView(binding, this,this)

        ManagerFragments(binding,this)
    }

    fun vivod(s: String?) {
        Toast.makeText(this, s?:"", Toast.LENGTH_SHORT).show()
    }

    fun vivodMes(s: String?) {
        MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
            .setTitle("AlertDialogExample")
            .setMessage(s ?: "")
            .setCancelable(false)
            .setPositiveButton("Proceed") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
//https\://services.gradle.org/distributions/gradle-8.12.1-bin.zip
//file\:///D:/MyFiles/Trainings/0_Gradle/gradle-8.12.1-bin.zip