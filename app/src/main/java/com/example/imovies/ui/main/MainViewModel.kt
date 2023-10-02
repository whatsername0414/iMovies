package com.example.imovies.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private var _isNewOpen = true
    private var _lastDateVisit = ""

    fun setIsNewOpen(value: Boolean) {
        _isNewOpen = value
    }

    fun getIsNewOpen(): Boolean = _isNewOpen

    fun setLastDateVisit(value: String) {
        _lastDateVisit = value
    }

    fun getLastDateVisit() = _lastDateVisit
}