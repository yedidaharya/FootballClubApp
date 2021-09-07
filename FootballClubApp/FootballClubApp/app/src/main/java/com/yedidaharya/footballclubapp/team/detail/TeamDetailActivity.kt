package com.yedidaharya.footballclubapp.team.detail

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.yedidaharya.footballclubapp.R
import com.yedidaharya.footballclubapp.db.db
import com.yedidaharya.footballclubapp.model.Team
import com.yedidaharya.footballclubapp.team.adapter.TeamPagerAdapter
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

class TeamDetailActivity : AppCompatActivity() {

    private lateinit var teamExtra: Team
    private var menuItem: Menu? = null
    private var toolbarTeam: Toolbar? = null
    private var isFavorite: Boolean = false
    // declaring variables
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        teamExtra = intent.getParcelableExtra("teamExtra")!!
        val teamDetailActivity = TeamPagerAdapter(supportFragmentManager, teamExtra)
        vp_team_detail.adapter = teamDetailActivity
        tl_team_detail.setupWithViewPager(vp_team_detail)

        toolbarTeam = toolbar_team_detail
        setSupportActionBar(toolbarTeam)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        teamExtra.idTeam?.let { favoriteState(it) }
        setTeamDetail()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    private fun setTeamDetail() {
        tv_team_name.text = teamExtra.strTeam
        tv_team_formed.text = teamExtra.intFormedYear
        tv_team_stadium.text = teamExtra.strStadium
        Glide.with(applicationContext).load(teamExtra.strTeamBadge).into(iv_team_detail)

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        menuItem = menu
        menuItem?.getItem(1)?.isVisible = false
        setFavorite()
        return true
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorites)
        } else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_unfavorite)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_fav -> {
                if (isFavorite) deleteFavorite() else addFavorite()
                isFavorite = !isFavorite
                setFavorite()

                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteFavorite() {
        try {
            db.use {
                delete(Team.TABLE_TEAMS, "ID_TEAM = {idTeam}",
                    "idTeam" to teamExtra.idTeam.toString())
            }
            notificationUnfavorite()
        }catch (e: SQLiteConstraintException){
            toast(e.localizedMessage)
        }
    }

    private fun addFavorite() {
        try{
            db.use {
                insert(Team.TABLE_TEAMS,
                    Team.ID_TEAM to teamExtra.idTeam.toString(),
                    Team.TEAM_NAME to teamExtra.strTeam,
                    Team.TEAM_FORMED to teamExtra.intFormedYear,
                    Team.TEAM_STADIUM to teamExtra.strStadium,
                    Team.TEAM_DESC to teamExtra.strDescriptionEN,
                    Team.TEAM_LOGO to teamExtra.strTeamBadge)
            }
            notificationFavorite()

        }catch (e: SQLiteConstraintException){
            toast(e.localizedMessage)
        }
    }

    private fun favoriteState(idTeam: String){
        db.use {
            val selectData = select(Team.TABLE_TEAMS)
                .whereArgs("ID_TEAM = {idTeam}",
                    "idTeam" to idTeam)
            val favorites = selectData.parseList(classParser<Team>())
            if (!favorites.isEmpty()) isFavorite = true
        }
    }

    private fun notificationFavorite(){
        val intent = Intent(this,TeamDetailActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(packageName,R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title,"Football Club")
        contentView.setTextViewText(R.id.tv_content,"Added to Favorite")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.splash)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.splash))
                .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.splash)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.splash))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1,builder.build())
    }

    private fun notificationUnfavorite(){
        val intent = Intent(this,TeamDetailActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(packageName,R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title,"Football Club")
        contentView.setTextViewText(R.id.tv_content,"Deleted from Favorite")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.splash)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.splash))
                .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.splash)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.splash))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(2,builder.build())
    }






}