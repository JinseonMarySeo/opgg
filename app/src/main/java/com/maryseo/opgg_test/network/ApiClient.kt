package com.maryseo.opgg_test.network

import android.annotation.SuppressLint
import com.maryseo.opgg_test.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object ApiClient {
    private val BASE_URL = "https://codingtest.op.gg/api/"
    private const val TIMEOUT = 60

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getUnsafeOkHttpClient().build())
        .build()

    val service: ApiInterface = retrofit.create(ApiInterface::class.java)

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(
                @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                }
            )
//            val sslContext = SSLContext.getInstance("SSL")
//            sslContext.init(null, trustAllCerts, SecureRandom())
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                logging.setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            builder.addInterceptor(logging)
            builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
            builder.followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
            builder.connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}