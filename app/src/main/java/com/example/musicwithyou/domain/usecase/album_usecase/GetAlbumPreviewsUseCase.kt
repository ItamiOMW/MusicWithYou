package com.example.musicwithyou.domain.usecase.album_usecase

import com.example.musicwithyou.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumPreviewsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke() = repository.getAlbumPreviews()

}