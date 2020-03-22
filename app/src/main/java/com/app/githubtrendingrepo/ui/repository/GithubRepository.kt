package com.abhijith.assignment.github_trending.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.app.githubtrendingrepo.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GithubRepository(private val context: Context) {

    private var githubRepoApiClient: ServiceGenerator = ServiceGenerator(context)
    private val repository: MutableLiveData<List<RepositoryResponse.Item>> =
        MutableLiveData()

    val repositoryList :LiveData<List<RepositoryResponse.Item>>
        get() = repository

    fun getTrendingReposFromApi(
        query: String,
        sort: String,
        order: String,
        page: Int,
        perPage: Int
    ) {
        githubRepoApiClient.getRepoApiService().getRepos(query, sort, order, page, perPage)
            .enqueue(object : Callback<RepositoryResponse> {
                override fun onFailure(call: Call<RepositoryResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<RepositoryResponse>,
                    response: Response<RepositoryResponse>
                ) {
                    if (response.isSuccessful && response.code() == 200) {
                        repository.value = response.body()?.items
                    }
                }

            })
    }

}