package com.abhijith.assignment.github_trending.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhijith.assignment.github_trending.repositories.GithubRepository
import com.app.githubtrendingrepo.model.RepositoryResponse


class RepoListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GithubRepository
    var repos: MutableLiveData<List<RepositoryResponse.Item>> = MutableLiveData()

    init {
        repository = GithubRepository(application.baseContext)
    }

    fun getRepos() {
        repository.getTrendingReposFromApi("", "", "", 1, 10)

    }

    fun subscribeRepoList(): LiveData<List<RepositoryResponse.Item>> {
        return repository.repositoryList
    }

}