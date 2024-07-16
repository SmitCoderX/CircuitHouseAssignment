package com.smitcoderx.circuithouseassignment.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.circuithouseassignment.data.NewsData
import com.smitcoderx.circuithouseassignment.utils.Constants.TAG
import com.smitcoderx.circuithouseassignment.utils.ResponseState
import com.smitcoderx.circuithouseassignment.utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(private val repository: BaseRepository) : ViewModel() {

    private val _topHeadlinesLiveData = MutableLiveData<ResponseState<NewsData>>()
    val topHeadlines: LiveData<ResponseState<NewsData>> = _topHeadlinesLiveData
    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()


    fun getTopHeadlines(country: String, category: String) = viewModelScope.launch {
        Log.d(TAG, "getTopHeadlines: $country $category")
        if (isNetworkConnectedLiveData.value == false) {
            _topHeadlinesLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _topHeadlinesLiveData.value = ResponseState.Loading()
        try {

            val response = if (country.isNotEmpty() && category.isNotEmpty()) {
                repository.getTopHeadlines(country, category)
            } else {
                if (country.isNotEmpty()) {
                    repository.getTopHeadlinesCountryWise(country = country)
                } else {
                    repository.getTopHeadlinesCategoryWise(category = category)
                }
            }

            if (response.isSuccessful && response.body()?.status == "ok") {
                _topHeadlinesLiveData.value = ResponseState.Success(response.body()!!)
            } else {
                _topHeadlinesLiveData.value =
                    ResponseState.Error(errorResponse(response)?.message.toString())
            }
        } catch (e: HttpException) {
            _topHeadlinesLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _topHeadlinesLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

}