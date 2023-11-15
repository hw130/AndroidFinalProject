package com.example.auctioninginfoapp.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.auctioninginfoapp.R
import com.example.auctioninginfoapp.database.DatabaseModule
import com.example.auctioninginfoapp.databinding.FragmentSaveBinding
import com.example.auctioninginfoapp.result.SaveAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SaveFragment : Fragment() {
    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!

    private val database by lazy {
        DatabaseModule.getDatabase(requireContext())
    }

    private val saveAdapter by lazy { SaveAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listSave.layoutManager = LinearLayoutManager(requireContext())
        binding.listSave.adapter = saveAdapter

        arguments?.getLong("SAVE_ID")?.let { saveId ->
            val pagingData = Pager(
                PagingConfig(20), 1,
                database.freshDao().loadFreshData(saveId = saveId).asPagingSourceFactory(Dispatchers.IO)
            )

            pagingData.liveData.observe(viewLifecycleOwner){
                lifecycleScope.launch {
                    saveAdapter.submitData(it)
                }
            }
        }
    }
   
}