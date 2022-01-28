package com.marchbreeze.scheder

import android.app.TimePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.ItemTimeDurationBinding

class SigninOwnerTimeDurationAdapter(
    private var timeList: MutableList<String>,
    var currentId: String,
    val startHour: MutableList<String> = mutableListOf(),
    val startMinute: MutableList<String> = mutableListOf(),
    val endHour: MutableList<String> = mutableListOf(),
    val endMinute: MutableList<String> = mutableListOf()
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

        val startHour00 = mutableListOf("09", "12", "15", "18", "21", "24", "27", "30")
        val endHour00 = mutableListOf("12", "15", "18", "21", "24", "27", "30", "33")
        binding.btnStartTime.text = "${startHour00[position]} : 00"
        binding.btnEndTime.text = "${endHour00[position]} : 00"

        binding.btnStartTime.setOnClickListener {
            TimePickerDialog(
                binding.root.context,
                R.style.TimePickerTheme,
                { _, hour, minute ->

                    startHour[position] = hour.toString()
                    startMinute[position] = minute.toString()

                    binding.btnStartTime.text =
                        "${makeDoubleDigit(hour.toString())}:${makeDoubleDigit(minute.toString())}"
                    Log.d("SIGNIN", "${timeList[position]} Start: ${hour}:${minute}")


                    db.collection("owner")
                        .document(currentId)
                        .update("startHour", startHour)
                    Log.d("SIGNIN", "startHour : $startHour")

                    db.collection("owner")
                        .document(currentId)
                        .update("startMinute", startMinute)
                    Log.d("SIGNIN", "startMinute : $startMinute")

                }, startHour[position].toInt(), startMinute[position].toInt(), true
            ).show()
        }

        binding.btnEndTime.setOnClickListener {
            TimePickerDialog(
                binding.root.context,
                R.style.TimePickerTheme,
                { _, hour, minute ->

                    endHour[position] = hour.toString()
                    endMinute[position] = minute.toString()

                    binding.btnEndTime.text =
                        "${makeDoubleDigit(hour.toString())}:${makeDoubleDigit(minute.toString())}"
                    Log.d("SIGNIN", "${timeList[position]} End: ${hour}:${minute}")


                    db.collection("owner")
                        .document(currentId)
                        .update("endHour", endHour)
                    Log.d("SIGNIN", "endHour : $endHour")

                    db.collection("owner")
                        .document(currentId)
                        .update("endMinute", endMinute)
                    Log.d("SIGNIN", "endMinute : $endMinute")

                }, endHour[position].toInt(), endMinute[position].toInt(), true
            ).show()
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

