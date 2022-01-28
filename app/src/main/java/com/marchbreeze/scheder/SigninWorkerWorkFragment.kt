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
import com.marchbreeze.scheder.databinding.FragmentSigninWorkerWorkBinding

class SigninWorkerWorkFragment : Fragment() {

    lateinit var binding: FragmentSigninWorkerWorkBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String
    private lateinit var timeList: MutableList<String>
    private lateinit var storeId: String

    private val data = arrayListOf<DayTime>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninWorkerWorkBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninWorkerFragment")

        currentId = arguments?.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

        timeList = arguments?.getStringArray("timeList")!!.toMutableList()
        Log.d("SIGNIN", "timeList : $timeList")

        storeId = arguments?.getString("storeId").toString()
        Log.d("SIGNIN", "storeId : $storeId")


        // 스피너 설정
        val spinnerDay = binding.spinnerDay
        val spinnerTime = binding.spinnerTime

        spinnerDay.adapter = ArrayAdapter(
            activity as SigninActivity,
            R.layout.spinner_text,
            resources.getStringArray(R.array.dayItemList)
        )
        spinnerTime.adapter =
            ArrayAdapter(activity as SigninActivity, R.layout.spinner_text, timeList)

        Log.d("SIGNIN", "Set spinner day & time")


        // 리사이클러뷰 설정
        binding.recyclerviewWork.layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewWork.adapter =
            SigninWorkerWorkAdapter(timeList, currentId, data, storeId, onClickDeleteIcon = {
                // 리사이클러뷰에서 삭제
                data.remove(it)
                binding.recyclerviewWork.adapter?.notifyDataSetChanged()
                Log.d("SIGNIN", "Remove item : $it")
            })

        Log.d("SIGNIN", "set recyclerView with adapter")


        // 리사이클러뷰에 추가
        binding.btnAdd.setOnClickListener {
            val dayTime = DayTime(spinnerDay.selectedItem.toString(), spinnerTime.selectedItem.toString())
            data.add(dayTime)
            binding.recyclerviewWork.adapter?.notifyDataSetChanged()
            Log.d("SIGNIN", "Add spinner data to item : $dayTime")
        }


        binding.btnSet.setOnClickListener {
            // 근무 시작 년월 & 시급 설정
            val startYear = binding.edittextYear.text.toString()
            val startMonth = binding.edittextMonth.text.toString()
            val salary = binding.edittextSalary.text.toString()

            db.collection("worker")
                .document(currentId)
                .update(
                    mapOf(
                        "startYear" to startYear,
                        "startMonth" to startMonth,
                        "salary" to salary
                    )
                )
            Log.d("SIGNIN", "year: $startYear, month: $startMonth")
            Log.d("SIGNIN", "salary: $salary")


            // data를 db에 저장
            db.collection("worker")
                .document(currentId)
                .update("workTime", data)
            Log.d("SIGNIN", "workTime: $data")


            // 회원가입 성공적 표시
            db.collection("worker")
                .document(currentId)
                .update("finishSignin", true)
            Log.d("SIGNIN", "Finished Signin")

            // 다음 액티비티로
            (activity as SigninActivity).replaceFragmentWithId(
                SigninWorkerGuideFragment(),
                currentId
            )
            Log.d("SIGNIN", "Set SigninWorkerGuideFragment (currentId : $currentId)")
        }


        return binding.root
    }
}