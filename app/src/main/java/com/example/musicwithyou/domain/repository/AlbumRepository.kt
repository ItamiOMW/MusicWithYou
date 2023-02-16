package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.Album

interface AlbumRepository {

    suspend fun getAlbums(): List<Album>

    suspend fun getAlbumById(id: Long): Album?

}