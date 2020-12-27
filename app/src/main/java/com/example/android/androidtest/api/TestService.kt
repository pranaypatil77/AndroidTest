package com.example.android.androidtest.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestService {
    fun create():TestApi{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(TestApi::class.java)
        return service

    }
}