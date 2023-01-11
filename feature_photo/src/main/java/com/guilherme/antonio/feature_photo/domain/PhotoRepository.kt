package com.guilherme.antonio.feature_photo.domain

interface PhotoRepository {

    suspend fun getPhotos(): Result<List<PhotoDomain>>

}