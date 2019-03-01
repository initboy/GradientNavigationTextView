package com.time.demo.gntv.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *Project:GradientNavigationTextView
 *Author:君
 *Time:2019-02-28 12:06
 */
class ViewPagerAdapter(manager:FragmentManager,
                      private val data:List<Fragment>) :FragmentPagerAdapter(manager){
    override fun getItem(position: Int): Fragment {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }
}