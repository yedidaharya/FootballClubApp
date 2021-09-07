package com.yedidaharya.footballclubapp.db



import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yedidaharya.footballclubapp.model.Team
import org.jetbrains.anko.db.*

class SQLiteDBHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FootballClubApp.db", null, 1) {
    companion object {
        private var instance: SQLiteDBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): SQLiteDBHelper {
            if (instance == null) {
                instance = SQLiteDBHelper(ctx.applicationContext)
            }
            return instance as SQLiteDBHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {


        db?.createTable(
            Team.TABLE_TEAMS, true,
            Team.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Team.ID_TEAM to TEXT,
            Team.TEAM_NAME to TEXT,
            Team.TEAM_FORMED to TEXT,
            Team.TEAM_STADIUM to TEXT,
            Team.TEAM_DESC to TEXT,
            Team.TEAM_LOGO to TEXT,
            Team.MATCH_TYPE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(Team.TABLE_TEAMS, true)
    }
}

val Context.db: SQLiteDBHelper
    get() = SQLiteDBHelper.getInstance(applicationContext)