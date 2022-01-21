package com.marchbreeze.scheder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninOwnerTimeListBinding

class SigninOwnerTimeListFragment : Fragment() {
    lateinit var binding: FragmentSigninOwnerTimeListBinding

    var timeList: MutableList<String> = mutableListOf()
    var unbusyNumberList: MutableList<String> = mutableListOf()
    var busyNumberList: MutableList<String> = mutableListOf()
    var totalTime: Int = 0

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninOwnerTimeListBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninTimetableFragment")

        currentId = arguments?.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

        // 비밀번호 등록
        binding.btnSetTime.setOnClickListener {

            // 변수 설정
            timeList = makeMutableList(binding.edittextTime.text.toString())
            unbusyNumberList = makeMutableList(binding.edittextUnbusy.text.toString())
            busyNumberList = makeMutableList(binding.edittextBusy.text.toString())
            totalTime = timeList.size

            if ((timeList.size == unbusyNumberList.size) and (timeList.size == busyNumberList.size) and (busyNumberList.size != 0)) {
                // DB에 추가
                db.collection("owner")
                    .document(currentId)
                    .update(
                        mapOf(
                            "totalTime" to totalTime,
                            "timeList" to timeList,
                            "unbusyNumberList" to unbusyNumberList,
                            "busyNumberList" to busyNumberList
                        )
                    )
                Log.d("SIGNIN", "totalTime : $totalTime")
                Log.d("SIGNIN", "timeList : $timeList")
                Log.d("SIGNIN", "unbusyNumberList : $unbusyNumberList")
                Log.d("SIGNIN", "busyNumberList : $busyNumberList")

                // 다음 회원가입 페이지로 전환
                if (totalTime != 1) {
                    (activity as SigninActivity).replaceFragmentWithIdList(
                        SigninOwnerTimeDurationFragment(),
                        currentId,
                        timeList
                    )
                    Log.d("SIGNIN", "Set SigninOwnerTimeDivisionFragment (currentId : $currentId)")
                } else {
                    Toast.makeText(activity, "타임을 구분해주세요", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(activity, "타임 개수가 일치한지 다시 한 번 확인해주세요", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    // 입력된 값 리스트로 생성
    private fun makeMutableList(string: String): MutableList<String> {
        return string.replace("\\s".toRegex(), "").split(',').toMutableList()
    }
}