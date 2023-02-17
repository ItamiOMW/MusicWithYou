package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.ArtistDetail
import com.example.musicwithyou.domain.models.ArtistPreview

interface ArtistRepository {

    suspend fun getArtistPreviews(): List<ArtistPreview>

    suspend fun getArtistDetailById(id: Long): ArtistDetail?

}