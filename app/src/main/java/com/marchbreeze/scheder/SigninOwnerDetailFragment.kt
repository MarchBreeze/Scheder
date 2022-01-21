package com.marchbreeze.scheder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninOwnerDetailBinding

class SigninOwnerDetailFragment : Fragment() {

    lateinit var binding: FragmentSigninOwnerDetailBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninOwnerDetailBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninOwnerFragment")

        currentId = arguments?.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")


        // DB에 등록
        binding.btnSetOwner.setOnClickListener {

            // 중복된 상호명인지 확인
            db.collection("owner")
                .get()
                .addOnSuccessListener { documents ->
                    val storeName = eraseBlank(binding.edittextStorename.text.toString())
                    val ownerName = eraseBlank(binding.edittextOwner.text.toString())

                    loop@ for (document in documents) {
                        // 가게명 중복 확인
                        if (document.get("storeName") == storeName) {
                            Log.d("SIGNIN", "store name already exist")
                            Toast.makeText(activity, "이미 존재하는 상호명입니다", Toast.LENGTH_SHORT).show()
                            binding.warningStore.text = "이미 존재하는 상호명입니다"
                            binding.warningStore.setTextColor(Color.RED)

                            break@loop
                        }

                        // 사용 가능한 가게명
                        Log.d("SIGNIN", "Acceptable Store Name")

                        if (binding.warningStore.text == "이미 존재하는 상호명입니다") {
                            binding.warningStore.text = ""
                        }

                        // DB에 저장
                        db.collection("owner")
                            .document(currentId)
                            .update(
                                mapOf(
                                    "storeName" to storeName,
                                    "ownerName" to ownerName
                                )
                            )
                        Log.d("SIGNIN", "storeName : $storeName")
                        Log.d("SIGNIN", "ownerName : $ownerName")

                        // 다음 회원가입 페이지로 전환
                        (activity as SigninActivity).replaceFragmentWithId(
                            SigninOwnerTimeListFragment(),
                            currentId
                        )
                        Log.d("SIGNIN", "Set SigninTimetableFragment (currentId : $currentId)")
                    }
                }.addOnFailureListener { e ->
                    Log.d("SIGNIN", "btnSetOwner failure ($e)")
                }
        }
        return binding.root
    }

    // 해당 문자열의 빈칸 자르기
    private fun eraseBlank(string: String): String {
        return string.replace("\\s".toRegex(), "")
    }
}