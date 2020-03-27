package com.app.githubtrendingrepo.ui.home

import RepoListViewModel
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.githubtrendingrepo.R
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.app.githubtrendingrepo.network.Order
import com.app.githubtrendingrepo.network.ReposParam
import com.app.githubtrendingrepo.network.Sort
import com.app.githubtrendingrepo.network.Status
import com.app.githubtrendingrepo.ui.adapter.ReposAdapter
import com.app.githubtrendingrepo.ui.adapter.ViewHolder
import com.app.githubtrendingrepo.ui.repodetail.RepositoryDetail
import com.app.githubtrendingrepo.util.Constants
import com.app.githubtrendingrepo.util.showToast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), ViewHolder.OnRepoSelectedListener {

    private lateinit var repoListViewModel: RepoListViewModel
    private var LOG_TAG = HomeActivity::class.java.simpleName
    private var PAGE = 1
    private var filterRepoSearchList: List<RepositoryResponse.Item> = mutableListOf()
    private var originalReposSearchList: MutableList<RepositoryResponse.Item> = mutableListOf()

    private val listAdapter: ReposAdapter by lazy {
        ReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        repoListViewModel = ViewModelProvider(this).get(RepoListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        rv_repos.apply {
            adapter = listAdapter
        }

        listAdapter.setClickListener(this)

        if (listAdapter.itemCount == 0) {
            repoListViewModel.getRepos(repoParmRequest(Constants.TRENDING_REPO), false)
        }

        swipe_refresh_container.apply {
            setColorSchemeColors(
                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary),
                ContextCompat.getColor(this@HomeActivity, R.color.colorAccent),
                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimaryDark)
            )
            setOnRefreshListener {
                listAdapter.clear()
                repoListViewModel.getRepos(repoParmRequest(Constants.TRENDING_REPO), true)
                isRefreshing = false
            }
        }

        repoListViewModel.repositoryItemList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> progress_bar.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    listAdapter.clear()
                    originalReposSearchList = it.data as MutableList<RepositoryResponse.Item>
                    listAdapter.repos = originalReposSearchList
                    progress_bar.visibility = View.GONE
                }
                Status.NETWORK_ERROR -> {
                    showToast(this, getString(R.string.no_internet_connection))
                    progress_bar.visibility = View.GONE
                }
                Status.ERROR -> {
                    showToast(this, it.message)
                    progress_bar.visibility = View.GONE
                }
                else -> error("unexpected error ")
            }
        })

        edt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    filterByRepoName(s)

                } else if (s.toString().isEmpty()) {
                    listAdapter.repos = originalReposSearchList
                    filterRepoSearchList = emptyList()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }

    private fun filterByRepoName(s: Editable?) {
        filterRepoSearchList = originalReposSearchList.filter { repo ->
            repo.name!!.contains(s.toString(), true)
        }
        listAdapter.repos = filterRepoSearchList as MutableList<RepositoryResponse.Item>
    }


    fun repoParmRequest(search: String): ReposParam {
        progress_bar.visibility = View.VISIBLE
        return ReposParam(
            searchTerm = search, sort = Sort.STARS, order = Order.DESCENDING, page = PAGE,
            perPage = Constants.PAGE_SIZE
        )
    }

    override fun onRepoSelected(repo: RepositoryResponse.Item) {
        // start a new activity for Repository Detail.
        val intent = Intent(this, RepositoryDetail::class.java).apply {
            putExtra(Constants.REPOSITORY, repo)
        }
        startActivity(intent)
    }
}
