package com.guilherme.antonio.feature_photo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.antonio.feature_photo.domain.GetPhotosUseCase
import kotlinx.coroutines.launch

class PhotoViewModel(private val getPhotosUseCase: GetPhotosUseCase) : ViewModel() {

    fun getPhotos() {
        viewModelScope.launch {
            getPhotosUseCase().also { result ->
                when {
                    result.isSuccess -> {

                    }
                    result.isFailure -> {}
                }
            }
        }
    }

}