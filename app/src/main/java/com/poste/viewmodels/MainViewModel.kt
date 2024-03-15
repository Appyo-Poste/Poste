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

class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<ApiResponse?>()
    val data: LiveData<ApiResponse?> = _data

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchData(context: Context) {
        viewModelScope.launch {
            try {
                // Directly receive ApiResponse; exceptions for failures are handled within the repository
                val response = DataRepository.fetchData(context) // Assuming this directly returns ApiResponse
                _data.postValue(response) // Update LiveData with the ApiResponse
                _errorMessage.postValue(null) // Clear previous error messages
            } catch (e: Exception) {
                _data.postValue(null) // Clear previous data
                _errorMessage.postValue(e.message) // Update LiveData to reflect an error state
                Log.e("Dashboard", "Error fetching data", e)
            }
        }
    }
}