package com.app.githubtrendingrepo.network

import android.content.Context
import com.app.githubtrendingrepo.util.Connection
import com.app.githubtrendingrepo.util.Constants.BASE_URL
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ServiceGenerator(private val context: Context) {

    val cacheSize = (5 * 1024 * 1024).toLong()

    private val HEADER_CACHE_CONTROL = "Cache-Control"
    private val HEADER_PRAGMA = "Pragma"
    private var requestType: Boolean = false

    private val okHttpClient = OkHttpClient.Builder()
        .cache(cache())
        .addNetworkInterceptor(networkInterceptor())
        .addInterceptor(offlineInterceptor()).build()


    private fun cache(): Cache? {
        return Cache(File(context.cacheDir, "offlineCache"), cacheSize)
    }


    fun forceRequest(requestType: Boolean) {
        this.requestType = requestType
    }

    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(
                    5,
                    TimeUnit.SECONDS
                )
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }


    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request: Request = chain.request()

            if (requestType) {
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
            } else {
                if (!Connection.hasNetwork(context)) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(
                            2,
                            TimeUnit.HOURS
                        )
                        .build()

                    request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
                }
            }
            chain.proceed(request)
        }
    }

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    private val repoApiService = retrofitBuilder.create(RepositoryApiService::class.java)

    fun getRepoApiService(): RepositoryApiService {
        return repoApiService
    }

}