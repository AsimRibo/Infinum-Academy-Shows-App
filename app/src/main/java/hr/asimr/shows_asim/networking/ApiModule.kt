package hr.asimr.shows_asim.networking

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hr.asimr.shows_asim.viewModels.ACCESS_TOKEN_VALUE
import hr.asimr.shows_asim.viewModels.CLIENT_VALUE
import hr.asimr.shows_asim.viewModels.UID_VALUE
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

const val ACCESS_TOKEN = "access-token"
const val CLIENT = "client"
const val UID = "uid"
object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    private const val TOKEN_TYPE = "token-type"
    private const val TOKEN_TYPE_VALUE = "Bearer"
    lateinit var retrofit: ShowsApiService

    fun initRetrofit(context: Context, pref: SharedPreferences) {
        val okhttpBuilder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(ChuckerInterceptor.Builder(context).build())

            if (pref.getString(ACCESS_TOKEN_VALUE, "").orEmpty().isNotEmpty()){
                okhttpBuilder.addInterceptor{ chain ->
                    chain.request().newBuilder()
                        .addHeader(TOKEN_TYPE, TOKEN_TYPE_VALUE)
                        .addHeader(ACCESS_TOKEN, pref.getString(ACCESS_TOKEN_VALUE, "").orEmpty())
                        .addHeader(CLIENT, pref.getString(CLIENT_VALUE, "").orEmpty())
                        .addHeader(UID, pref.getString(UID_VALUE, "").orEmpty())
                        .build()
                        .let(chain::proceed)
                }
            }

        val okhttp = okhttpBuilder.build()

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