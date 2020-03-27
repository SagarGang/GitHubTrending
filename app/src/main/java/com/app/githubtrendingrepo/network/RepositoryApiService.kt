package com.app.githubtrendingrepo.network

import com.app.githubtrendingrepo.model.RepositoryResponse
import io.reactivex.Single
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
    ): Single<RepositoryResponse>

}