package hr.asimr.shows_asim.networking

import android.content.Context
import android.content.SharedPreferences
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

    fun initRetrofit(context: Context, pref: SharedPreferences) {
        val okhttpBuilder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(ChuckerInterceptor.Builder(context).build())

            if (pref.getString(ACCESS_TOKEN, "").orEmpty().isNotEmpty()){
                okhttpBuilder.addInterceptor{ chain ->
                    chain.request().newBuilder()
                        .addHeader(TOKEN_TYPE, TOKEN_TYPE_VALUE)
                        .addHeader(ACCESS_TOKEN, pref.getString(ACCESS_TOKEN, "").orEmpty())
                        .addHeader(CLIENT, pref.getString(CLIENT, "").orEmpty())
                        .addHeader(UID, pref.getString(UID, "").orEmpty())
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