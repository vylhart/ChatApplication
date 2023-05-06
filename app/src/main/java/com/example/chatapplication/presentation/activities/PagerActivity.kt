package com.example.chatapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chatapplication.databinding.ActivityPagerBinding
import com.example.chatapplication.presentation.fragments.ChatFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpPager()
    }

    private fun setUpPager() {
        binding.viewPager.adapter = PagerAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                // Do something when page is selected
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Do something when page is scrolled
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Do something when page scroll state is changed
            }
        })
    }

}

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val pageCount = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChatFragment()
            1 -> ChatFragment()
            2 -> ChatFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Chat"
    }

    override fun getCount(): Int {
        return pageCount
    }
}