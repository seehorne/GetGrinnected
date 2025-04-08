package com.example.myapplication.ui.theme

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.launch
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.api.Constant

class EventViewModel :ViewModel() {
    private val eventApi = RetrofitInstance.eventApi

    @OptIn(UnstableApi::class)
    fun getData(title : String){
        viewModelScope.launch{
            val response = eventApi.getEvent(Constant.apiKey,title)
            if (response.isSucessful){
                Log.i("Response : ",response.body().toString())
            }
            else {
                Log.i("Error : ", response.message())
            }
        }

    }
}