package com.abhijith.assignment.github_trending.viewmodels

import ReposParam
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhijith.assignment.github_trending.repositories.GithubRepository
import com.app.githubtrendingrepo.model.RepositoryResponse


class RepoListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GithubRepository

    init {
        repository = GithubRepository(application.baseContext)
    }

    fun getRepos(reposParam: ReposParam) {
        repository.getTrendingReposFromApi(reposParam)

    }

    fun subscribeRepoList(): LiveData<MutableList<RepositoryResponse.Item>> {
        return repository.repositoryList
    }

}