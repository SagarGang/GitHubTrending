package com.app.githubtrendingrepo.network

data class ReposParam(val searchTerm: String, val sort: Sort, val order: Order,
                      val page: Int, val perPage: Int) {
}