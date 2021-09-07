package com.yedidaharya.footballclubapp.team

import com.yedidaharya.footballclubapp.api.APIRepository
import com.yedidaharya.footballclubapp.api.APIService
import com.yedidaharya.footballclubapp.model.TeamResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamPresenter(
    private val view: TeamView,
    private val apiRepository: APIRepository,
    private val gson: Gson
)  {
    fun getTeamsByLeague(leagueId : String?){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main){
            val data = gson.fromJson(apiRepository.doRequest(
                APIService.getTeamsByLeague(leagueId)).await(), TeamResponse::class.java)
            view.hideLoading()
            data?.teams.let { view.showTeamList(it) }
        }
    }

    fun getTeamBySearch(text : String?){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main){
            val data = gson.fromJson(apiRepository.doRequest(
                APIService.searchTeam(text)).await(), TeamResponse::class.java)
            view.hideLoading()
            data?.teams.let { view.showTeamList(it) }
        }
    }


}
