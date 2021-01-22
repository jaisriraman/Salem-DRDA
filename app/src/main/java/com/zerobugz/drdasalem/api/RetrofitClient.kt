package com.zerobugz.drdasalem.api

import com.zerobugz.drdasalem.MyApp
import com.zerobugz.drdasalem.utils.SharedPreference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {
    private val retrofit: Retrofit
    val api: Api get() = retrofit.create(Api::class.java)

    companion object {
        private const val BASE_URL = Constants.Http.URL_BASE
        private var mInstance: RetrofitClient? = null

        @get:Synchronized
        val instance: RetrofitClient?
            get() {
                if (mInstance == null) {
                    mInstance = RetrofitClient()
                }
                return mInstance
            }
    }

    init {

        val sp = SharedPreference()
        sp.getString(MyApp.context, "id")
        sp.getString(MyApp.context, "token")

        println(
            "================================" + sp.getString(
                MyApp.instance!!.applicationContext,
                "token"
            )
        )

        val httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        //  httpClient.addInterceptor(ChuckerInterceptor(MyApp.instance!!.applicationContext))
        httpClient.addInterceptor(logging)  // <-- this is the important line!
        httpClient.readTimeout(600000, TimeUnit.MILLISECONDS)
        httpClient.retryOnConnectionFailure(true)



        httpClient.addInterceptor(
            object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader(
                            "Authorization",
                            sp.getString(MyApp.instance!!.applicationContext, "token")
                        )
                        .method(original.method, original.body)
                    val request = requestBuilder.build()
                    return chain.proceed(request)
                }
            }
        ).build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
}