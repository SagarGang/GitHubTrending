package com.app.githubtrendingrepo.ui.repodetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.githubtrendingrepo.R
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.app.githubtrendingrepo.util.Constants
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_repository_detail.*

class RepositoryDetail : AppCompatActivity() {

    private var repositoryDetail: RepositoryResponse.Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_detail)

    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra(Constants.REPOSITORY)) {
            repositoryDetail = intent.extras?.get(Constants.REPOSITORY) as RepositoryResponse.Item
        }

        Glide.with(this)
            .load(repositoryDetail?.owner?.avatarUrl)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .into(img_owner_avatar)

        txt_repo_title.text = repositoryDetail?.name
        txt_repo_description.text = repositoryDetail?.description
        txt_repo_watchers.text =
            String.format(getString(R.string.watchers), repositoryDetail?.watchers)
        tv_pro_link.text = repositoryDetail?.url

    }
}
