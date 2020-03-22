package com.app.githubtrendingrepo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Repository {
    @SerializedName("id")
    @Expose
    var id: Long = 0
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("full_name")
    @Expose
    var fullName: String? = null
    @SerializedName("owner")
    @Expose
    var owner: Owner? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("stargazers_count")
    @Expose
    var stargazersCount: Int = 0
    @SerializedName("language")
    @Expose
    var language: String? = null
    @SerializedName("forks_count")
    @Expose
    var forksCount: Int = 0
    @SerializedName("open_issues")
    @Expose
    var openIssues: Int = 0
    @SerializedName("watchers")
    @Expose
    var watchers: Int = 0
}