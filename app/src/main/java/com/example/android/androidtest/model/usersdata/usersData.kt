package com.example.android.androidtest.model.usersdata

data class usersData(
    val `data`: List<Data>?,
    val page: Int?,
    val per_page: Int?,
    val support: Support?,
    val total: Int?,
    val total_pages: Int?
)