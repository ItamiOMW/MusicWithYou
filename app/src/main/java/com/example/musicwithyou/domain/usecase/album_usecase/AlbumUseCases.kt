package com.example.musicwithyou.domain.usecase.album_usecase

import javax.inject.Inject

class AlbumUseCases @Inject constructor(
    private val getAlbumDetailByIdUseCase: GetAlbumDetailByIdUseCase,
    private val getAlbumPreviewsUseCase: GetAlbumPreviewsUseCase,
) {

    suspend fun getAlbumPreviews() = getAlbumPreviewsUseCase.invoke()

    suspend fun getAlbumDetailById(id: Long) = getAlbumDetailByIdUseCase.invoke(id)

}