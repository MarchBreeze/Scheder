package com.marchbreeze.scheder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    val fragments: List<Fragment> =
        listOf(SalaryFragment(), CalendarFragment(), RequestFragment(), AnnouncementFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SalaryFragment()
            1 -> CalendarFragment()
            2 -> RequestFragment()
            else -> AnnouncementFragment()
        }
    }
}