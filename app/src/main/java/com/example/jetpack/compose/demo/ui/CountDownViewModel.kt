package com.example.jetpack.compose.demo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class CountDownViewModel : ViewModel() {

    private val _countDownValue = MutableStateFlow(60)
    val countdownValue: StateFlow<Int> = _countDownValue

    fun startCountDown(){
        viewModelScope.launch {
            while (countdownValue.value >=0){
                _countDownValue.updateAndGet { it - 1 }
                delay(10000)
                Log.d("APP_TAG", "countdownValue.value: ${countdownValue.value}")
            }
        }
    }
}