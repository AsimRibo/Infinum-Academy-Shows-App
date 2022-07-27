package hr.asimr.shows_asim.networking

import hr.asimr.shows_asim.models.api.request.RegisterRequest
import hr.asimr.shows_asim.models.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {
    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}