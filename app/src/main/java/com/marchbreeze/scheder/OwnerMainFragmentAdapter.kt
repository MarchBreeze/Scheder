package com.marchbreeze.scheder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OwnerMainFragmentAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    val fragments: List<Fragment> =
        listOf(OwnerSalaryFragment(), OwnerSchedFragment(), OwnerRequestFragment(), OwnerRequestFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OwnerSalaryFragment()
            1 -> OwnerSchedFragment()
            2 -> OwnerRequestFragment()
            else -> OwnerRequestFragment()
        }
    }
}