package com.marchbreeze.scheder

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninOwnerTimeDurationBinding

class SigninOwnerTimeDurationFragment : Fragment() {
    lateinit var binding: FragmentSigninOwnerTimeDurationBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String
    private lateinit var timeList: MutableList<String>

    private var timeDurationHour: MutableList<String> = mutableListOf()
    private var timeDurationMinute: MutableList<String> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninOwnerTimeDurationBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninTimeDurationFragment")

        currentId = arguments!!.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

        timeList = arguments!!.getStringArray("timeList")!!.toMutableList()
        Log.d("SIGNIN", "timeList : $timeList")


        // edittext 값 입력 받을 timeduration 리스트 미리 생성 (칸 개수 맞춰서)
        for (i in 1..timeList.size) {
            timeDurationHour.add("4")
        }
        for (i in 1..timeList.size) {
            timeDurationMinute.add("0")
        }


        // 오픈 시간 타임피커로 설정
        binding.btnOpentime.setOnClickListener {
            TimePickerDialog(activity, { _, hour, minute ->

                val openTime = mutableMapOf<String, Int>(
                    "hour" to hour,
                    "minute" to minute
                )
                db.collection("worker")
                    .document(currentId)
                    .update("openTime", openTime)

                if (hour >= 10) {
                    if (minute >= 10) {
                        binding.btnOpentime.text = "${hour}시 ${minute}분"
                        Log.d("SIGNIN", "openTime: ${hour}시 ${minute}분")
                    } else {
                        binding.btnOpentime.text = "${hour}시 0${minute}분"
                        Log.d("SIGNIN", "openTime: ${hour}시 0${minute}분")
                    }
                } else {
                    if (minute >= 10) {
                        binding.btnOpentime.text = "0${hour}시 ${minute}분"
                        Log.d("SIGNIN", "openTime: 0${hour}시 ${minute}분")
                    } else {
                        binding.btnOpentime.text = "0${hour}시 0${minute}분"
                        Log.d("SIGNIN", "openTime: 0${hour}시 0${minute}분")
                    }
                }
            }, 8, 0, true).show()
        }


        binding.recyclerviewTime.layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewTime.adapter =
            SigninTimeDurationAdapter(timeList, timeDurationHour, timeDurationMinute, currentId)

        binding.btnSetDivision.setOnClickListener {
            // 회원가입 성공적 표시
            db.collection("worker")
                .document(currentId)
                .update("finishSignin", true)

            // 다음 회원가입 페이지로 전환
            (activity as SigninActivity).replaceFragmentWithId(
                SigninOwnerGuideFragment(),
                currentId
            )
            Log.d("SIGNIN", "Set SigninOwnerTimeDivisionFragment (currentId : $currentId)")
        }

        return binding.root
    }
}
