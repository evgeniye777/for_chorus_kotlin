package com.example.for_chour_kotlin.presentations.fragments.attendance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.data.typeData.appAllGroups.ViewModelAppAllGroups
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.ViewModelAppDataParticipant
import com.example.for_chour_kotlin.data.typeData.appStPersons.ViewModelAppStPersons
import com.example.for_chour_kotlin.databinding.FragmentAttendanceBinding
import com.example.for_chour_kotlin.presentations._interfaces.OnDataListener
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.source.url_responses.AccountHolder
import com.example.for_chour_kotlin.data.typeData.appStPersons.AppStPersons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.view.isVisible
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.presentations.activities.MainActivity
import com.example.the_planner_semen.my_menu.FragmentMenu
import com.example.the_planner_semen.my_menu.InterfaceMenu

class AttendanceFragment : Fragment(), OnDataListener {
    private val groups: ViewModelAppAllGroups by activityViewModels()
    private val participants: ViewModelAppDataParticipant by activityViewModels()
    private val stPersons: ViewModelAppStPersons by activityViewModels()
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var calendar: CalendarView
    private lateinit var openCalendar: ImageButton
    private lateinit var idTextDate: TextView
    private lateinit var spinnerPurpose: Spinner
    private lateinit var butWrite: Button
    private lateinit var butCreate: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var textInfo: TextView
    private var adapter: StPersonsAdapter? = null
    private var selectedDate: String = ""
    private var groupHash: String? = null
    private var selectedStPersons: AppStPersons? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)

        openCalendar = binding.idButShowCalendar
        idTextDate = binding.idTextDate
        spinnerPurpose = binding.spinnerPurpose
        butWrite = binding.butWrite
        textInfo = binding.textInplace
        butCreate = binding.butCreate
        recyclerView = binding.gvMain
        calendar = binding.calendar

        // Восстановление selectedDate из savedInstanceState
        selectedDate = savedInstanceState?.getString("selectedDate") ?: formatDate(System.currentTimeMillis())

        initializeData()

        // Инициализируем слушатели
        butCreate.setOnClickListener { onCreateButtonClick() }
        butWrite.setOnClickListener { onWriteButtonClick() }

        val root: View = binding.root
        return root
    }

    private fun initializeData() {
        if (!isAdded) return  // Проверка, что фрагмент attached

        val nameTable = AuthorizationState.groups?.focus?.value?.listNameBases?.get("st_persons_sinch") ?: ""

        if (nameTable.isEmpty()) {
            (requireActivity() as MainActivity).vivod("Ошибка чтения данных")  // Замена на requireActivity()
            return
        }

        stPersons.connection(AuthorizationState.database, nameTable)
        AuthorizationState.stPersons = stPersons
        groupHash = groups.focus.value?.hashName

        if (stPersons.stPersons.value?.isNotEmpty() == true) {
            val last_index: String? = stPersons.stPersons.value?.lastOrNull()?.id?.toString()
            if (groupHash != null && last_index != null) {
                AuthorizationState.typeResponses?.uploadingUpdatesStPersonsSinch(groupHash!!, last_index, {
                    if (isAdded) stPersons.rereadIt()  // Проверка перед вызовом
                }, {})
            }
        } else {
            groupHash?.let {
                AuthorizationState.typeResponses?.uploadingStPersonsSinch(it, {
                    if (isAdded) stPersons.connection(AuthorizationState.database, nameTable)  // Проверка
                }, {})
            }
        }

        installCalendar()
        initOpenCalendar()
        setupSpinner()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeView() {
        if (!isAdded) return  // Проверка

        highlightDatesInCalendar()
        clickCalendar(selectedDate)

        // Устанавливаем TouchListener для закрытия календаря
        binding.root.setOnTouchListener { v, event ->
            if (binding.calendar.isVisible && !isPointInsideCalendar(event.x, event.y)) {
                binding.calendar.visibility = View.GONE
                openCalendar.setImageResource(R.drawable.img_v_calendar_visible)
            }
            false
        }
    }

    @SuppressLint("DefaultLocale")
    private fun highlightDatesInCalendar() {
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = String.format("%04d.%02d.%02d", year, month + 1, dayOfMonth)
            clickCalendar(selectedDate)
        }
    }

    private fun clickCalendar(selectedDate: String) {
        idTextDate.text = selectedDate
        val stPersonsDate = stPersons.stPersons.value?.find { it.date == selectedDate }
        loadRecord(stPersonsDate)
        clickButtonOpenCalendar(false)
    }

    private fun loadRecord(record: AppStPersons?) {
        if (!isAdded) return  // Проверка

        if (record != null) {
            spinnerPurpose.setSelection(record.purpose)
            adapter = StPersonsAdapter(
                participants.participants.value ?: listOf(),
                spinnerPurpose.selectedItemId.toInt(),
                record,
                this
            )
            selectedStPersons = record
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

            (requireActivity() as MainActivity).vivodMes("$record")

            when (record.sinch) {
                0 -> managerState(1)
                1 -> managerState(2)
                2 -> managerState(3)
            }
        } else {
            managerState(0)
        }
    }

    private fun onCreateButtonClick() {
        if (!isAdded) return  // Проверка

        val committer = AccountHolder.hashName
        adapter = StPersonsAdapter(participants.participants.value ?: listOf(), spinnerPurpose.selectedItemId.toInt(), null, this)
        recyclerView.adapter = adapter
        val id_reverse: Int? = stPersons.stPersons.value?.firstOrNull()?.id?.minus(1)

        if (id_reverse != null) {
            val newRecord = adapter?.createAppStPersons(committer, selectedDate, id_reverse)
            if (newRecord != null) {
                newRecord.sinch = 0
                stPersons.addItem(newRecord)
                selectedStPersons = newRecord
                loadRecord(newRecord)
            }
        } else {
            (requireActivity() as MainActivity).vivod("Ошибка создания")  // Замена
        }
    }

    private fun onWriteButtonClick() {
        if (!isAdded || adapter == null) return  // Проверка

        try {
            if (adapter!!.initAppStPersons()) {
                val appStPersons = adapter!!.getAppStPersons()
                appStPersons?.sinch = 1
                if (appStPersons != null && stPersons.addOrUpdateItem(appStPersons) >= 1) {
                    adapter!!.update()
                    selectedStPersons = appStPersons
                } else {
                    (requireActivity() as MainActivity).vivod("Ошибка сохранения")  // Замена
                }
            } else {
                val appStPersons = adapter!!.createAppStPersons(
                    AccountHolder.hashName,
                    idTextDate.text.toString(),
                    stPersons.stPersons.value?.last()?.id?.plus(1) ?: -1
                )
                appStPersons?.sinch = 1
                if (appStPersons != null && stPersons.addOrUpdateItem(appStPersons) >= 1) {
                    adapter!!.update()
                    selectedStPersons = appStPersons
                } else {
                    (requireActivity() as MainActivity).vivod("Ошибка сохранения")  // Замена
                }
            }
            managerState(2)
        } catch (e: Exception) {
            (requireActivity() as MainActivity).vivod("Ошибка записи: ${e.message}")  // Обработка исключений
        }
    }

    private fun setupSpinner() {
        groups.focus.observe(viewLifecycleOwner) { focus ->
            if (!isAdded) return@observe  // Проверка, что фрагмент attached

            val purposes = focus?.data?.find { it.type == "purposes" }?.data
            val adapterSpinner = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                purposes?.map { it.name } ?: listOf()
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPurpose.adapter = adapterSpinner

            // Безопасная установка selection: проверка на пустой список
            val selectionIndex = selectedStPersons?.purpose ?: 0
            if (purposes != null && purposes.isNotEmpty() && selectionIndex < purposes.size) {
                spinnerPurpose.setSelection(selectionIndex)
            } else {
                spinnerPurpose.setSelection(0)  // Или не устанавливать, если список пустой
            }

            spinnerPurpose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    try {
                        // Проверка на null и диапазон перед доступом
                        if (purposes != null && position >= 0 && position < purposes.size) {
                            val selectedPurposeId = purposes[position].id?.toIntOrNull() ?: 0
                            // Используйте selectedPurposeId по необходимости (например, обновить adapter)
                        }
                    } catch (e: Exception) {
                        // Логирование или обработка ошибки
                        (requireActivity() as MainActivity).vivod("Ошибка в onItemSelected: ${e.message}")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Ничего не делать
                }
            }
        }
    }


    private fun initOpenCalendar() {
        calendar.visibility = View.GONE
        openCalendar.setOnClickListener {
            clickButtonOpenCalendar()
        }
    }

    private fun clickButtonOpenCalendar(state: Boolean? = null) {
        if (state == null && calendar.isGone) {
            binding.calendar.visibility = View.VISIBLE
            openCalendar.setImageResource(R.drawable.img_v_calendar_hide)
        } else {
            calendar.visibility = View.GONE
            openCalendar.setImageResource(R.drawable.img_v_calendar_visible)
        }
    }

    private fun installCalendar() {
        val today = System.currentTimeMillis()
        calendar.date = today

        if (selectedDate.isEmpty()) {
            selectedDate = formatDate(today)
        }

        idTextDate.text = selectedDate
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun isPointInsideCalendar(x: Float, y: Float): Boolean {
        val calendarLocation = IntArray(2)
        binding.calendar.getLocationOnScreen(calendarLocation)
        val calendarX = calendarLocation[0]
        val calendarY = calendarLocation[1]
        val width = binding.calendar.width
        val height = binding.calendar.height
        return x >= calendarX && x <= calendarX + width && y >= calendarY && y <= calendarY + height
    }

    override fun onDataUpdate(counts: Triple<Int, Int, Int>) {}

    private fun managerState(state: Int) {
        // 0 - нет записи; 1 - запись создана, но не записана в базу; 2 - Запись добавлена; 3 - запись отправлена
        when (state) {
            0 -> {
                butCreate.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                butWrite.isEnabled = false
                spinnerPurpose.isEnabled = false
            }
            1 -> {
                butCreate.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                butWrite.isEnabled = true
                butWrite.text = "Записать"
                spinnerPurpose.isEnabled = true
            }
            2 -> {
                butCreate.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                butWrite.isEnabled = true
                butWrite.text = "Перезаписать"
                spinnerPurpose.isEnabled = true
            }
            3 -> {
                butCreate.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                butWrite.isEnabled = false
                butWrite.text = "Записать"
                spinnerPurpose.isEnabled = false
            }
        }
    }
    var menuFragment: FragmentMenu  = init_my_mune()
    private fun init_my_mune(): FragmentMenu {
        val items = listOf("Отправить запись", "Удалить запись")
        val fragmentMenu = FragmentMenu()
        fragmentMenu.getDate(items, object : InterfaceMenu.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (!isAdded) return  // Проверка, что фрагмент attached

                when (position) {
                    0 -> {
                        try {
                            groupHash = groups.focus.value?.hashName
                            val last_index: String = stPersons.lastId.value.toString()
                            if (groupHash != null && selectedStPersons != null) {
                                AuthorizationState.typeResponses?.sendStPersonsData(
                                    groupHash!!, last_index, selectedStPersons!!, {
                                        if (isAdded) {
                                            stPersons.rereadIt()
                                            clickCalendar(selectedDate)
                                            (requireActivity() as MainActivity).vivodMes("Данные синхронизированы")  // Замена на requireActivity()
                                        }
                                    }, {
                                        (requireActivity() as MainActivity).vivodMes("Ошибка sendStPersonsData")  // Замена
                                    })
                            } else {
                                (requireActivity() as MainActivity).vivodMes("Ошибка null groupHash || selectedStPersons")  // Замена
                            }
                        } catch (_: Exception) {
                            (requireActivity() as MainActivity).vivodMes("Ошибка отправки")  // Замена
                        }
                    }
                    1 -> {
                        try {
                            if (selectedStPersons != null) {
                                if (selectedStPersons!!.sinch != 2) {
                                    stPersons.deleteItem(selectedStPersons!!)
                                    clickCalendar(selectedDate)
                                    (requireActivity() as MainActivity).vivodMes("Запись удалена")  // Замена
                                } else {
                                    (requireActivity() as MainActivity).vivodMes("Нельзя удалить/изменить синхронизированную запись")  // Замена
                                }
                            } else {
                                (requireActivity() as MainActivity).vivodMes("Нет записи")  // Замена
                            }
                        } catch (_: Exception) {
                            (requireActivity() as MainActivity).vivodMes("Ошибка удаления")  // Замена
                        }
                    }
                    else -> {
                        // Ничего не делать
                    }
                }
            }
        })
        return fragmentMenu
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedDate", selectedDate)  // Сохранение selectedDate для восстановления после поворота
    }

    override fun onResume() {
        super.onResume()
        if (groupHash != groups.focus.value?.hashName) {
            initializeData()
            initializeView()
        }
    }
}

