package hr.asimr.shows_asim.viewModels.factories

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.asimr.shows_asim.viewModels.LoginViewModel
import hr.asimr.shows_asim.viewModels.ShowDetailsViewModel

class LoginViewModelFactory(private val preferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(preferences) as T
    }
}