package com.marchbreeze.scheder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninWorkerGuideBinding
import com.marchbreeze.scheder.databinding.FragmentSigninWorkerWorkBinding

class SigninWorkerGuideFragment : Fragment() {

    lateinit var binding: FragmentSigninWorkerGuideBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninWorkerGuideBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninWorkerFragment")

        currentId = arguments?.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

// TODO 직원 승인 기능 만들기 (Owner Request 완성 후) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // 요청 재전송 버튼
        binding.btnResendRequest.setOnClickListener {

        }
        return binding.root
    }
}