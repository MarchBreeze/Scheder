package com.marchbreeze.scheder

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.marchbreeze.scheder.databinding.FragmentOwnerSchedBinding

class OwnerSchedFragment : Fragment() {
    lateinit var binding: FragmentOwnerSchedBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerSchedBinding.inflate(inflater, container, false)
        return binding.root
    }
}

//// 테이블 동적 생성
//val tableLayout = binding.tableMain
//tableLayout.isStretchAllColumns = true
//tableLayout.gravity = Gravity.CENTER
//
//val tbrow0 = TableRow(activity)
//
//val tv0 = TextView(activity)
//val tv1 = TextView(activity)
//val tv2 = TextView(activity)
//val tv3 = TextView(activity)
//val tv4 = TextView(activity)
//val tv5 = TextView(activity)
//val tv6 = TextView(activity)
//
//tv0.text = "월요일"
//tv1.text = "화요일"
//tv2.text = "수요일"
//tv3.text = "목요일"
//tv4.text = "금요일"
//tv5.text = "토요일"
//tv6.text = "일요일"
//
//setTableRow(tv0, tbrow0)
//setTableRow(tv1, tbrow0)
//setTableRow(tv2, tbrow0)
//setTableRow(tv3, tbrow0)
//setTableRow(tv4, tbrow0)
//setTableRow(tv5, tbrow0)
//setTableRow(tv6, tbrow0)
//
//tableLayout.addView(tbrow0)
//
//
//for (i in 1 .. timeList.size) {
//    val tbrow = TableRow(activity)
//
//    val t1v = TextView(activity)
//    val t2v = TextView(activity)
//    val t3v = TextView(activity)
//    val t4v = TextView(activity)
//    val t5v = TextView(activity)
//    val t6v = TextView(activity)
//    val t7v = TextView(activity)
//
//    t1v.text = "$i"
//    t2v.text = "$i"
//    t3v.text = "$i"
//    t4v.text = "$i"
//    t5v.text = "$i"
//    t6v.text = "$i"
//    t7v.text = "$i"
//
//    setTableElement(t1v, tbrow)
//    setTableElement(t2v, tbrow)
//    setTableElement(t3v, tbrow)
//    setTableElement(t4v, tbrow)
//    setTableElement(t5v, tbrow)
//    setTableElement(t6v, tbrow)
//    setTableElement(t7v, tbrow)
//
//    tableLayout.addView(tbrow)

private fun setTableRow(tv: TextView, tbrow: TableRow) {
    tv.setTextColor(Color.BLACK)
    tv.textSize = 16F
    tv.fontFeatureSettings = "@font/ssurround_air"
    tbrow.addView(tv)
}

private fun setTableElement(tv: TextView, tbrow: TableRow) {
    tv.setTextColor(Color.GREEN)
    tv.gravity = Gravity.CENTER
    tbrow.addView(tv)
}



