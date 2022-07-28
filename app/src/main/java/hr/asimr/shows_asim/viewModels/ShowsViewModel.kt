package hr.asimr.shows_asim.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.models.api.response.ShowsResponse
import hr.asimr.shows_asim.networking.ApiModule
import java.util.UUID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

//    private val _showsLiveData = MutableLiveData(
//        listOf(
//            Show(
//                UUID.randomUUID().toString(),
//                "The Office",
//                "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees at the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, spanning a total of nine seasons.",
//                R.drawable.ic_office,
//                mutableListOf()
//            ),
//            Show(
//                UUID.randomUUID().toString(),
//                "Stranger Things",
//                "Stranger Things is an American science fiction horror drama television series " +
//                    "created by the Duffer Brothers that is streaming on Netflix. The brothers serve as showrunners and are executive producers along with Shawn Levy and Dan Cohen. The first season of the series was released on Netflix on July 15, 2016.",
//                R.drawable.ic_stranger_things,
//                mutableListOf()
//            ),
//            Show(
//                UUID.randomUUID().toString(),
//                "Krv nije voda",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",
//                R.drawable.ic_knv,
//                mutableListOf()
//            ))
//    )

    private val _showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    private val _success: MutableLiveData<Boolean> by lazy{ MutableLiveData<Boolean>() }
    val success: LiveData<Boolean> = _success

    fun getAllShows(){
        ApiModule.retrofit.getAllShows()
            .enqueue(object: Callback<ShowsResponse>{
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    _success.value = true
                    _showsLiveData.value = response.body()?.shows
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    _success.value = false
                }
            })
    }
}