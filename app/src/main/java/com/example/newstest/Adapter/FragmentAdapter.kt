package com.example.newstest.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newstest.Constants.TOTAL_NEWS_TAB
import com.example.newstest.MainActivity

import com.example.newstest.UI.*

class FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle){

    override fun getItemCount(): Int = TOTAL_NEWS_TAB

    override fun createFragment(position: Int): Fragment {
                return when(position){
            0 -> GeneralFragment()
            1 -> TechFragment()
            2 -> HealthFragment()
            3 -> ScienceFragment()
            4 -> EntertainmentFragment()
            5 -> SportsFragment()
            else -> BusinessFragment()
        }
    }
}


//
//class FragmentAdapter(MainActivity: AppCompatActivity) : FragmentStateAdapter(MainActivity){
//    override fun getItemCount(): Int {
//        return TOTAL_NEWS_TAB
//    }
//
//    override fun createFragment(position: Int): Fragment {
//
//        return when(position){
//            0 -> GeneralFragment()
//            1 -> TechFragment()
//            2 -> HealthFragment()
//            3 -> ScienceFragment()
//            4 -> EntertainmentFragment()
//            5 -> SportsFragment()
//            else -> BusinessFragment()
//        }
//
//    }
//}

//class FragmentAdapter(Fragment:ViewPagingFragment) : FragmentStateAdapter(Fragment){
//    override fun getItemCount(): Int {
//        return TOTAL_NEWS_TAB
//    }
//
//    override fun createFragment(position: Int): Fragment {
//
//        return when(position){
//            0 -> GeneralFragment()
//            1 -> TechFragment()
//            2 -> HealthFragment()
//            3 -> ScienceFragment()
//            4 -> EntertainmentFragment()
//            5 -> SportsFragment()
//            else -> BusinessFragment()
//        }
//
//    }
//}
