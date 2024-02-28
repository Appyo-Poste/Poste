package com.poste.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poste.models.ApiResponse
import com.poste.repositories.DataRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _data = MutableLiveData<ApiResponse>()
    val data: LiveData<ApiResponse> = _data
    private val repository = DataRepository

    fun fetchData(context: Context) {
        viewModelScope.launch {
            try {
                val response = repository.fetchData(context)
                _data.value = response
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching data", e)
            }
        }
    }
}