package com.example.for_chour_kotlin.presentations._cases

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.databinding.ActivityMainBinding
import com.example.for_chour_kotlin.databinding.MenuItemBottomBinding
import com.example.for_chour_kotlin.presentations.fragments.attendance.AttendanceFragment
import com.example.for_chour_kotlin.presentations.fragments.group_details.GroupDetailsFragment
import com.example.for_chour_kotlin.presentations.fragments.web_fragments.WebViewFragment
import com.example.the_planner_semen.my_menu.FragmentMenu

class ManagerFragments(val binding: ActivityMainBinding,val context: Context, val fragmentManager: FragmentManager) {
    private lateinit var menuBottomContent: Array<MenuItemBottomBinding>

    private var fragmentMenu: FragmentMenu? = null

    private lateinit var image_my_menu: ImageView

    private lateinit var appBarPanel: LinearLayout

    private var statusMenu = false


    init {
        initialization()
        //Заполнение элементов нижнего меню ресурсами
        MenuLoader.loadMenuContent(menuBottomContent, context)

        appBarPanel = binding.idAppBarMain.layoutAppBarPanel
        appBarPanel.setOnClickListener {
            if (statusMenu) {
                fragmentMenu?.onHideFragment()
            }
        }
        //инициализация кнопки открытия меню
        image_my_menu = binding.idAppBarMain.idIconMyMenu
        image_my_menu.setOnClickListener{ view ->
            if (fragmentMenu!=null) {
                loadFragmentMenu(fragmentMenu!!)
                statusMenu = !statusMenu
            }
        }


    }

    private fun initialization() {
        val menuComponents = binding.idMenuPackage
        menuBottomContent = arrayOf(menuComponents.menuSours,menuComponents.menuLike,menuComponents.menuResponses,menuComponents.menuMessage,menuComponents.menuProfile)
        changeColorTextImage(0)

        val myLinear_menu_sours: LinearLayout = menuBottomContent[0].menuLinear
        myLinear_menu_sours.setOnClickListener {
            AuthorizationState.stateIn = 1
            changeColorTextImage(0)
            replaceFragment(GroupDetailsFragment(), "ATTENDANCE_FRAGMENT")
        }
        val myLinear_menu_like: LinearLayout = menuBottomContent[1].menuLinear
        myLinear_menu_like.setOnClickListener {
            AuthorizationState.stateIn = 2
            changeColorTextImage(1)
            val fragment = AttendanceFragment()
            statusMenu = false
            fragmentMenu = fragment.menuFragment
            replaceFragment(fragment, "LIKE_FRAGMENT")
        }
        val myLinear_menu_responses: LinearLayout = menuBottomContent[2].menuLinear
        myLinear_menu_responses.setOnClickListener {
            AuthorizationState.stateIn = 3
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
    @SuppressLint("CommitTransaction")
    private fun loadFragmentMenu(fragment: Fragment) {
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        // Проверяем, существует ли фрагмент
        var existingFragment = fragmentManager.findFragmentByTag(fragment.javaClass.simpleName)

        if (existingFragment != null) {
            if (statusMenu) { fragmentTransaction.hide(existingFragment) }
            else {fragmentTransaction.show(existingFragment)}
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }

    fun vivod(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}