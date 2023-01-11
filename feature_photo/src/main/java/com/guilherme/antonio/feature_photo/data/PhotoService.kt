package com.guilherme.antonio.feature_photo.data

import com.guilherme.antonio.feature_photo.base.ApiResult
import retrofit2.http.GET

interface PhotoService {

    @GET("photos?page=1&client_id=52ed5e63ad1915fed2bbfd2326aade6b8549b050fc8367a7c105567476df2a81")
    suspend fun getPhotos(): ApiResult<String>

}