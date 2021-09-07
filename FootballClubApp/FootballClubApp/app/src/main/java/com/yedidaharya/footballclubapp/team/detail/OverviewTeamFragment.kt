package com.yedidaharya.footballclubapp.team.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yedidaharya.footballclubapp.R
import com.yedidaharya.footballclubapp.model.Team
import kotlinx.android.synthetic.main.fragment_overview_team.*


class OverviewTeamFragment : Fragment() {

    private lateinit var teamDescription : Team
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_overview_team, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        teamDescription = bundle?.getParcelable("teamExtra")!!

        tv_overview_team.text = teamDescription.strDescriptionEN

    }

}