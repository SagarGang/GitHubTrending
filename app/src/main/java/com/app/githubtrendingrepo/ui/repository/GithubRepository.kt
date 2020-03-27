package com.app.githubtrendingrepo.network

import com.app.githubtrendingrepo.model.RepositoryResponse
import io.reactivex.Single


class GithubRepository(private val githubRepoApiClient: ServiceGenerator) {

    fun getTrendingReposFromApi(
        reposParam: ReposParam
    ): Single<RepositoryResponse> {
        return githubRepoApiClient.getRepoApiService().getRepos(
            reposParam.searchTerm,
            reposParam.sort.value,
            reposParam.order.value,
            reposParam.page,
            reposParam.perPage
        )
    }
}