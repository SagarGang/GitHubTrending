package com.app.githubtrendingrepo.ui

import Order
import ReposParam
import Sort
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abhijith.assignment.github_trending.viewmodels.RepoListViewModel
import com.app.githubtrendingrepo.R
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.app.githubtrendingrepo.ui.adapter.ReposAdapter
import com.app.githubtrendingrepo.ui.adapter.ViewHolder
import com.app.githubtrendingrepo.util.Constants
import com.app.githubtrendingrepo.util.observeEditText
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Predicate
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity(), ViewHolder.OnRepoSelectedListener {


    private lateinit var listAdapter: ReposAdapter
    private lateinit var repoListViewModel: RepoListViewModel
    private var LOG_TAG = HomeActivity::class.java.simpleName
    private var PAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        repoListViewModel = ViewModelProvider(this).get(RepoListViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        listAdapter = ReposAdapter()

        rv_repos.apply {
            adapter = listAdapter
        }

        listAdapter.setClickListener(this)

        if (listAdapter.itemCount == 0) {
            repoListViewModel.getRepos(repoParmRequest("java"))
        }


        swipe_refresh_container.apply {
            setColorSchemeColors(
                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary),
                ContextCompat.getColor(this@HomeActivity, R.color.colorAccent),
                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimaryDark)
            )
            setOnRefreshListener {
                listAdapter.clear()
                repoListViewModel.getRepos(repoParmRequest("android"))
                isRefreshing = false
            }
        }

        repoListViewModel.subscribeRepoList().observe(this, Observer {
            Log.e(LOG_TAG, "Response : $it")
            progress_bar.visibility = View.GONE
            listAdapter.clear()
            listAdapter.repos = it
        })

        observeEditText(edt_search).debounce(20, TimeUnit.MILLISECONDS)
            .filter(Predicate {
                return@Predicate it.isNotEmpty()
            })
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: String ->
                repoListViewModel.getRepos(repoParmRequest(result))

            }, { t: Throwable? ->
                Log.e("search failed!", t.toString())
            })

    }


    fun repoParmRequest(search: String): ReposParam {
        progress_bar.visibility = View.VISIBLE
        return ReposParam(
            searchTerm = search, sort = Sort.STARS, order = Order.DESCENDING, page = PAGE,
            perPage = Constants.PAGE_SIZE
        )
    }

    override fun onRepoSelected(repo: RepositoryResponse.Item) {

    }
}
