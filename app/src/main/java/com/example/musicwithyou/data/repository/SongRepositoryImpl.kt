package com.example.musicwithyou.data.repository

import android.app.Application
import com.example.musicwithyou.data.local.content_resolver.ContentResolverHelper
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SongRepositoryImpl @Inject constructor(
    private val application: Application,
    private val contentResolver: ContentResolverHelper<Song>,
) : SongRepository {

    override suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        contentResolver.getData()
    }

}