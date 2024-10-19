package com.example.samsara.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.samsara.screens.rent.RentFragment
import com.example.samsara.screens.buy.BuyFragment

class RentBuyViewPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    private val fragments= arrayOf(RentFragment(), BuyFragment())
    override fun getItemCount(): Int {
        return fragments.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
