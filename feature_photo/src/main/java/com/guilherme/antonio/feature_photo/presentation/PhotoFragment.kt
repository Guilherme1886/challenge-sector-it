package com.guilherme.antonio.feature_photo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guilherme.antonio.feature_photo.R
import com.guilherme.antonio.feature_photo.RetrofitConfig
import com.guilherme.antonio.feature_photo.data.PhotoRepositoryImpl
import com.guilherme.antonio.feature_photo.data.PhotoService
import com.guilherme.antonio.feature_photo.databinding.FragmentPhotoBinding
import com.guilherme.antonio.feature_photo.domain.GetPhotosUseCase

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    private var binding: FragmentPhotoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        val repository = PhotoRepositoryImpl(RetrofitConfig.getClient().create(PhotoService::class.java))
        val useCase = GetPhotosUseCase(repository)
        val viewModel = PhotoViewModel(useCase)
        viewModel.getPhotos()
    }

}