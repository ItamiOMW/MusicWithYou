package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.Song

interface SongRepository {

    suspend fun getSongs(): List<Song>

    suspend fun getSongsByIds(songIds: List<Long>): List<Song>

}