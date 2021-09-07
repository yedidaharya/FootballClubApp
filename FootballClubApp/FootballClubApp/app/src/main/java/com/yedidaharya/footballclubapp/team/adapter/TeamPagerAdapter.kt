package com.yedidaharya.footballclubapp.team.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.yedidaharya.footballclubapp.model.Team
import com.yedidaharya.footballclubapp.team.detail.OverviewTeamFragment

class TeamPagerAdapter(fragmentManager: FragmentManager, val teamItem : Team): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment {
        when (p0){
            0 -> {
                val bundle = Bundle()
                val overviews = OverviewTeamFragment()
                bundle.putParcelable("teamExtra", teamItem)
                overviews.arguments = bundle
                return overviews
            }
            else -> {
                val bundle = Bundle()
                val overviews = OverviewTeamFragment()
                bundle.putParcelable("teamExtra", teamItem)
                overviews.arguments = bundle
                return overviews
            }
        }

    }

    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence? {
            return "Team Description"

    }

}