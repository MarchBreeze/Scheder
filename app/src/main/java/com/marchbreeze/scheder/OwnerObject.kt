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

    val startHour: MutableList<String> = mutableListOf(),
    val startMinute: MutableList<String> = mutableListOf(),
    val endHour: MutableList<String> = mutableListOf(),
    val endMinute: MutableList<String> = mutableListOf(),

    val finishSignin: Boolean = false
)