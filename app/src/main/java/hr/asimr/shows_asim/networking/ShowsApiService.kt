package hr.asimr.shows_asim.networking

import hr.asimr.shows_asim.models.api.request.LoginRequest
import hr.asimr.shows_asim.models.api.request.RegisterRequest
import hr.asimr.shows_asim.models.api.response.LoginResponse
import hr.asimr.shows_asim.models.api.response.RegisterResponse
import hr.asimr.shows_asim.models.api.response.ShowsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShowsApiService {
    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest) : Call<LoginResponse>

    @GET("/shows")
    fun getAllShows() : Call<ShowsResponse>
}