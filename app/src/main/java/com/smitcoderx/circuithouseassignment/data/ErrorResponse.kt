package com.smitcoderx.circuithouseassignment.data


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: String? = null
)