package com.mj.covid19detector.module

import com.google.gson.GsonBuilder
import com.mj.covid19detector.config.OPEN_API_URL
import com.mj.covid19detector.net.RetrofitConnection
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun retrofitApiConnectionModule() = module{

    single {
        GsonBuilder().setLenient().create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(OPEN_API_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(RetrofitConnection::class.java)
    }
}


