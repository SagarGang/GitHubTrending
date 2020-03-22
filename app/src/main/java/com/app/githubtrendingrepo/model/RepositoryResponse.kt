package com.app.githubtrendingrepo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RepositoryResponse {
    @SerializedName("items")
    @Expose
    var repos: List<Repository> = listOf()
}