package com.app.githubtrendingrepo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.githubtrendingrepo.R
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.app.githubtrendingrepo.ui.adapter.ReposAdapter
import com.app.githubtrendingrepo.ui.adapter.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), ViewHolder.OnRepoSelectedListener {


    private lateinit var listAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        rv_repos.apply {
            adapter = listAdapter
        }

        listAdapter.setClickListener(this)

        if (listAdapter.itemCount == 0) {
//            presenter.loadRepos(true)
        }


        swipe_refresh_container.apply {
            setColorSchemeColors(
                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary),
                ContextCompat.getColor(this@HomeActivity, R.color.colorAccent),
                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimaryDark)
            )
            setOnRefreshListener {
                listAdapter.clear()
//                presenter.loadRepos(true)
            }
        }


    }

    override fun onRepoSelected(repo: RepositoryResponse.Item) {
    }
}
