package hr.asimr.shows_asim

import android.app.Application
import android.content.Context
import hr.asimr.shows_asim.fragments.LOGIN_PREFERENCES
import hr.asimr.shows_asim.networking.ApiModule

class ShowsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiModule.initRetrofit(this, getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE))
    }
}