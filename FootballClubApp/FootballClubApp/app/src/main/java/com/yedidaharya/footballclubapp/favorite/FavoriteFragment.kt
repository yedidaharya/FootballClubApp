package com.yedidaharya.footballclubapp.favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.yedidaharya.footballclubapp.R
import com.yedidaharya.footballclubapp.db.db
import com.yedidaharya.footballclubapp.model.Team
import com.yedidaharya.footballclubapp.team.adapter.TeamAdapter
import com.yedidaharya.footballclubapp.team.detail.TeamDetailActivity
import com.yedidaharya.footballclubapp.utils.gone
import com.yedidaharya.footballclubapp.utils.visible
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity



class FavoriteFragment : Fragment() {
    private var teamsFavorite : MutableList<Team> = mutableListOf()
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var progressBarTeam: ProgressBar
    private lateinit var swipeRefreshTeam: SwipeRefreshLayout
    private lateinit var recyclerViewTeam: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshTeam = sr_teams_fav
        progressBarTeam = pb_teams_fav
        recyclerViewTeam = rv_teams_fav
        swipeRefreshTeam.setOnRefreshListener {
            swipeRefreshTeam.isRefreshing = false
            getTeam()
        }
        getTeam()
    }

    private fun getTeam() {
        progressBarTeam.visible()
        context?.db?.use {
            val selectData = select(Team.TABLE_TEAMS)
            val favorites = selectData.parseList(classParser<Team>())
            teamsFavorite.clear()
            teamsFavorite.addAll(favorites)
            recyclerViewTeam.layoutManager = LinearLayoutManager(context)
            teamAdapter = TeamAdapter(context,teamsFavorite) {
                context?.startActivity<TeamDetailActivity>("teamExtra" to it)
            }
            recyclerViewTeam.adapter = teamAdapter
        }
        progressBarTeam.gone()
    }

    override fun onResume() {
        super.onResume()
        getTeam()
        teamAdapter.notifyDataSetChanged()
    }


}