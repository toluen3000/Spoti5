package com.example.spoti5.data.apis

import com.example.spoti5.data.database.entities.AppModel
import retrofit2.Response
import retrofit2.http.GET

interface AppApi {
    @GET("all_data")
    suspend fun getAllData(): Response<List<AppModel>>
}