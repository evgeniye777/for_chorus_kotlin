package com.example.for_chour_kotlin.presentations.fragments.attendance

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.for_chour_kotlin.domain.entities.infoOnePerson
import com.example.for_chour_kotlin.domain.entities.infoOneRec
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.data.source.remote.ServerClass
import com.example.for_chour_kotlin.data.source.remote.SendLastDate
import com.example.for_chour_kotlin.domain.WriteMDB
import com.example.for_chour_kotlin.databinding.FragmentAttendanceBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date


public class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    var serverClass =
        ServerClass()
    lateinit var writeMDB: WriteMDB
    lateinit var masSpinner: MutableList<String>
    lateinit var masPurpose: List<String>
    lateinit var adapterSpinner: ArrayAdapter<String>
    lateinit var adapterPurpose: ArrayAdapter<String>
    lateinit var personsList: List<infoOnePerson>
    lateinit var listInfoRec: List<infoOneRec>

    lateinit var calendarView: CalendarView
    lateinit var butShowCalendar: ImageButton

    var cursorRec: Int = -1
    var rStart: Boolean = true;

    var today: String = "2025.02.18"


    var mAdapter: PersonsAdapter? = null

    lateinit var mPersonsRec: RecyclerView
    lateinit var butWrite: Button
    lateinit var spinnerDate: Spinner
    lateinit var spinnerPurpose: Spinner
    lateinit var textInPlace: TextView
    lateinit var editDate: EditText
    lateinit var checkBoxActive: CheckBox

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        var date = Date()
        var sdf = SimpleDateFormat("YYYY.MM.dd")
        val dateString: String = sdf.format(date)
        today = dateString
        writeMDB =
            WriteMDB(activity, context)
        listInfoRec = writeMDB.readOneRecMas(today)
        masSpinner = writeMDB.spinnerDay
        personsList = writeMDB.readPersonMas()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rStart =true;
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //adapter = ArrayAdapter<String>(requireActivity(), R.layout.item, R.id.tvText,masPersson)
        textInPlace = binding.textInplace
        spinnerPurpose = binding.spinnerPurpose
        masPurpose = listOf("Общий", "Сестринский", "Братский","Служение","Репетиция Св.","Служение Св.")
        adapterPurpose = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, masPurpose)
        spinnerPurpose.adapter = adapterPurpose
        spinnerPurpose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                updateUI()
                rStart = false;
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработка случая, когда ничего не выбрано
            }
        }
        //vivod(""+masSpinner.size)
        mPersonsRec = binding.gvMain
        mPersonsRec.setLayoutManager(GridLayoutManager(activity,2))

        butWrite = binding.butWrite
        butWrite.setOnClickListener {
            var date: String = masSpinner.get(spinnerDate.selectedItemPosition)
            if (date.equals("Сегодня")) {date = today}

            if (cursorRec==-1) {
                writeMDB.writeSt(date,spinnerPurpose.selectedItemPosition ,personsList)
            }else {
                writeMDB.overWriteSt(date,spinnerPurpose.selectedItemPosition ,listInfoRec.get(cursorRec).getRecList())
            }



            if (!rStart) {
                listInfoRec = writeMDB.readOneRecMas(today)
                masSpinner = writeMDB.spinnerDay
                cursorRec = 0
                updateUI()}
        }

        spinnerDate = binding.spinnerDate
        adapterSpinner = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, masSpinner)
        spinnerDate.adapter = adapterSpinner
        spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ViborDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработка случая, когда ничего не выбрано
            }
        }

        editDate = binding.editDate
        checkBoxActive = binding.checkActive
        checkBoxActive.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                changeMasSpiner()
                checkBoxActive.isChecked = false
            } else {
            }
        }


        calendarView = binding.calendar

        butShowCalendar = binding.idButShowCalendar
        butShowCalendar.setOnClickListener {
            show_hide_Calendar()
        }

        show_hide_Calendar()

        return root
    }

    private fun changeMasSpiner() {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        try {
            val date = LocalDate.parse(editDate.text, formatter)
            var formattedDate = date.format(formatter)

            val today = LocalDate.now()
            println("Сегодняшняя дата: $today")
            // Сравниваем даты
            when {date.isEqual(today) -> { formattedDate = "Сегодня" } }
            var r: Boolean = false
            for (i in 0..masSpinner.size-1) {
                if (masSpinner[i].equals(formattedDate)) {
                    spinnerDate.setSelection(i);
                    r=true
                    break
                }
            }
            if (!r) {
                masSpinner.add(0,formattedDate);
                adapterSpinner = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, masSpinner)
                spinnerDate.adapter = adapterSpinner
            }
        } catch (e: DateTimeParseException) {
            vivod("Укажите правильный формат даты (пример 2025.01.01 -> год.месяц.день)")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun vivod(s: String?) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
    private fun updateUI() {
        if (mAdapter == null) {
            mAdapter = PersonsAdapter(personsList)
            mPersonsRec.setAdapter(mAdapter)
        } else {
            mAdapter!!.setListPersons(personsList)
            mAdapter!!.notifyDataSetChanged()
            mPersonsRec.setAdapter(mAdapter)
        }
        if (cursorRec>=0) {butWrite.text = "Перезаписать";butWrite.setBackgroundColor(resources.getColor(R.color.pereza))}
        else {butWrite.text = "Записать"; butWrite.setBackgroundColor(resources.getColor(R.color.purple_500))}
        textInPlace.setText(""+counting())
    }

    private fun counting():String {
        //все
        var n: Int = 0
        var nMustAll: Int = 0
        var nAbsAll: Int = 0

        //братья
        var n_b = 0
        var nMustAll_b = 0
        var nAbsAll_b = 0

        //сестры
        var n_s = 0
        var nMustAll_s = 0
        var nAbsAll_s = 0

        var purpose: Int = spinnerPurpose.selectedItemPosition
        if (cursorRec==-1) {
            for (infoP: infoOnePerson in personsList) {

                if (infoP.state==1) {n++
                    if (infoP.gender==2) {n_b++}
                    else if (infoP.gender==1) {n_s++}
                }

                if (infoP.allowed in 0..1&&(purpose==0||purpose==3&& infoP.allowed ==1|| infoP.gender ==purpose)) {
                    nMustAll++
                    if (infoP.gender==2) {nMustAll_b++}
                    else if (infoP.gender==1) {nMustAll_s++}
                }

                if (infoP.gender==2) {nAbsAll_b++}
                else if (infoP.gender==1) {nAbsAll_s++}
                nAbsAll++
            }
        }
        else {
            var i=0;
            for (infoR:String in listInfoRec.get(cursorRec).getRecList()) {
                if (i>=personsList.size) {break}
                val infoP: infoOnePerson = personsList.get(i)
                if (infoR.equals("p")) {
                    n++
                    if (infoP.gender == 2) {
                        n_b++;
                    } else if (infoP.gender == 1) {
                        n_s++;
                    }
                }

                if (infoP.allowed in 0..1&&(purpose==0||purpose==3&& infoP.allowed ==1|| infoP.gender ==purpose)) {
                    nMustAll++
                    if (infoP.gender == 2) {
                        nMustAll_b++
                    } else if (infoP.gender == 1) {
                        nMustAll_s++
                    }
                }


                if (infoP.gender==2) {nAbsAll_b++}
                else if (infoP.gender==1) {nAbsAll_s++}
                nAbsAll++
                i++
            }
        }
        return "Все: "+n+"/"+nMustAll+" ["+nAbsAll+"]  "+"Б: "+n_b+"/"+nMustAll_b+" ["+nAbsAll_b+"]  "+"С: "+n_s+"/"+nMustAll_s+" ["+nAbsAll_s+"] "
    }

    //метод для показа и скрывания календаря
    fun show_hide_Calendar() {
        if (calendarView.visibility == View.VISIBLE) {
            calendarView.visibility = View.GONE
            butShowCalendar.setImageResource(R.drawable.img_v_calendar_visible)
        }
        else {
            calendarView.visibility = View.VISIBLE
            butShowCalendar.setImageResource(R.drawable.img_v_calendar_hide)
        }

        vivod(""+calendarView.visibility)
    }

    fun ViborDate() {
        var date: String = masSpinner.get(spinnerDate.selectedItemPosition)
        if (date.equals("Сегодня")) {date=today;}
        cursorRec = -1
        for (i in 0..listInfoRec.size-1) {
            if (date.equals(listInfoRec.get(i).getRecDate())) {
                cursorRec = i;
                break
            }
        }
        if (cursorRec >=0) {spinnerPurpose.isEnabled = false;
            spinnerPurpose.setSelection(listInfoRec.get(cursorRec).purpose)
        }
        else {spinnerPurpose.isEnabled = true;
            spinnerPurpose.setSelection(0)
        }
        if (!rStart) {updateUI();}
    }

    fun vivodMes(s: String) {
        // построение диалогового окна
        val dialogBuilder = AlertDialog.Builder(activity)
        // установка сообщения диалогового окна
        dialogBuilder.setMessage(s)
            // отмена возможности отмены диалога
            .setCancelable(false)
            // текст и действие для положительной кнопки
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->  })
            // текст и действие для отрицательной кнопки
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->  })
        // создание диалогового окна
        val alert = dialogBuilder.create()
        // установка заголовка для диалогового окна
        alert.setTitle("AlertDialogExample")
        // вывод диалогового окна
        alert.show()
    }
    inner class PersonHolder:
        ViewHolder,
        View.OnClickListener {
        var mPerson: infoOnePerson? = null
        var selectLayout: LinearLayout? = null
        var name: TextView? = null
        constructor(inflater: LayoutInflater, parent: ViewGroup?) : super(inflater.inflate(R.layout.item, parent, false)) {
            itemView.setOnClickListener(this)
            selectLayout =itemView.findViewById(R.id.styleLayout)
            name = itemView.findViewById(R.id.tvText)
        }

        fun bind(sperson: infoOnePerson?) {
            mPerson = sperson
            name?.text = mPerson?.name
            if (cursorRec==-1) {
                var purpose: Int = spinnerPurpose.selectedItemPosition
            if (mPerson?.state ==0?: true) {
                if (mPerson?.allowed in 0..1&&(purpose==0||purpose==4||purpose==5||purpose==3&&mPerson?.allowed==1||mPerson?.gender==purpose)) { selectLayout?.setBackgroundResource(R.drawable.rect1)}
                else {selectLayout?.setBackgroundResource(R.drawable.rect_gone)}
            }
            else {
                selectLayout?.setBackgroundResource(R.drawable.rect2)
            }
            } else {
                var infoCs: List<String> = listInfoRec.get(cursorRec).getRecList()
                var id: Int = mPerson?.id ?: 1
                id--
                if (infoCs.get(id).equals("p")) {
                    selectLayout?.setBackgroundResource(R.drawable.rect2)
                }else if (infoCs.get(id).equals("n")) {
                    selectLayout?.setBackgroundResource(R.drawable.rect_not)
                }
                else  {
                    selectLayout?.setBackgroundResource(R.drawable.rect_gone)
                }
            }
        }

        @SuppressLint("SuspiciousIndentation", "SetTextI18n")
        override fun onClick(view: View) {
            if (cursorRec == -1) {
                val purpose: Int = spinnerPurpose.selectedItemPosition
                if (mPerson?.state == 0) {
                    mPerson?.state = 1
                    selectLayout?.setBackgroundResource(R.drawable.rect2)
                } else if (!(mPerson?.allowed in 0..1 && (purpose == 0 ||purpose == 4 ||purpose == 5  ||(purpose == 3 && mPerson?.allowed == 1) || mPerson?.gender == purpose))) {
                    selectLayout?.setBackgroundResource(R.drawable.rect_gone)
                    mPerson?.state = 0
                } else {
                    mPerson?.state = 0
                    selectLayout?.setBackgroundResource(R.drawable.rect1)
                }
            } else {
                val purpose: Int = spinnerPurpose.selectedItemPosition
                val listNow: MutableList<String> = listInfoRec[cursorRec].getRecList()
                var id: Int = (mPerson?.id ?: 1) - 1

                try {
                    if (listNow[id].isNotEmpty() && listNow[id] != "p") {
                        listNow[id] = "p"
                        selectLayout?.setBackgroundResource(R.drawable.rect2)
                    } else if (!(mPerson?.allowed in 0..1 && (purpose == 0 || (purpose == 3 && mPerson?.allowed == 1) || mPerson?.gender == purpose))) {
                        selectLayout?.setBackgroundResource(R.drawable.rect_gone)
                        listNow[id] = "d"
                    } else {
                        listNow[id] = "n"
                        selectLayout?.setBackgroundResource(R.drawable.rect_not)
                    }
                } catch (_: Exception) {}
            }
            textInPlace.text = counting().toString()
        }
    }

    inner class PersonsAdapter(listPersons: List<infoOnePerson>) :
        RecyclerView.Adapter<PersonHolder>() {
        private var mListPersons: List<infoOnePerson>
        init {
            mListPersons = listPersons
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            i: Int
        ): PersonHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return PersonHolder(
                layoutInflater,
                parent
            )
        }

        override fun onBindViewHolder(
            holder: PersonHolder,
            position: Int
        ) {
           //val infoOnePerson1: infoOnePerson = mListPersons[position]
                holder.bind(mListPersons[position])
        }

        override fun getItemCount(): Int {
            return mListPersons.size
        }

        fun setListPersons(spersons: List<infoOnePerson>) {
            mListPersons = spersons
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_attendance, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toSet -> {
                writeMDB.toSet();
                return true;
            }
            R.id.toSetLast -> {
                val sendObject: SendLastDate = SendLastDate()
                sendObject.startSend(requireActivity(),requireContext())
                return true;
            }
            R.id.MyDelo -> {
                writeMDB.muDelo();
                return true;
            }
            R.id.Update -> {
                vivodMes(writeMDB.Update());
                return true;
            }
            R.id.Del-> {
                if (cursorRec>=0) {
                    var sel: Int = spinnerDate.selectedItemPosition
                    var day: String = masSpinner[sel]
                    val dialogBuilder = AlertDialog.Builder(activity)
                    dialogBuilder.setTitle("Удаление записи")
                        .setMessage("Будет удалена запись за " + day + " число, вы уверены?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Удалить",
                            DialogInterface.OnClickListener { dialog, id ->
                                if (day.equals("Сегодня")) {
                                    day = today
                                }
                                writeMDB.Del(day);
                                listInfoRec = writeMDB.readOneRecMas(today)
                                masSpinner = writeMDB.spinnerDay
                                personsList = writeMDB.readPersonMas()
                                adapterSpinner = ArrayAdapter<String>(
                                    requireActivity(),
                                    android.R.layout.simple_list_item_1,
                                    masSpinner
                                )
                                spinnerDate.adapter = adapterSpinner
                                if (sel == masSpinner.size) {
                                    sel--;
                                }
                                spinnerDate.setSelection(sel)
                                //ViborDate()
                                updateUI()
                            })
                        .setNegativeButton(
                            "Отмена",
                            DialogInterface.OnClickListener { dialog, id -> })
                    val alert = dialogBuilder.create()
                    alert.setTitle("AlertDialogExample")
                    alert.show()
                }else {vivodMes("На Сегодня запись отсутствует")}
                return true;
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    fun joinTo(l: List<String>): String {
        var str: String=""
        for (i in 0..l.size-1) {
        }
        return str
    }
    override fun onStart() {
        super.onStart()
        if (!rStart) {updateUI()}
    }
}