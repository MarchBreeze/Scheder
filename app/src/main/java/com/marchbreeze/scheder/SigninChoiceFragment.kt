package com.marchbreeze.scheder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.marchbreeze.scheder.databinding.FragmentSigninChoiceBinding

class SigninChoiceFragment : Fragment() {
    lateinit var binding: FragmentSigninChoiceBinding
    private lateinit var callback : OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SIGNIN", "onCreateView SigninChoiceFragment")
        binding = FragmentSigninChoiceBinding.inflate(inflater, container, false)

        binding.selectOwner.setOnClickListener {
            // 관리자 회원가입 페이지로 전환
            (activity as SigninActivity).replaceFragment(SigninOwnerAuthFragment())
            Log.d("SIGNIN", "Set SigninOwnerAuthFragment")
        }

        binding.selectWorker.setOnClickListener {
            // 직원 회원가입 페이지로 전환
            (activity as SigninActivity).replaceFragment(SigninWorkerAuthFragment())
            Log.d("SIGNIN", "Set SigninWorkerAuthFragment")
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 해당 프래그먼트 표시된 액티비티에서 뒤로가기 버튼 눌렀을 때
                startActivity(Intent(activity, FirstActivity::class.java))
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}