package com.example.auctioninginfoapp.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auctioninginfoapp.R
import com.example.auctioninginfoapp.database.DatabaseModule
import com.example.auctioninginfoapp.databinding.FragmentRegisterBinding
import com.example.auctioninginfoapp.databinding.FragmentResultBinding
import com.example.auctioninginfoapp.model.Fruits


class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    val database by lazy {
        DatabaseModule.getDatabase(requireContext())
    }

    val resultViewModel by lazy {
        ViewModelProvider(this).get(ResultViewModel::class.java)
    }

    val resultAdapter = ResultAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectFruit = arguments?.getString("SELECT_FRUIT")
        val selectDate = arguments?.getString("SELECT_DATE")
        val resultAmount = arguments?.getString("RESULT_AMOUNT")

        if(selectDate != null && selectFruit != null && resultAmount != null){
            resultViewModel.loadDataFromURL(selectDate, selectFruit, resultAmount)

            resultViewModel.getResultList().observe(viewLifecycleOwner, Observer {
                resultAdapter.freshList = it
                Log.i("FRESH", "it: $it")

                resultAdapter.notifyDataSetChanged()

                if(it.isNotEmpty()){
                    /** 로딩은 사라지고 */
                    binding.progressLoader.visibility = View.GONE
                }



                /** 저장버튼을 보여주기 */
                binding.flotingSave.visibility = View.VISIBLE
            })

            resultViewModel.notificationMsg.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                binding.progressLoader.visibility = View.GONE
            })

            binding.recycleReesult.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            binding.recycleReesult.adapter = resultAdapter
            binding.recycleReesult.layoutManager = LinearLayoutManager(requireContext())
            binding.flotingSave.setOnClickListener {
                if(resultViewModel.getResultList().value.isNullOrEmpty()){
                    Toast.makeText(requireContext(), "저장할 데이터가 없습니다.", Toast.LENGTH_LONG).show()
                }else {
                    resultViewModel.saveResult(
                        requireContext(),
                        "${selectDate} ${Fruits.valueOf(selectFruit).holder} 검색결과"
                    )
                    Toast.makeText(requireContext(), "데이터가 저장되었습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}