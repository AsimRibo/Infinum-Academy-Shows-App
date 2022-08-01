package hr.asimr.shows_asim.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.viewModels.ShowDetailsViewModel

class ShowDetailsViewModelFactory(private val id: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShowDetailsViewModel(id) as T
    }
}