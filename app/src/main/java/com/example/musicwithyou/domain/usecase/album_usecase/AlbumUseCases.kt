package com.example.musicwithyou.domain.usecase.album_usecase

import javax.inject.Inject

class AlbumUseCases @Inject constructor(
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase,
) {

    suspend fun getAlbums() = getAlbumsUseCase.invoke()

    suspend fun getAlbumById(id: Long) = getAlbumByIdUseCase.invoke(id)

}