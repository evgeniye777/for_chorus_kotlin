package com.example.for_chour_kotlin.presentations._cases

import android.content.Context
import android.content.res.ColorStateList
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.databinding.ActivityMainBinding
import com.example.for_chour_kotlin.databinding.MenuItemBottomBinding
import com.example.for_chour_kotlin.presentations.fragments.attendance.AttendanceFragment
import com.example.for_chour_kotlin.presentations.fragments.group_details.GroupDetailsFragment
import com.example.for_chour_kotlin.presentations.fragments.web_fragments.WebViewFragment
import com.google.android.material.navigation.NavigationView

class ManagerFragments(val binding: ActivityMainBinding,val context: Context) {
    private lateinit var menuBottomContent: Array<MenuItemBottomBinding>

    init {
        initialization()
        //Заполнение элементов нижнего меню ресурсами
        MenuLoader.loadMenuContent(menuBottomContent, context)
    }

    private fun initialization() {
        val menuComponents = binding.idMenuPackage
        menuBottomContent = arrayOf(menuComponents.menuSours,menuComponents.menuLike,menuComponents.menuResponses,menuComponents.menuMessage,menuComponents.menuProfile)
        changeColorTextImage(0)

        val myLinear_menu_sours: LinearLayout = menuBottomContent[0].menuLinear
        myLinear_menu_sours.setOnClickListener {
            changeColorTextImage(0)
            replaceFragment(GroupDetailsFragment(), "ATTENDANCE_FRAGMENT")
        }
        val myLinear_menu_like: LinearLayout = menuBottomContent[1].menuLinear
        myLinear_menu_like.setOnClickListener {
            changeColorTextImage(1)
            replaceFragment(AttendanceFragment(), "LIKE_FRAGMENT")
        }
        val myLinear_menu_responses: LinearLayout = menuBottomContent[2].menuLinear
        myLinear_menu_responses.setOnClickListener {
            changeColorTextImage(2)
            replaceFragment(WebViewFragment(), "RESPONSES_FRAGMENT")
        }
        val myLinear_menu_message: LinearLayout = menuBottomContent[3].menuLinear
        myLinear_menu_message.setOnClickListener {
            changeColorTextImage(3)
            //заглушка
            //replaceFragment(MessageFragment(), "MESSAGE_FRAGMENT")
        }
        val myLinear_menu_profile: LinearLayout = menuBottomContent[4].menuLinear
        myLinear_menu_profile.setOnClickListener {
            changeColorTextImage(4)
            //заглушка
            //replaceFragment(ProfileFragment(), "PROFILE_FRAGMENT")
        }
    }


    private fun changeColorTextImage(n: Int) {
        val colorClick = ContextCompat.getColor(context, R.color.menu_click)
        val colorNotClick = ContextCompat.getColor(context, R.color.menu_not_click)
        for (i: Int in 0..menuBottomContent.size-1) {
            if (i==n) {
                menuBottomContent[i].imgContent.imageTintList = ColorStateList.valueOf(colorClick)
                menuBottomContent[i].textContent.setTextColor(colorClick);
            }
            else {
                menuBottomContent[i].imgContent.imageTintList = ColorStateList.valueOf(colorNotClick)
                menuBottomContent[i].textContent.setTextColor(colorNotClick);
            }
        }
    }
    private fun replaceFragment(fragment: Fragment, tag: String) {
        val activity = context as? AppCompatActivity
            ?: throw IllegalStateException("Context is not an AppCompatActivity")

        val fragmentManager = activity.supportFragmentManager

        val existingFragment = fragmentManager.findFragmentByTag(tag)

        fragmentManager.beginTransaction().apply {
            if (existingFragment == null) {
                add(R.id.fragment_container, fragment, tag)
            } else {
                show(existingFragment)
            }

            fragmentManager.fragments.forEach { f ->
                if (f != existingFragment) {
                    hide(f)
                }
            }
            commit()
        }
    }


    fun vivod(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}