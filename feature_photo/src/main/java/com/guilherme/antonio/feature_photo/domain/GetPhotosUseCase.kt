package com.guilherme.antonio.feature_photo.domain

class GetPhotosUseCase(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(): Result<List<PhotoDomain>> = photoRepository.getPhotos()

}