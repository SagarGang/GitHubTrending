package com.app.githubtrendingrepo.network


class Resource<T> private constructor(val status: Status, val data: T?,
                                      val message: String?) {
    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T> networkError(): Resource<T> {
            return Resource(Status.NETWORK_ERROR, null, null)
        }

        fun <T> idle(): Resource<T> {
            return Resource(Status.IDLE, null, null)
        }
    }
}
