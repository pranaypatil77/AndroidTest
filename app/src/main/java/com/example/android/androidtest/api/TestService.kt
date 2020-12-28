package com.example.android.androidtest.api

import com.example.android.androidtest.utils.constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestService {
    fun create():TestApi{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(TestApi::class.java)
        return service

    }
}