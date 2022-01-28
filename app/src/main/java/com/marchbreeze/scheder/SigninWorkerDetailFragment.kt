package com.marchbreeze.scheder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninWorkerDetailBinding

class SigninWorkerDetailFragment : Fragment() {

    lateinit var binding: FragmentSigninWorkerDetailBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String
    private lateinit var timeList: MutableList<String>
    private lateinit var storeId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninWorkerDetailBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninWorkerDetailFragment")

        currentId = arguments?.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

        // DB에 등록
        binding.btnSetDetail.setOnClickListener {

            // 이름 등록
            val name = binding.edittextName.text.toString()
            db.collection("worker")
                .document(currentId)
                .update("workerName", name)

            // 관리자에 등록된 상호명인지 확인
            val storeName = eraseBlank(binding.edittextStorename.text.toString())

            db.collection("owner")
                .whereEqualTo("storeName", storeName)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        Log.d("SIGNIN", "Acceptable Store Name")

                        // 직원 정보에 가게 업데이트
                        db.collection("worker")
                            .document(currentId)
                            .update("storeName", storeName)

                        // 해당 가게의 시간 리스트 & 아이디 가져오기
                        for (document in documents) {
                            timeList = document.get("timeList") as MutableList<String>
                            Log.d("SIGNIN", "timeList: $timeList")
                            storeId = document.get("id").toString() + "_1"
                            Log.d("SIGNIN", "storeId: $storeId")
                        }



                        // 다음 회원가입 페이지로 전환
                        (activity as SigninActivity).replaceFragmentWithIdListId(
                            SigninWorkerWorkFragment(),
                            currentId,
                            timeList,
                            storeId
                        )
                        Log.d("SIGNIN", "Set SigninWorkerWorkFragment (currentId : $currentId)")

                    } else {
                        // 가게명이 등록되지 않은 경우
                        Log.d("SIGNIN", "Unverificated Store Name")
                        binding.warningName.text = "등록되지 않은 가게명이에요 :("
                        binding.warningName.setTextColor(Color.RED)
                    }
                }.addOnFailureListener { e ->
                    Log.d("SIGNIN", "btnSetDetail failure ($e)")
                }
        }
        return binding.root
    }

    // 해당 문자열의 빈칸 자르기
    private fun eraseBlank(string: String): String {
        return string.replace("\\s".toRegex(), "")
    }
}