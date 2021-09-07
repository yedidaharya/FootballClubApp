package com.yedidaharya.footballclubapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.yedidaharya.footballclubapp.favorite.FavoriteFragment
import com.yedidaharya.footballclubapp.team.TeamFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener{item ->
            when (item.itemId) {
                R.id.navigation_teams -> {
                    val teamFragment = TeamFragment()
                    loadFragment(savedInstanceState, teamFragment)
                }
               R.id.navigation_favorites -> {
                    val favFragment = FavoriteFragment()
                    loadFragment(savedInstanceState, favFragment)
                }

            }
            true
        }
        navigation.selectedItemId = R.id.navigation_teams
    }

    private fun loadFragment(savedInstanceState: Bundle?, fragment: Fragment){
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
        }
    }
}