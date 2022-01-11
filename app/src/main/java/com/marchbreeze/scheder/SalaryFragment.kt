package com.marchbreeze.scheder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.marchbreeze.scheder.databinding.FragmentSalaryBinding

class SalaryFragment : Fragment() {
    lateinit var binding: FragmentSalaryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalaryBinding.inflate(inflater, container, false)

        // 가상 데이터 설정
        val datas = mutableListOf<String>("김알바", "박주방", "최직원", "장오픈", "임미들")

        binding.workerRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.workerRecyclerView.adapter = MainRecyclerViewAdapter(datas)
        return binding.root
    }
}