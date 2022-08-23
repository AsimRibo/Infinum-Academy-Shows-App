package hr.asimr.shows_asim.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import hr.asimr.shows_asim.fragments.LOGIN_PREFERENCES

object SharedPreferencesManager {
    private lateinit var sharedPrefs: SharedPreferences

    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun writeString(key: String, value: String?) {
        sharedPrefs.edit {
            putString(key, value)
        }
    }

    fun readString(key: String, value: String): String? {
        return sharedPrefs.getString(key, value)
    }

    fun writeBoolean(key: String, value: Boolean) {
        sharedPrefs.edit {
            putBoolean(key, value)
        }
    }

    fun readBoolean(key: String): Boolean {
        return sharedPrefs.getBoolean(key, false)
    }
}