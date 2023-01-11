package com.guilherme.antonio.feature_photo.data

import android.util.Log
import com.guilherme.antonio.feature_photo.base.ApiResult
import com.guilherme.antonio.feature_photo.domain.PhotoDomain
import com.guilherme.antonio.feature_photo.domain.PhotoRepository

class PhotoRepositoryImpl(
    private val photoService: PhotoService
) : PhotoRepository {

    override suspend fun getPhotos(): Result<List<PhotoDomain>> =
        when (val apiResult = photoService.getPhotos()) {
            is ApiResult.Success -> {
                Log.d("response", apiResult.data)
//                val photos = apiResult.data.photos?.map { PhotoDomain(it.urls?.small) } ?: emptyList()
                Result.success(emptyList())
            }
            is ApiResult.Error -> {
                Result.failure(Exception())
            }
            is ApiResult.Exception -> {
                Result.failure(Exception())
            }
        }
}