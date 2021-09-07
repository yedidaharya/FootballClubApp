package com.yedidaharya.footballclubapp.team

import com.yedidaharya.footballclubapp.model.Team

interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(teams: List<Team>?)
}