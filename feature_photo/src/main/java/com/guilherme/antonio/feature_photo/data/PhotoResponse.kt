package com.guilherme.antonio.feature_photo.data

data class PhotoResponse(
    val photos: List<Photo>? = null
)

data class Photo(
    val id: String? = null,
    val urls: Urls? = null
)

data class Urls(
    val raw: String? = null,
    val full: String? = null,
    val regular: String? = null,
    val small: String? = null,
    val thumb: String? = null,
    val small_s3: String? = null,
)