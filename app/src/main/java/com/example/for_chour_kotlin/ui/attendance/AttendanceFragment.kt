package com.example.for_chour_kotlin.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.for_chour_kotlin.PersonsInfo.infoOnePerson
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.ServerClass
import com.example.for_chour_kotlin.WriteMDB
import com.example.for_chour_kotlin.databinding.FragmentAttendanceBinding


public class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    var serverClass = ServerClass()
    lateinit var writeMDB: WriteMDB
    lateinit var masSpinner: List<String>
    lateinit var masPersson: List<String>
    lateinit var adapterSpinner: ArrayAdapter<String>
    lateinit var personsList: List<infoOnePerson>


    var mAdapter: PersonsAdapter? = null

    lateinit var mPersonsRec: RecyclerView
    lateinit var butWrite: Button
    lateinit var spinnerDate: Spinner

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        writeMDB = WriteMDB(activity)
        masSpinner = writeMDB.readSpinerMas()
        personsList = writeMDB.readPersonMas()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //adapter = ArrayAdapter<String>(requireActivity(), R.layout.item, R.id.tvText,masPersson)
        spinnerDate = binding.spinnerDate
        adapterSpinner = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, masSpinner)
        spinnerDate.adapter = adapterSpinner
        vivod(""+masSpinner.size)
        mPersonsRec = binding.gvMain
        mPersonsRec.setLayoutManager(GridLayoutManager(activity,3))
        updateUI()
        butWrite = binding.butWrite
        butWrite.setOnClickListener {
            writeMDB.writeSt("",0,personsList)
        } //gridView.setOnClickListener { itemListener }
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
            mAdapter!!.setSborniks(personsList)
            mAdapter!!.notifyDataSetChanged()
        }
    }
    class PersonHolder:
        ViewHolder,
        View.OnClickListener {
        var mSbornickk: infoOnePerson? = null
        var selectLayout: LinearLayout? = null
        var name: TextView? = null
        constructor(inflater: LayoutInflater, parent: ViewGroup?) : super(inflater.inflate(R.layout.item, parent, false)) {
            itemView.setOnClickListener(this)
            selectLayout =itemView.findViewById(R.id.styleLayout)
            name = itemView.findViewById(R.id.tvText)
        }

        fun bind(sbornickk: infoOnePerson?) {
            mSbornickk = sbornickk
            name?.text = mSbornickk?.getName()
            if (mSbornickk?.getState() ==0?: true) {
                selectLayout?.setBackgroundResource(R.drawable.rect1)
            }
            else {selectLayout?.setBackgroundResource(R.drawable.rect2)}
        }

        override fun onClick(view: View) {
            if (mSbornickk?.getState() ==0 ?: true) {mSbornickk?.setState(1); selectLayout?.setBackgroundResource(R.drawable.rect2)}
            else {mSbornickk?.setState(0);selectLayout?.setBackgroundResource(R.drawable.rect1)}

        }
    }

    inner class PersonsAdapter(sbornickk: List<infoOnePerson>) :
        RecyclerView.Adapter<PersonHolder>() {
        private var mSbornick: List<infoOnePerson>
        init {
            mSbornick = sbornickk
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
            val infoOneSbornickk: infoOnePerson = mSbornick[position]
            holder.bind(infoOneSbornickk)
        }

        override fun getItemCount(): Int {
            return mSbornick.size
        }

        fun setSborniks(sborniks: List<infoOnePerson>) {
            mSbornick = sborniks
        }
    }
}