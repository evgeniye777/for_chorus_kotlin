package com.example.for_chour_kotlin.presentations.fragments.group_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData.appAllGroups.AppAllGroups
import com.example.for_chour_kotlin.data.typeData.appAllGroups.ViewModelAppAllGroups
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.ViewModelAppDataParticipant
import com.example.for_chour_kotlin.databinding.FragmentGroupDetailsBinding
import com.example.for_chour_kotlin.presentations.adapters.for_group_details.NameBasesAdapter
import com.example.for_chour_kotlin.presentations.adapters.for_group_details.ParticipantsAdapter
import com.example.for_chour_kotlin.presentations.adapters.for_group_details.TypeDataAdapter
import kotlin.getValue

class GroupDetailsFragment : Fragment() {

    private var _binding: FragmentGroupDetailsBinding? = null
    private val binding get() = _binding!!

    val groups: ViewModelAppAllGroups by activityViewModels()

    val participant: ViewModelAppDataParticipant by activityViewModels()

    // Адаптеры
    private lateinit var nameBasesAdapter: NameBasesAdapter
    private lateinit var typeDataAdapter: TypeDataAdapter
    private lateinit var participantsAdapter: ParticipantsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация RecyclerView
        initRecyclerViews()

        // Observe LiveData
        observeData()
    }

    private fun initRecyclerViews() {
        // listNameBases
        nameBasesAdapter = NameBasesAdapter()
        binding.rvNameBases.apply {
            adapter = nameBasesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // data
        typeDataAdapter = TypeDataAdapter()
        binding.rvData.apply {
            adapter = typeDataAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // participants
        participantsAdapter = ParticipantsAdapter()
        binding.rvParticipants.apply {
            adapter = participantsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeData() {
        val currentGroup = groups.focus.value
        updateGroupData(currentGroup)

        val currentParticipants = participant.participants.value
        updateParticipantsData(currentParticipants)

        groups.focus.observe(viewLifecycleOwner) { group ->
            updateGroupData(group)
        }

        participant.participants.observe(viewLifecycleOwner) { participants ->
            updateParticipantsData(participants)
        }
    }

    private fun updateGroupData(group: AppAllGroups?) {
        if (group == null) {
            binding.tvGroupName.text = "Нет выбранной группы"
            hideGroupDetails()
            return
        }

        binding.tvGroupName.text = group.name ?: "Без имени"

        binding.tvDateCreate.text = "Дата создания: ${group.date_create ?: "Не указано"}"
        binding.tvLocation.text = "Место: ${group.location ?: "Не указано"}"
        binding.tvCreator.text = "Создатель: ${group.creator ?: "Не указано"}"

        // listNameBases
        nameBasesAdapter.submitList(group.listNameBases)
        val hasNameBases = group.listNameBases.isNotEmpty()
        binding.tvNameBasesTitle.visibility = if (hasNameBases) View.VISIBLE else View.GONE
        binding.rvNameBases.visibility = if (hasNameBases) View.VISIBLE else View.GONE

        // data
        typeDataAdapter.submitList(group.data)
        val hasData = !group.data.isNullOrEmpty()
        binding.tvDataTitle.visibility = if (hasData) View.VISIBLE else View.GONE
        binding.rvData.visibility = if (hasData) View.VISIBLE else View.GONE
    }

    private fun updateParticipantsData(participants: List<AppDataParticipant>?) {
        participantsAdapter.submitList(participants)

        val isEmpty = participants.isNullOrEmpty()
        binding.tvParticipantsTitle.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.rvParticipants.visibility = if (isEmpty) View.GONE else View.VISIBLE

        if (isEmpty) {
            Log.d("GroupDetails", "Нет участников")
        }
    }

    private fun hideGroupDetails() {
        // Скрываем все детали группы, кроме заголовка
        binding.tvDateCreate.visibility = View.GONE
        binding.tvLocation.visibility = View.GONE
        binding.tvCreator.visibility = View.GONE
        binding.tvNameBasesTitle.visibility = View.GONE
        binding.rvNameBases.visibility = View.GONE
        binding.tvDataTitle.visibility = View.GONE
        binding.rvData.visibility = View.GONE
        binding.tvParticipantsTitle.visibility = View.GONE
        binding.rvParticipants.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Очистка binding для избежания leaks
    }
}
