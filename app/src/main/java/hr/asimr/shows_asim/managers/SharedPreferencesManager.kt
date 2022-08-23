package hr.asimr.shows_asim.managers

import android.content.Context
import android.content.SharedPreferences
import hr.asimr.shows_asim.fragments.LOGIN_PREFERENCES

object SharedPreferencesManager {
    private lateinit var sharedPrefs: SharedPreferences

    fun init(context: Context){
        sharedPrefs = context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
    }


}