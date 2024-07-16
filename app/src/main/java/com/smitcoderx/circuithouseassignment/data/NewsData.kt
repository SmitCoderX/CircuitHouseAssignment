package com.smitcoderx.circuithouseassignment.data


import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("articles")
    var articles: List<Article?>? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("totalResults")
    var totalResults: Int? = null
)