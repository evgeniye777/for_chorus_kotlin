package com.example.for_chour_kotlin.presentations._cases

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData._cases.HashUtils
import com.example.for_chour_kotlin.data.typeData.appAllGroups.AppAllGroups
import com.example.for_chour_kotlin.databinding.ActivityMainBinding
import com.example.for_chour_kotlin.databinding.NavHeaderMainBinding
import com.example.for_chour_kotlin.presentations.adapters.GroupNameAdapter
import com.example.for_chour_kotlin.presentations.dialogs.DialogFragmentIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView

class ManagerNavView(val binding: ActivityMainBinding,val context: Context,val lifecycleOwner: LifecycleOwner) {
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    init {
        initialization()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initialization() {
        navView = binding.navView
        drawerLayout = binding.mainContainer

        //Активация кнопки открытия и закрытия шторки
        val openDrawerImageView: ImageView = binding.idAppBarMain.idOpenDrawer
        val navViewLayout = NavHeaderMainBinding.bind(navView.getHeaderView(0))

        val loginLabel = navViewLayout.idLoginLabel
        loginLabel.text = AccountHolder.login

        val buttonExit = navViewLayout.exit
        buttonExit.setOnClickListener {
            AuthorizationState.localDataAY?.clearAccountTable()
            AccountHolder.clean()
            AuthorizationState.groups?.hideData()
            AuthorizationState.participants?.hideData()
            loginLabel.text = ""
        }

        val buttonIn = navViewLayout.authorization
        buttonIn.setOnClickListener {
            val dialogHelper = DialogFragmentIn(context)
            dialogHelper.showDialog { login, password ->
                // Обработка введённых логина и пароля
                AuthorizationState.typeResponses?.inAccount(login, HashUtils.sha256(password),
                    {
                        loginLabel.text = AccountHolder.login
                        AuthorizationState.groups?.connection(AuthorizationState.database,"app_all_groups")
                        },
                    {})
            }

        }

        openDrawerImageView.setOnClickListener { view ->
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val recyclerView = navViewLayout.idGroupsListName
        val adapter = GroupNameAdapter(AuthorizationState.groups?.groups?.value as MutableList<AppAllGroups>?)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.chooseFirst()
        AuthorizationState.groups?.groups?.observe(lifecycleOwner, Observer { groups ->
            if (groups!=null&& groups.isNotEmpty()) {
                adapter.updateGroups(groups as MutableList<AppAllGroups>?)}
            else {
                adapter.updateGroups(mutableListOf())
            }
        })


    }

    fun vivodMes(s: String?) {
        MaterialAlertDialogBuilder(context, R.style.MyAlertDialogTheme)
            .setTitle("AlertDialogExample")
            .setMessage(s ?: "")
            .setCancelable(false)
            .setPositiveButton("Proceed") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

}