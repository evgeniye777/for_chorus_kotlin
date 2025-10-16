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

    private var selectedStPersons: AppStPersons? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nameTable = AuthorizationState.groups?.focus?.value?.listNameBases?.get("st_persons_sinch") ?:"";

        binding = FragmentAttendanceBinding.inflate(inflater, container, false)

        if (nameTable.isEmpty()) {
            AuthorizationState.mainActivity?.vivod("Ошибка чтения данных"); return binding.root}

        stPersons.connection(AuthorizationState.database, nameTable)


        openCalendar = binding.idButShowCalendar
        idTextDate = binding.idTextDate
        spinnerPurpose = binding.spinnerPurpose
        butWrite = binding.butWrite
        textInfo = binding.textInplace
        butCreate = binding.butCreate
        recyclerView = binding.gvMain
        calendar = binding.calendar

        installCalendar()
        initOpenCalendar()
        setupSpinner()  // Заполняем Spinner

        // Инициализируем слушатели
        butCreate.setOnClickListener { onCreateButtonClick() }
        butWrite.setOnClickListener { onWriteButtonClick() }

        val root: View = binding.root
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подписываемся на данные stPersons для проверки дат
        stPersons.stPersons.observe(viewLifecycleOwner) { list ->
            val dates = list?.map { it.date }?.toSet() ?: emptySet()
            highlightDatesInCalendar(dates)  // Выделяем даты (хотя CalendarView не идеален, используем workaround)
        }

        // Устанавливаем TouchListener для закрытия календаря
        binding.root.setOnTouchListener { v, event ->
            if (binding.calendar.isVisible && !isPointInsideCalendar(event.x, event.y)) {
                binding.calendar.visibility = View.GONE
                openCalendar.setImageResource(R.drawable.img_v_calendar_visible)
            }
            false
        }
    }

    private fun highlightDatesInCalendar(dates: Set<String>) {
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year.${month + 1}.$dayOfMonth"
            idTextDate.text = selectedDate
            checkRecordsForDate(selectedDate)  // Проверяем записи для даты
        }
    }

    private fun checkRecordsForDate(date: String) {
        stPersons.stPersons.observe(viewLifecycleOwner) { list ->
            val recordsForDate = list?.filter { it.date == date } ?: emptyList()
            if (recordsForDate.isEmpty()) {
                managerState(0)
            } else {
                setupCommitterList(recordsForDate)
                val latest = recordsForDate.maxByOrNull { it.dateWrite }
                if (latest != null) {
                    loadRecord(latest)
                }
            }
        }
    }

    private fun setupCommitterList(records: List<AppStPersons>) {
        val layout = binding.committersLayout
        layout.removeAllViews()
        records.forEach { record ->
            val committerParticipant = participants.participants.value?.find { it.hashName == record.committer }
            val textView = TextView(requireContext()).apply {
                text = committerParticipant?.pName ?: record.committer
                //setPadding(16, 8, 16, 8)
                width = 300
                height = 80
                textSize = 10f
                setBackgroundResource(R.drawable.rect1)  // Можно стилизовать
                setOnClickListener {
                    loadRecord(record)
                }
            }
            layout.addView(textView)
        }
    }

    private fun loadRecord(record: AppStPersons) {
        adapter = StPersonsAdapter(participants.participants.value ?: listOf(), spinnerPurpose.selectedItemId.toInt(), null, this)
        selectedStPersons = record
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        if (record.sinch == 0) {
            if (adapter?.initAppStPersons() == false) {
                managerState(1)
            }
            else {
                managerState(2)
            }
        } else {
            managerState(1)
        }
    }

    private fun onCreateButtonClick() {
        val committer = AccountHolder.hashName
        adapter = StPersonsAdapter(participants.participants.value ?: listOf(), spinnerPurpose.selectedItemId.toInt(), null, this)
        recyclerView.adapter = adapter
        val newRecord = adapter?.createAppStPersons(committer, selectedDate)
        if (newRecord != null) {
            stPersons.addItem(newRecord)
            loadRecord(newRecord)
        }
    }

    private fun onWriteButtonClick() {

        if (adapter != null) {
            if (adapter!!.initAppStPersons()) {
                val appStPersons = adapter!!.getAppStPersons()
                if (appStPersons != null && stPersons.updateItem(appStPersons)>=1) {
                    adapter!!.update()
                }
                else {
                    AuthorizationState.mainActivity?.vivod("Ошибка сохранения")
                }
            }
            else {
                AuthorizationState.mainActivity?.vivodMes(AccountHolder.hashName)
                val appStPersons = adapter!!.createAppStPersons(AccountHolder.hashName,
                    idTextDate.text as String
                )
                if (appStPersons!=null && stPersons.addItem(appStPersons)>=1) {
                    adapter!!.update()
                }
                else {
                    AuthorizationState.mainActivity?.vivod("Ошибка сохранения")
                }
            }
            managerState(2)
        }
    }

    private fun setupSpinner() {
        groups.focus.observe(viewLifecycleOwner) { focus ->
            val purposes = focus?.data?.find { it.type == "purposes" }?.data
            val adapterSpinner = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                purposes?.map { it.name } ?: listOf()
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPurpose.adapter = adapterSpinner

            spinnerPurpose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedPurposeId = purposes?.get(position)?.id?.toIntOrNull() ?: 0
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle nothing selected
                }
            }
        }
    }

    private fun initOpenCalendar() {
        calendar.visibility = View.GONE
        openCalendar.setOnClickListener {
            if (calendar.isGone) {
                binding.calendar.visibility = View.VISIBLE
                openCalendar.setImageResource(R.drawable.img_v_calendar_hide)
            } else {
                calendar.visibility = View.GONE
                openCalendar.setImageResource(R.drawable.img_v_calendar_visible)
            }
        }
    }

    private fun installCalendar() {
        val today = System.currentTimeMillis()
        calendar.date = today
        selectedDate = formatDate(today)  // Инициализируем выбранную дату
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

    override fun onDataUpdate(counts: Triple<Int, Int, Int>) {
    }


    private fun managerState(state: Int) {
        //0 - нет записи; 1 - запись создана, но не записана в базу; 2 - Запись добавлена
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
        }
    }
}