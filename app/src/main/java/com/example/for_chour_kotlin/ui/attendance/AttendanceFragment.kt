package com.example.for_chour_kotlin.ui.attendance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.for_chour_kotlin.R
import com.example.for_chour_kotlin.ServerClass
import com.example.for_chour_kotlin.WriteMDB
import com.example.for_chour_kotlin.databinding.FragmentAttendanceBinding
import com.example.for_chour_kotlin.ui.attendance.AttendanceFragment.MyCustomAdapter




public class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    var serverClass = ServerClass()
    lateinit var writeMDB: WriteMDB
    lateinit var masSpinner: List<String>
    lateinit var masPersson: List<String>
    lateinit var adapter: ArrayAdapter<String>

    lateinit var gridView: GridView
    lateinit var myObjects: ArrayList<GridObject>
    var myAdapter: MyCustomAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        writeMDB = WriteMDB(activity)
        masSpinner = writeMDB.writeSpinerMas()
        masPersson = writeMDB.writePersonMas()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        myObjects = ArrayList<GridObject>()
        for (s in masPersson) {
            myObjects.add(GridObject(s))
        }
        adapter = ArrayAdapter<String>(requireActivity(), R.layout.item, R.id.tvText,masPersson)
        val gridView: GridView = binding.gvMain
        gridView.adapter = adapter;
        /*gridView = binding.gvMain
        myAdapter = MyCustomAdapter(context);
        gridView.setAdapter(myAdapter);*/

        gridView.numColumns = 3

        val itemListener =
            OnItemClickListener { parent, view, position, id ->
                //val l = view.findViewById<LinearLayout>(R.id.styleLayout)
                //l.setBackgroundResource(R.drawable.rect2)
                // var answer: String = serverClass.postRequest(activity, getContext());
                //vivod("-"+position+"-"+id)
                myObjects.get(position).state=1;
                myAdapter?.notifyDataSetChanged()
            }
        gridView.onItemClickListener = itemListener//gridView.setOnClickListener { itemListener }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun vivod(s: String?) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    internal class ViewHolder {
        var layout: LinearLayout? = null
    }
    inner class MyCustomAdapter(context: Context?) : BaseAdapter() {
        private val mInflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView = convertView
            val `object`: GridObject = myObjects.get(position)
            val holder: ViewHolder

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item, null)
                holder = ViewHolder()
                holder.layout = convertView.findViewById<View>(R.id.styleLayout) as LinearLayout
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            if (`object`.state === 1) {
                holder.layout?.setBackgroundResource(R.drawable.rect2)
            } else {
                holder.layout?.setBackgroundColor(R.drawable.rect1)
            }
            return convertView
        }

        override fun getCount(): Int {
            return myObjects.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }

    class GridObject {
        var name: String
        var state: Int = 0
        constructor(name:String) {this.name = name; state=0}
    }
}