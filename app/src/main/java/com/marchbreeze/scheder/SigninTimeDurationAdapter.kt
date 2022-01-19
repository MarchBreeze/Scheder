package com.marchbreeze.scheder

import android.app.TimePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.ItemTimeDurationBinding

class SigninTimeDurationAdapter(
    private var timeList: MutableList<String>,
    var timeDurationHour: MutableList<String>,
    var timeDurationMinute: MutableList<String>,
    var currentId: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    inner class SigninTimeDurationViewHolder(val binding: ItemTimeDurationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = timeList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = SigninTimeDurationViewHolder(
        ItemTimeDurationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SigninTimeDurationViewHolder).binding
        binding.time.text = timeList[position]

        binding.btnSetDuration.setOnClickListener {
            TimePickerDialog(binding.root.context, { _, hour, minute ->

                timeDurationHour[position] = hour.toString()
                timeDurationMinute[position] = minute.toString()

                if (minute >= 10) {
                    binding.btnSetDuration.text = "${hour}시간 ${minute}분"
                    Log.d("SIGNIN", "${timeList[position]} : ${hour}시간 ${minute}분")
                } else {
                    binding.btnSetDuration.text = "${hour}시간 0${minute}분"
                    Log.d("SIGNIN", "${timeList[position]} : ${hour}시간 0${minute}분")
                }

                db.collection("worker")
                    .document(currentId)
                    .update("timeDurationHour", timeDurationHour)
                Log.d("SIGNIN", "timeDurationHour : $timeDurationHour")

                db.collection("worker")
                    .document(currentId)
                    .update("timeDurationMinute", timeDurationMinute)
                Log.d("SIGNIN", "timeDurationMinute : $timeDurationMinute")

            }, timeDurationHour[position].toInt(), timeDurationMinute[position].toInt(), true).show()
        }

    }


}
