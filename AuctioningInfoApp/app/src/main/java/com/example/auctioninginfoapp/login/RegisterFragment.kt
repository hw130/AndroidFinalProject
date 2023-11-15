package com.example.auctioninginfoapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.auctioninginfoapp.R
import com.example.auctioninginfoapp.databinding.FragmentRegisterBinding
import com.example.auctioninginfoapp.util.hideKeyboard
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding ?= null
    private val binding get() = _binding!!

    val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** 가입하기 버튼 클릭 */
        binding.btnRegister.setOnClickListener {
            val id = binding.fieldRegisterId.text.toString()
            val pw = binding.fieldRegisterPw.text.toString()
            val pw_confirm = binding.fieldRegisterPwConfirm.text.toString()

            when {
                id.isEmpty() ->
                    Toast.makeText(requireContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG).show()
                pw.isEmpty() ->
                    Toast.makeText(requireContext(), "패스워드를 입력하세요.", Toast.LENGTH_LONG).show()
                pw_confirm != pw ->
                    Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                else -> {
                    //id, pw, pw_confirm이 정상이면
                    binding.registerLoader.visibility = View.VISIBLE

                    firebaseAuth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener{task ->
                            //성공한 경우
                            task.addOnSuccessListener {
                                binding.fieldRegisterId.text = null
                                binding.fieldRegisterPw.text = null
                                binding.fieldRegisterPwConfirm.text = null

                                hideKeyboard()
                                findNavController().navigate(R.id.action_global_searchFragment)
                            }
                            /* 실패한 경우 */
                            task.addOnFailureListener {
                                binding.registerLoader.visibility = View.GONE
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}