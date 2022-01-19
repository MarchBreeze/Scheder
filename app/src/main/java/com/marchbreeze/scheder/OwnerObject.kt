package com.marchbreeze.scheder

class OwnerObject (
    val id: String,
    val pw: String = "",
    val storeName: String = "",
    val ownerName: String = "",
    val totalTime: Int = 0,
    val timeList: MutableList<String> = mutableListOf(),
    val unbusyNumberList: MutableList<String> = mutableListOf(),
    val busyNumberList: MutableList<String> = mutableListOf(),
    val openTime: MutableMap<String, Int> = mutableMapOf(
        "hour" to 8,
        "minute" to 0
    ),
    val timeDurationHour: MutableList<String> = mutableListOf(),
    val timeDurationMinute: MutableList<String> = mutableListOf(),
    val finishSignin: Boolean = false
)