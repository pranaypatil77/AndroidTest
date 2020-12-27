package com.example.android.androidtest.api

import com.example.android.androidtest.model.logindata.loginData
import com.example.android.androidtest.model.usersdata.usersData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface TestApi {
    @FormUrlEncoded
    @POST("api/login")
    fun getLogin(
        @Field("email")email:String,
        @Field("password")password:String
    ): Call<loginData>

    @GET("api/users?page=2")
    fun getUsers(): Call<usersData>
}