package com.yedidaharya.footballclubapp.team

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner

import com.yedidaharya.footballclubapp.R
import com.yedidaharya.footballclubapp.api.APIRepository
import com.yedidaharya.footballclubapp.model.League
import com.yedidaharya.footballclubapp.model.Team
import com.yedidaharya.footballclubapp.team.adapter.TeamAdapter
import com.yedidaharya.footballclubapp.team.detail.TeamDetailActivity
import com.yedidaharya.footballclubapp.utils.gone
import com.yedidaharya.footballclubapp.utils.isNetworkAvailable
import com.yedidaharya.footballclubapp.utils.visible
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_team.*
import org.jetbrains.anko.startActivity


class TeamFragment : Fragment(), TeamView {
    private var listTeams: MutableList<Team> = mutableListOf()
    private var listLeagues: MutableList<League> = mutableListOf()
    private lateinit var teamPresenter: TeamPresenter
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var progressBarTeam: ProgressBar
    private lateinit var swipeRefreshTeam: SwipeRefreshLayout
    private lateinit var recyclerViewTeam: RecyclerView
    private lateinit var spinnerTeam : Spinner
    private var spinnerIdDefault = "4328"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshTeam = sr_teams
        progressBarTeam = pb_teams
        recyclerViewTeam = rv_teams
        spinnerTeam = spinner_teams

        swipeRefreshTeam.setOnRefreshListener {
            swipeRefreshTeam.isRefreshing = false
            if (isNetworkAvailable(context)){
                signal_status.gone()
                tv_signal_status.gone()
                teamPresenter.getTeamsByLeague(spinnerIdDefault)
                setSpinnerContent()
                hideLoading()
            }else{
                hideLoading()
                signal_status.visible()
                tv_signal_status.visible()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val request = APIRepository()
        val gson = Gson()
        teamPresenter = TeamPresenter(this, request, gson)
        if (isNetworkAvailable(context)){
            setHasOptionsMenu(true)
            signal_status.gone()
            tv_signal_status.gone()
            teamPresenter.getTeamsByLeague(spinnerIdDefault)
            setSpinnerContent()
            hideLoading()
        }else{
            hideLoading()
            signal_status.visible()
            tv_signal_status.visible()
        }
    }

    private fun setSpinnerContent() {
        val arrayLeagues : MutableList<String> = mutableListOf()
        val nameLeague = resources.getStringArray(R.array.league_name)
        val idLeague = resources.getStringArray(R.array.league_id)
        listLeagues.clear()
        for (i in idLeague.indices){
            listLeagues.add(League(idLeague[i],nameLeague[i]))
        }
        for (value in listLeagues){
            arrayLeagues.add(value.strLeague.toString())
        }
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, arrayLeagues)
        spinnerTeam.adapter = adapter
        spinnerTeam.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val leagues = listLeagues[position]
                val leagueId = leagues.idLeague
                spinnerIdDefault = leagueId.toString()
                teamPresenter.getTeamsByLeague(spinnerIdDefault)
            }

        }
    }

    override fun showLoading() {
        progressBarTeam.visible()
    }

    override fun hideLoading() {
        progressBarTeam.gone()
    }

    override fun showTeamList(teams: List<Team>?) {
        val teamFilter: List<Team>? = teams?.filter { s -> s.strSport == "Soccer" }
        listTeams.clear()
        teamFilter?.let { listTeams.addAll(it) }
        recyclerViewTeam.layoutManager = LinearLayoutManager(context)
        teamAdapter = TeamAdapter(context,listTeams) {
            context?.startActivity<TeamDetailActivity>("teamExtra" to it)
        }
        recyclerViewTeam.adapter = teamAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search, menu)
        val searchTeam = menu?.findItem(R.id.searchButton)?.actionView as SearchView?
        searchTeam?.queryHint = "Search Team Name"

        searchTeam?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                teamPresenter.getTeamBySearch(p0)
                return false
            }
        })

        searchTeam?.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                return true
            }
        })
    }



}