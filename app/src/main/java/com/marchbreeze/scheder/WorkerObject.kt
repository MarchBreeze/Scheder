package com.marchbreeze.scheder

class WorkerObject (
    val id: String,
    val pw: String = "",
    val workerName: String = "",
    val storeName: String = "",

    val startYear: String = "2022",
    val startMonth: String = "1",
    val salary: String = "9160",

    val workTime: ArrayList<DayTime> = arrayListOf(),

    val finishSignin: Boolean = false,
    val approval: Boolean = false
)