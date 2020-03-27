package com.app.githubtrendingrepo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.githubtrendingrepo.R
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter :
    RecyclerView.Adapter<ViewHolder>() {

    lateinit var repoListener: ViewHolder.OnRepoSelectedListener

    var repos: MutableList<RepositoryResponse.Item> = mutableListOf()
        set(repos) {
            field.clear()
            field.addAll(repos)
            notifyDataSetChanged()
        }

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRepo(repos[position], repoListener)
    }

    fun setClickListener(repoListener: ViewHolder.OnRepoSelectedListener) {
        this.repoListener = repoListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        return ViewHolder(inflatedView)
    }

    fun clear() {
        repos.clear()
        notifyDataSetChanged()
    }
}

class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    fun bindRepo(repo: RepositoryResponse.Item, repoListener: OnRepoSelectedListener) {
        itemView.setOnClickListener {
            repoListener.onRepoSelected(repo)
        }
        Glide.with(itemView)
            .load(repo.owner?.avatarUrl)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .into(itemView.img_owner_avatar)
        itemView.txt_repo_title.text = repo.name
        itemView.txt_repo_description.text = repo.description
        itemView.txt_repo_watchers.text =
            String.format(itemView.context.getString(R.string.watchers), repo.watchers)

    }

    interface OnRepoSelectedListener {
        fun onRepoSelected(repo: RepositoryResponse.Item)
    }
}