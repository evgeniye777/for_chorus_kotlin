package com.example.for_chour_kotlin.presentations._cases

import android.content.Context
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.databinding.MenuItemBottomBinding

class MenuLoader {
    companion object {
        fun loadMenuContent(menuBottomContent: Array<MenuItemBottomBinding>, context: Context) {
            val images = arrayOf(
                R.drawable.img_sours,
                R.drawable.img_heart,
                R.drawable.img_responses,
                R.drawable.img_message,
                R.drawable.img_profile
            )

            val texts = arrayOf(
                "Данные",
                "Посещаемость",
                "Планы",
                "История",
                "Настройки"
            )

            for (i in menuBottomContent.indices) {
                menuBottomContent[i].imgContent.setImageResource(images[i])
                menuBottomContent[i].textContent.text = texts[i]
            }
        }
    }
}
