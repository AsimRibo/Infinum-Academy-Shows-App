package hr.asimr.shows_asim.networking

import hr.asimr.shows_asim.models.api.request.LoginRequest
import hr.asimr.shows_asim.models.api.request.RegisterRequest
import hr.asimr.shows_asim.models.api.request.ReviewRequest
import hr.asimr.shows_asim.models.api.response.UserResponse
import hr.asimr.shows_asim.models.api.response.RegisterResponse
import hr.asimr.shows_asim.models.api.response.ReviewResponse
import hr.asimr.shows_asim.models.api.response.ShowResponse
import hr.asimr.shows_asim.models.api.response.ShowReviewsResponse
import hr.asimr.shows_asim.models.api.response.ShowsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ShowsApiService {
    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest) : Call<UserResponse>

    @GET("/shows2")
    fun getAllShows() : Call<ShowsResponse>

    @GET("/shows/{showId}")
    fun getShow(@Path("showId") showId: String) : Call<ShowResponse>

    @GET("/shows/{showId}/reviews")
    fun getShowReviews(@Path("showId") showId: String) : Call<ShowReviewsResponse>

    @POST("/reviews")
    fun addReview(@Body request : ReviewRequest) : Call<ReviewResponse>

    @Multipart
    @PUT("/users")
    fun updateUserImage(@Part image: MultipartBody.Part) : Call<UserResponse>
}