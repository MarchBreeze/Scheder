package com.marchbreeze.scheder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marchbreeze.scheder.databinding.FragmentSigninChoiceBinding

class SigninChoiceFragment : Fragment() {
    lateinit var binding: FragmentSigninChoiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SIGNIN", "onCreateView SigninChoiceFragment")
        binding = FragmentSigninChoiceBinding.inflate(inflater, container, false)

        binding.selectOwner.setOnClickListener {
            Log.d("SIGNIN", "selected owner signin")

            // 관리자 회원가입 페이지로 전환
            (activity as SigninActivity).replaceFragment(SigninOwnerAuthFragment())
            Log.d("SIGNIN", "Set SigninOwnerAuthFragment")
        }

        binding.selectWorker.setOnClickListener {
            Log.d("SIGNIN", "selected worker signin")

            // 직원 회원가입 페이지로 전환
            (activity as SigninActivity).replaceFragment(SigninWorkerAuthFragment())
            Log.d("SIGNIN", "Set SigninWorkerAuthFragment")
        }


        return binding.root
    }
}