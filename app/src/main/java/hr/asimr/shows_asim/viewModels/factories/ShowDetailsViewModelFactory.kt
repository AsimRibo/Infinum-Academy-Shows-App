package hr.asimr.shows_asim.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.asimr.shows_asim.dao.ShowsDatabase
import hr.asimr.shows_asim.viewModels.ShowDetailsViewModel

class ShowDetailsViewModelFactory(private val id: String, private val database: ShowsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShowDetailsViewModel(id, database) as T
    }
}