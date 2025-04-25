package com.example.for_chour_kotlin.presentations.fragments.statisticSongs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.for_chour_kotlin.databinding.FragmentStatisticsongsBinding

class StatisticSongFragment : Fragment() {

    private var _binding: FragmentStatisticsongsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatisticsongsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
            textView.text = "СТатистика Песен"
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}