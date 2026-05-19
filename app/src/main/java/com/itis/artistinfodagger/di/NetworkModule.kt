package com.itis.artistinfodagger.di

import android.app.Application
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.itis.artistinfodagger.api.TheAudioDBApi
import dagger.Module
import dagger.Provides
import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return try {
//            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
//                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
//                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
//                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
//            })
//
//            val sslContext = SSLContext.getInstance("TLS")
//            sslContext.init(null, trustAllCerts, SecureRandom())
//
//            OkHttpClient.Builder()
//                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
//                .hostnameVerifier { _, _ -> true }
//                .build()
//        } catch (e: Exception) {
//            OkHttpClient()
//        }
//    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .dns(object : Dns {
                override fun lookup(hostname: String): List<java.net.InetAddress> {
                    return Dns.SYSTEM.lookup(hostname)
                        .filter { it is Inet4Address }
                        .ifEmpty { Dns.SYSTEM.lookup(hostname) }
                }
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.theaudiodb.com/api/v1/json/2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTheAudioDBApi(retrofit: Retrofit): TheAudioDBApi {
        return retrofit.create(TheAudioDBApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        okHttpClient: OkHttpClient,
        application: Application
    ): ImageLoader {
        return ImageLoader.Builder(application)
            .components {
                add(OkHttpNetworkFetcherFactory(okHttpClient))
            }
            .build()
    }
}