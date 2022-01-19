package com.marchbreeze.scheder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninOwnerGuideBinding

class SigninOwnerGuideFragment : Fragment() {
    lateinit var binding: FragmentSigninOwnerGuideBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninOwnerGuideBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninOwnerGuideFragment")

        currentId = arguments!!.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

        db.collection("worker").document(currentId).get().addOnSuccessListener { document ->
            val storeName = document.get("storeName").toString()
            binding.textStoreName.text = "가게명: $storeName"
            Log.d("SIGNIN", "Set StoreName")
        }

        // TODO 유저 회원가입 완성하고 여기에 이미지 뷰 GLIDE 로 집어넣기 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        binding.btnFinishSignin.setOnClickListener {
            Log.d("SIGNIN", "Finish Signin")
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

}