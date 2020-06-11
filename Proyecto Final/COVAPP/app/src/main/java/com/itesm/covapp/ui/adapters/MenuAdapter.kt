package com.itesm.covapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MenuAdapter (manager: FragmentManager, val fragmentList: ArrayList<Fragment>): FragmentPagerAdapter(manager) {
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
    override fun getPageTitle(position: Int): CharSequence? {
        val tabIcons = intArrayOf(

        )

        return when (position) {
            0 -> ""
            1 -> ""
            2 -> ""
            3 -> ""
            4 -> ""
            else -> null
        }
    }
}