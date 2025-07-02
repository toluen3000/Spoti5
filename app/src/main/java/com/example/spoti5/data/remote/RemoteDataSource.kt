package com.example.spoti5.data.remote

//import com.example.spoti5.data.apis.AppApi
//import dagger.Lazy
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class RemoteDataSource @Inject constructor(private val appApi: Lazy<AppApi>): BaseRemoteService() {
//    suspend fun getAllData(): NetworkResult<List<AppModel>> {
//        return callApi { appApi.get().getAllData() }
//    }
//}