package com.smitcoderx.circuithouseassignment.ui.base

import com.smitcoderx.circuithouseassignment.api.NewsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseRepository @Inject constructor(private val newsApi: NewsApi) {

    suspend fun getTopHeadlines(country: String, category: String) =
        newsApi.getTopHeadlines(country = country, category = category)


    suspend fun getTopHeadlinesCountryWise(country: String) =
        newsApi.getTopHeadlinesCountryWise(country = country)

    suspend fun getTopHeadlinesCategoryWise(category: String) =
        newsApi.getTopHeadlinesCategoryWise(category = category)

}