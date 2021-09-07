package com.yedidaharya.footballclubapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View


fun View.visible() {
    visibility = View.VISIBLE
}


fun View.gone() {
    visibility = View.GONE
}


fun isNetworkAvailable(ctx: Context?): Boolean {
    val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else {
        false
    }
}