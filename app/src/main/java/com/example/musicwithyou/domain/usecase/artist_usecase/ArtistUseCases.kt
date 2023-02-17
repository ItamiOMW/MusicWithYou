package com.example.musicwithyou.domain.usecase.artist_usecase

import javax.inject.Inject

class ArtistUseCases @Inject constructor(
    private val getArtistDetailUseCase: GetArtistDetailUseCase,
    private val getArtistPreviewsUseCase: GetArtistPreviewsUseCase,
) {

    suspend fun getArtistPreviews() = getArtistPreviewsUseCase.invoke()

    suspend fun getArtistDetail(id: Long) = getArtistDetailUseCase.invoke(id)

}