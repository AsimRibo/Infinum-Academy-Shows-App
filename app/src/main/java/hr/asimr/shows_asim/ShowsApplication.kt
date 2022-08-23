package hr.asimr.shows_asim

import android.app.Application
import android.content.Context
import hr.asimr.shows_asim.dao.ShowsDatabase
import hr.asimr.shows_asim.fragments.LOGIN_PREFERENCES
import hr.asimr.shows_asim.managers.SharedPreferencesManager
import hr.asimr.shows_asim.networking.ApiModule

class ShowsApplication : Application() {

    val showsDatabase by lazy {
        ShowsDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        ApiModule.initRetrofit(this, getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE))
        SharedPreferencesManager.init(applicationContext)
    }
}