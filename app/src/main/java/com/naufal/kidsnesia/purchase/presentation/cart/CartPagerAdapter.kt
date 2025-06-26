package com.naufal.kidsnesia.purchase.presentation.cart

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.naufal.kidsnesia.purchase.presentation.cart.event.EventCartFragment
import com.naufal.kidsnesia.purchase.presentation.cart.merch.MerchCartFragment

class CartPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    val eventFragment = EventCartFragment()
    val merchFragment = MerchCartFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> eventFragment
            1 -> merchFragment
            else -> Fragment()
        }
    }
}
