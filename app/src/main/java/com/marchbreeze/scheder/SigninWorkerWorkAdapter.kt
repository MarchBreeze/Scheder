package com.marchbreeze.scheder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.ItemWorktimeBinding

class SigninWorkerWorkAdapter(
    private val timeList: MutableList<String>,
    var currentId: String,
    private val data: List<DayTime>,
    private val storeId: String,
    val onClickDeleteIcon: (daytime: DayTime) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    inner class SigninWorkerWorkViewHolder(val binding: ItemWorktimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = SigninWorkerWorkViewHolder(
        ItemWorktimeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SigninWorkerWorkViewHolder).binding

        // 아이템에 요일 타임 표시
        val listPosition = data[position]
        binding.day.text = listPosition.day
        binding.time.text = listPosition.time
        Log.d("SIGNIN", "set day: ${listPosition.day} & time: ${listPosition.time}")

        // 시간 표시
        val index = timeList.indexOf(listPosition.time)
        Log.d("SIGNIN", "index: $index")


        // 가게 시간 찾기
        db.collection("owner")
            .document(storeId)
            .get().addOnSuccessListener { document ->
                Log.d("SIGNIN", "Found owner document for StartEndTime")

                val startEndTime = document.toObject(StartEndTime::class.java)
                Log.d("SIGNIN", "startEndTime Object: $startEndTime")

                if (startEndTime != null) {
                    binding.startTime.text =
                        makeDoubleDigit(startEndTime.startHour[index]) +
                                ":" + makeDoubleDigit(startEndTime.startMinute[index])
                    Log.d("SIGNIN", "startTime: ${binding.startTime.text}")

                    binding.endTime.text =
                        makeDoubleDigit(startEndTime.endHour[index]) +
                                ":" + makeDoubleDigit(startEndTime.endMinute[index])
                    Log.d("SIGNIN", "endTime: ${binding.endTime.text}")


                } else {
                    Log.d("SIGNIN", "startEndTime Object is null")
                }
            }.addOnFailureListener {
                Log.d("SIGNIN", "falied to find owner document ($it)")
            }

        // 삭제 버튼
        binding.trash.setOnClickListener {
            onClickDeleteIcon.invoke(listPosition)
            Log.d("SIGNIN", "Pushed Delete Btn: $listPosition")
        }
    }

    private fun makeDoubleDigit(number: String): String {
        return if (number.toInt() < 10) {
            "0$number"
        } else {
            number
        }
    }
}