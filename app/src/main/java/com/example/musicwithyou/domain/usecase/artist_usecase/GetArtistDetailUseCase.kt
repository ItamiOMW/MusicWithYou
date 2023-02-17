package com.example.musicwithyou.domain.usecase.artist_usecase

import com.example.musicwithyou.domain.repository.ArtistRepository
import javax.inject.Inject

class GetArtistDetailUseCase @Inject constructor(
    private val repository: ArtistRepository
) {

    suspend operator fun invoke(id: Long) = repository.getArtistDetailById(id)

}