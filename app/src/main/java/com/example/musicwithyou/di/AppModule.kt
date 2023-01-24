package com.example.musicwithyou.di

import com.example.musicwithyou.data.local.content_resolver.ContentResolverHelper
import com.example.musicwithyou.data.local.content_resolver.SongContentResolver
import com.example.musicwithyou.data.repository.SongRepositoryImpl
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindSongContentResolver(contentResolver: SongContentResolver): ContentResolverHelper<Song>

    @Binds
    @Singleton
    fun bindSongRepository(songRepository: SongRepositoryImpl): SongRepository


    companion object {

    }

}