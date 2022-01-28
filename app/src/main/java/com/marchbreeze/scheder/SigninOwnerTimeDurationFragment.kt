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
import com.kizitonwose.time.Interval
import com.kizitonwose.time.Minute
import com.kizitonwose.time.hours
import com.kizitonwose.time.minutes
import com.marchbreeze.scheder.databinding.FragmentSigninOwnerTimeDurationBinding
import kotlin.collections.mutableListOf as mutableListOf

class SigninOwnerTimeDurationFragment : Fragment() {
    lateinit var binding: FragmentSigninOwnerTimeDurationBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String
    private lateinit var timeList: MutableList<String>

    val startHour: MutableList<String> = mutableListOf()
    val startMinute: MutableList<String> = mutableListOf()
    val endHour: MutableList<String> = mutableListOf()
    val endMinute: MutableList<String> = mutableListOf()

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
            startHour.add((6+3*i).toString())
        }
        for (i in 1..timeList.size) {
            startMinute.add("0")
        }
        for (i in 1..timeList.size) {
            endHour.add((9+3*i).toString())
        }
        for (i in 1..timeList.size) {
            endMinute.add("0")
        }


        binding.recyclerviewTime.layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewTime.adapter =
            SigninOwnerTimeDurationAdapter(timeList, currentId, startHour, startMinute, endHour, endMinute)



        binding.btnSetDivision.setOnClickListener {
            // 회원가입 성공적 표시
            db.collection("owner")
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
