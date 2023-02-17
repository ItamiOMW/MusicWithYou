package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.AlbumDetail
import com.example.musicwithyou.domain.models.AlbumPreview

interface AlbumRepository {

    suspend fun getAlbumPreviews(): List<AlbumPreview>

    suspend fun getAlbumDetailById(id: Long): AlbumDetail?

}