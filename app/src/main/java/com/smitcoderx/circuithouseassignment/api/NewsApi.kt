package com.smitcoderx.circuithouseassignment.api

import com.smitcoderx.circuithouseassignment.data.NewsData
import com.smitcoderx.circuithouseassignment.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {


    @GET("top-headlines")
    suspend fun getTopHeadlinesCountryWise(
        @Header("Authorization") token: String = API_KEY,
        @Query("country") country: String
    ): Response<NewsData>


    @GET("top-headlines")
    suspend fun getTopHeadlinesCategoryWise(
        @Header("Authorization") token: String = API_KEY,
        @Query("category") category: String
    ): Response<NewsData>

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Header("Authorization") token: String = API_KEY,
        @Query("country") country: String,
        @Query("category") category: String
    ): Response<NewsData>




}