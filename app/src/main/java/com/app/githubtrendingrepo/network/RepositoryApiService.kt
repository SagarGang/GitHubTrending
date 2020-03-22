package com.app.githubtrendingrepo.network

import androidx.lifecycle.LiveData
import com.app.githubtrendingrepo.model.RepositoryResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoryApiService {

    @GET("search/repositories")
    fun getRepos(
        @Query("q") searchTerm: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<RepositoryResponse>

}