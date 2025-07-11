package com.example.for_chour_kotlin.presentations.activities

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.databinding.ActivityMainBinding
import com.example.for_chour_kotlin.presentations.fragments.attendance.AttendanceFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageViews: Array<ImageView>
    private lateinit var textViews: Array<TextView>
    private lateinit var dimView: View

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(AttendanceFragment(), "SOURS_FRAGMENT")
        }
        val imgSours: ImageView = findViewById(R.id.img_sours)
        imageViews = arrayOf(imgSours)

        val textSours: TextView = findViewById(R.id.text_sours)
        textViews = arrayOf(textSours)
        changeColorTextImage(0)

        val myLinear_menu_sours: LinearLayout = findViewById(R.id.menu_sours)
        myLinear_menu_sours.setOnClickListener {
            changeColorTextImage(0)
            replaceFragment(AttendanceFragment(), "TABEL_FRAGMENT")
        }
        val myLinear_menu_like: LinearLayout = findViewById(R.id.menu_like)
        myLinear_menu_like.setOnClickListener {
            //changeColorTextImage(1)
            //replaceFragment(LikeFragment(), "LIKE_FRAGMENT")
        }
        val myLinear_menu_responses: LinearLayout = findViewById(R.id.menu_responses)
        myLinear_menu_responses.setOnClickListener {
            //changeColorTextImage(2)
            //заглушка
            //replaceFragment(ResponsesFragment(), "RESPONSES_FRAGMENT")
        }
        val myLinear_menu_message: LinearLayout = findViewById(R.id.menu_message)
        myLinear_menu_message.setOnClickListener {
            //changeColorTextImage(3)
            //заглушка
            //replaceFragment(MessageFragment(), "MESSAGE_FRAGMENT")
        }
        val myLinear_menu_profile: LinearLayout = findViewById(R.id.menu_profile)
        myLinear_menu_profile.setOnClickListener {
            //changeColorTextImage(4)
            //заглушка
            //replaceFragment(ProfileFragment(), "PROFILE_FRAGMENT")
        }
    }

    private fun changeColorTextImage(n: Int) {
        for (i: Int in 0..imageViews.size-1) {
            if (i==n) {
                imageViews.get(i).imageTintList = ColorStateList.valueOf(getColor(R.color.menu_click))
                textViews.get(i).setTextColor(ContextCompat.getColor(this, R.color.menu_click));
            }
            else {
                imageViews.get(i).imageTintList = ColorStateList.valueOf(getColor(R.color.menu_not_click))
                textViews.get(i).setTextColor(ContextCompat.getColor(this, R.color.menu_not_click));
            }
        }
    }
    private fun replaceFragment(fragment: Fragment, tag: String) {
        // Проверяем, существует ли фрагмент
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.beginTransaction().apply {
            if (existingFragment == null) {
                // Фрагмент еще не создан, создаем новый
                add(R.id.fragment_container, fragment, tag)
            } else {
                // Фрагмент уже существует, просто показываем его
                show(existingFragment)
            }
            // Скрываем все остальные фрагменты
            supportFragmentManager.fragments.forEach { f ->
                if (f != existingFragment) {
                    hide(f)
                }
            }

            commit()
        }
    }

    fun vivod(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
//https\://services.gradle.org/distributions/gradle-8.12.1-bin.zip
//file\:///D:/MyFiles/Trainings/0_Gradle/gradle-8.12.1-bin.zip