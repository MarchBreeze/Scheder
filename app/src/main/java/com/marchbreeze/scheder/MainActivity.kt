package com.marchbreeze.scheder

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.marchbreeze.scheder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        window.statusBarColor = Color.parseColor("#FF3C3D59")

        // 뷰 페이져에 어댑터 연결
        val adapter = MainFragmentAdapter(this)
        binding.viewPager.adapter = adapter

        // 탭 설정
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        // 탭 레이아웃과 뷰 페이져 연동
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.icon = getDrawable(R.drawable.icon_person)
                1 -> tab.icon = getDrawable(R.drawable.icon_calendar)
                2 -> tab.icon = getDrawable(R.drawable.icon_request)
                3 -> tab.icon = getDrawable(R.drawable.icon_check)
            }
        }.attach()
    }


}