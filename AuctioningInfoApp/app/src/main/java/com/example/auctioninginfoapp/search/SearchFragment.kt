package com.example.auctioninginfoapp.search

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auctioninginfoapp.R
import com.example.auctioninginfoapp.database.DatabaseModule
import com.example.auctioninginfoapp.databinding.FragmentSearchBinding
import com.example.auctioninginfoapp.model.Fruits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    var selectedFruit: String? = null
    var selectedDate: String? = null

    private val database by lazy {
        DatabaseModule.getDatabase(requireContext())
    }

    private val searchAdapter by lazy {
        SearchAdapter(database.freshDao())
    }

    private val alertDialog by lazy {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("농산물을 선택해주세요.")

        builder.setItems(Fruits.values().map { it.holder }.toTypedArray()){ _, index ->
            with(Fruits.values()[index]){
                selectedFruit = this.name

                binding.textType.text = this.holder
            }

            Log.i("FRESH", "which: $index - $selectedFruit")

            checkCondition()
        }

        builder.setNegativeButton("취소",null)

        builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DividerItemDecoration(
            requireContext(),
            LinearLayoutManager(requireContext()).orientation
        ).run {
            binding.listSearch.addItemDecoration(this)
        }

        selectedFruit = null
        selectedDate = null

        binding.listSearch.adapter = searchAdapter
        binding.listSearch.layoutManager = LinearLayoutManager(requireContext())

        binding.layoutType.setOnClickListener { alertDialog.show() }

        binding.layoutDate.setOnClickListener {
            val currentCalendar = Calendar.getInstance().apply { time = Date(System.currentTimeMillis()) }

            DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener{_, year, month, dayOfMonth ->
                    currentCalendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }.run {
                        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(currentCalendar.time)
                        changeInputTextBydate()
                    }
                },
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        val pageLiveData = Pager(
            PagingConfig(20), 1,
            database.freshDao().loadSaveItems().asPagingSourceFactory(Dispatchers.IO)).liveData
        pageLiveData.observe(viewLifecycleOwner){
            lifecycleScope.launch { searchAdapter.submitData(it) }
        }

        binding.btnSearch.setOnClickListener {
            if(selectedDate == null || selectedFruit == null){
                Toast.makeText(requireContext(), "분류와 날짜를 입력해주세요.", Toast.LENGTH_LONG).show()
            } else {
                Log.i("SELECT_DATE", selectedDate!!)
                Log.i("SELECT_FRUIT", selectedFruit!!)

                findNavController().navigate(
                    R.id.action_searchFragment_to_resultFragment,
                    Bundle().apply {
                        putString("SELECT_FRUIT", selectedFruit)
                        putString("SELECT_DATE", selectedDate)
                        putString(
                            "RESULT_AMOUNT",
                            view.findViewById<RadioButton>(binding.radioLayout.checkedRadioButtonId).tag.toString()
                        )
                    }
                )
            }
        }


    }
    private fun changeInputTextBydate(){
        checkCondition()
        selectedDate?.let { binding.txtGongpan.text = it }
    }

    private fun checkCondition(){
        if(selectedDate != null && selectedFruit != null){
            binding.btnSearch.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
            binding.btnSearch.setTextColor(ResourcesCompat.getColor(resources, android.R.color.white, null))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}