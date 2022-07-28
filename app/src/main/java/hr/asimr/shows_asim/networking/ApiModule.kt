package hr.asimr.shows_asim.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hr.asimr.shows_asim.viewModels.ACCESS_TOKEN
import hr.asimr.shows_asim.viewModels.CLIENT
import hr.asimr.shows_asim.viewModels.UID
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    private const val TOKEN_TYPE = "token-type"
    private const val TOKEN_TYPE_VALUE = "Bearer"
    lateinit var retrofit: ShowsApiService

    fun initRetrofit(context: Context) {
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }

    fun initRetrofit(context: Context, accessToken: String, client: String, uid: String) {
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor { chain ->
                chain.request()
                    .newBuilder()
                    .addHeader(TOKEN_TYPE, TOKEN_TYPE_VALUE)
                    .addHeader(ACCESS_TOKEN, accessToken)
                    .addHeader(CLIENT, client)
                    .addHeader(UID, uid)
                    .build()
                    .let(chain::proceed)
            }
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}