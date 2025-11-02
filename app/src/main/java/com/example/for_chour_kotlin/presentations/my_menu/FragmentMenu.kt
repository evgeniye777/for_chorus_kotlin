package com.example.the_planner_semen.my_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.R

class FragmentMenu : Fragment(), My_Menu_Adapter.HideFragmentListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: My_Menu_Adapter
    private var items: List<String>? = null // Измените на nullable тип
    private var listener: InterfaceMenu.OnItemClickListener? = null // Измените на nullable тип

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.my_menu_recycler, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Скрываем меню при клике на оставшуюся часть экрана
        val linearAllmenu: LinearLayout = view.findViewById(R.id.my_linear_layout)
        linearAllmenu.setOnClickListener { parentFragmentManager.beginTransaction().hide(this).commit() }

        // Инициализация списка
        recyclerView = view.findViewById(R.id.id_my_menu)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Проверяем, инициализированы ли items и listener
        if (items != null && listener != null) {
            // Инициализируйте адаптер с обработчиком нажатий
            adapter = My_Menu_Adapter(items!!, listener!!, this) // Используйте !! для безопасного извлечения
            recyclerView.adapter = adapter
        }
    }

    fun getDate(items0: List<String>, listener0: InterfaceMenu.OnItemClickListener) {
        items = items0
        listener = listener0

        // Если фрагмент уже создан, обновите адаптер
        if (::recyclerView.isInitialized) {
            adapter = My_Menu_Adapter(items!!, listener!!, this)
            recyclerView.adapter = adapter
        }
    }

    override fun onHideFragment() {
        parentFragmentManager.beginTransaction().hide(this).commit()
    }
}
