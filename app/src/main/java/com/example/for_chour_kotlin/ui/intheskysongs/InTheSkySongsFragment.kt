package com.example.for_chour_kotlin.ui.intheskysongs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.for_chour_kotlin.databinding.FragmentIntheskysongsBinding

class InTheSkySongsFragment : Fragment() {

    private var _binding: FragmentIntheskysongsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntheskysongsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
            textView.text = "В Небо Песни"
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}