package com.mksolution.newsshortadmin.Api


import com.mksolution.newsshortadmin.AccessToken
import com.mksolution.newsshortadmin.Models.Notification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {
    @POST("/v1/projects/news-short-11dd4/messages:send")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    fun notification(
        @Body message: Notification,
        @Header("Authorization") accessToken: String = "Bearer ${AccessToken.getAccessToken()}"
    ): Call<Notification>
}