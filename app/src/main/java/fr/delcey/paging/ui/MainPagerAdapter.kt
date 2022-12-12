package fr.delcey.paging.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fr.delcey.paging.ui.tracks.TracksFragment

class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = TracksFragment()

}
