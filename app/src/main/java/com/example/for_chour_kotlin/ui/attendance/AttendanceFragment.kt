package com.example.for_chour_kotlin.ui.attendance

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.for_chour_kotlin.PersonsInfo.infoOnePerson
import com.example.for_chour_kotlin.PersonsInfo.infoOneRec
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.ServerClass
import com.example.for_chour_kotlin.WriteMDB
import com.example.for_chour_kotlin.databinding.FragmentAttendanceBinding
import java.util.Date


public class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    var serverClass = ServerClass()
    lateinit var writeMDB: WriteMDB
    lateinit var masSpinner: List<String>
    lateinit var masPurpose: List<String>
    lateinit var adapterSpinner: ArrayAdapter<String>
    lateinit var adapterPurpose: ArrayAdapter<String>
    lateinit var personsList: List<infoOnePerson>
    lateinit var listInfoRec: List<infoOneRec>
    var cursorRec: Int = -1
    var rStart: Boolean = true;

    var today: String = "14.01.2025"


    var mAdapter: PersonsAdapter? = null

    lateinit var mPersonsRec: RecyclerView
    lateinit var butWrite: Button
    lateinit var spinnerDate: Spinner
    lateinit var spinnerPurpose: Spinner
    lateinit var textInPlace: TextView

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var date: Date = Date();
        var sdf:SimpleDateFormat = SimpleDateFormat("dd.MM.YYYY")
        val dateString: String = sdf.format(date)
        today = dateString
        writeMDB = WriteMDB(activity)
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
        masPurpose = listOf("Общий", "Сестринский", "Братский","Служение")
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


            listInfoRec = writeMDB.readOneRecMas(today)
            masSpinner = writeMDB.spinnerDay
            cursorRec = 0
            if (!rStart) {updateUI()}
        }

        spinnerDate = binding.spinnerDate
        adapterSpinner = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, masSpinner)
        spinnerDate.adapter = adapterSpinner
        spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработка случая, когда ничего не выбрано
            }
        }
        return root
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

    }

    private fun counting():Int {
        var n: Int=0;
        if (cursorRec==-1) {
            for (infoP:infoOnePerson in personsList) {
                if (infoP.getState()==1) {
                    n++
                }
            }
        }
        else {
            for (infoR:String in listInfoRec.get(cursorRec).getRecList()) {
                if (infoR.equals("p")) {
                    n++
                }
            }
        }
        return n
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
            name?.text = mPerson?.getName()
            if (cursorRec==-1) {
            if (mPerson?.getState() ==0?: true) {
                var purpose: Int = spinnerPurpose.selectedItemPosition
                if (purpose==0||purpose==3&&mPerson?.getAllowed()==1||mPerson?.getGender()==purpose) { selectLayout?.setBackgroundResource(R.drawable.rect1)}
                else {selectLayout?.setBackgroundResource(R.drawable.rect_gone)}
            }
            else {selectLayout?.setBackgroundResource(R.drawable.rect2)
            }
            } else {
                var infoCs: List<String> = listInfoRec.get(cursorRec).getRecList()
                var id: Int = mPerson?.getId() ?: 1
                id--
                if (infoCs.get(id).equals("p")) {
                    selectLayout?.setBackgroundResource(R.drawable.rect2)
                }else if (infoCs.get(id).equals("n")) {
                    selectLayout?.setBackgroundResource(R.drawable.rect_not)
                }
                else if (infoCs.get(id).equals("d")|| infoCs.get(id).equals("")) {
                    selectLayout?.setBackgroundResource(R.drawable.rect_gone)
                }
            }
        }

        override fun onClick(view: View) {
            if (cursorRec==-1) {
                var purpose: Int = spinnerPurpose.selectedItemPosition
                if (purpose==0||purpose==3&&mPerson?.getAllowed()==1||mPerson?.getGender()==purpose) {
                    if (mPerson?.getState() == 0 ?: true) {
                        mPerson?.setState(1); selectLayout?.setBackgroundResource(R.drawable.rect2)
                    } else {
                        mPerson?.setState(0);selectLayout?.setBackgroundResource(R.drawable.rect1)
                    }
                }
            }
            else {
                var purpose: Int = spinnerPurpose.selectedItemPosition
                var listNow: MutableList<String>  = listInfoRec.get(cursorRec).getRecList()
                var id: Int = mPerson?.getId() ?:1;
                id--
                if (purpose==0||purpose==3&&mPerson?.getAllowed()==1||mPerson?.getGender()==purpose) {
                    if (listNow.get(id).equals("p")) {
                       listNow.set(id,"n")
                        selectLayout?.setBackgroundResource(R.drawable.rect_not)
                    }
                    else {listNow.set(id,"p")
                        selectLayout?.setBackgroundResource(R.drawable.rect2)
                    }
                }
            }
            textInPlace.setText(""+counting());
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
            val infoOnePerson1: infoOnePerson = mListPersons[position]
                holder.bind(infoOnePerson1)
        }

        override fun getItemCount(): Int {
            return mListPersons.size
        }

        fun setListPersons(spersons: List<infoOnePerson>) {
            mListPersons = spersons
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