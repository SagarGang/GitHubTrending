package com.app.githubtrendingrepo.network

import android.content.Context
import com.app.githubtrendingrepo.util.Connection
import com.app.githubtrendingrepo.util.Constants.BASE_URL
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ServiceGenerator(private val context:Context) {

    val cacheSize = (5 * 1024 * 1024).toLong() // 5 Mb to store the cache

    private  val HEADER_CACHE_CONTROL = "Cache-Control"
    private  val HEADER_PRAGMA = "Pragma"

    private val okHttpClient = OkHttpClient.Builder()
        .cache(cache())
        .addNetworkInterceptor(networkInterceptor()) // only used when network is on
        .addInterceptor(offlineInterceptor()).build()


    private fun cache(): Cache? {
        return Cache(File(context.cacheDir, "offlineCache"), cacheSize)
    }


    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(
                    5,
                    TimeUnit.SECONDS
                ) // if the request is executed in less than 5 seconds, it will get from cache
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA) // header that is attached to HTTP request and says not to use cache any more
                .removeHeader(HEADER_CACHE_CONTROL) // that defines cache control
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }


    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request: Request = chain.request()

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
            chain.proceed(request)
        }
    }

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    private val repoApiService = retrofitBuilder.create(RepositoryApiService::class.java)

    fun getRepoApiService(): RepositoryApiService {
        return repoApiService
    }

}