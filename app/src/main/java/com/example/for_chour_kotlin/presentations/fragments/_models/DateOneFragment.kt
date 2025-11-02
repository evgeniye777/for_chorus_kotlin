package com.example.for_chour_kotlin.presentations.fragments._models

import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.the_planner_semen.my_menu.FragmentMenu

data class DateOneFragment(
    var menu_l: LinearLayout,
    var menu_name: String,
    var fragment: Fragment,
    var img_id: Int = -1,
    var optionR: Boolean = false,
    var option_f: FragmentMenu? =null
)