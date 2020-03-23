package com.app.githubtrendingrepo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.githubtrendingrepo.R
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter :
    RecyclerView.Adapter<ViewHolder>() {

    private lateinit var listener: ViewHolder.OnRepoSelectedListener

    var repos: MutableList<RepositoryResponse.Item> = mutableListOf()
        set(repos) {
            field.addAll(repos)
            notifyDataSetChanged()
        }

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRepo(repos[position])
    }

    fun setClickListener(listener: ViewHolder.OnRepoSelectedListener) {
        this.listener = listener
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

    fun bindRepo(repo: RepositoryResponse.Item) {
        itemView.setOnClickListener {

        }
        Picasso.get().load(repo.owner.avatarUrl).into(itemView.img_owner_avatar)
        itemView.txt_repo_title.text = repo.name
        itemView.txt_repo_description.text = repo.description
        itemView.txt_repo_watchers.text = " " + String.format("%,d", repo?.watchers)

    }

    interface OnRepoSelectedListener {
        fun onRepoSelected(repo: RepositoryResponse.Item)
    }
}