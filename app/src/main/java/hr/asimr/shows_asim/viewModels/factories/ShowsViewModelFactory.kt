package hr.asimr.shows_asim.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.asimr.shows_asim.dao.ShowsDatabase
import hr.asimr.shows_asim.viewModels.ShowsViewModel

class ShowsViewModelFactory(val database: ShowsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        }
        throw IllegalArgumentException("Wrong type")
    }
}