package com.example.musicwithyou.di

import android.app.Application
import com.example.musicwithyou.data.local.room.MusicWithYouDataBase
import com.example.musicwithyou.data.local.room.dao.AlbumDao
import com.example.musicwithyou.data.local.room.dao.ArtistDao
import com.example.musicwithyou.data.local.room.dao.PlaylistDao
import com.example.musicwithyou.data.local.room.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePlaylistDao(
        application: Application,
    ): PlaylistDao = MusicWithYouDataBase.getInstance(application).playListDao()

    @Provides
    @Singleton
    fun provideSongDao(
        application: Application,
    ): SongDao = MusicWithYouDataBase.getInstance(application).songDao()

    @Provides
    @Singleton
    fun provideAlbumDao(
        application: Application,
    ): AlbumDao = MusicWithYouDataBase.getInstance(application).albumDao()

    @Provides
    @Singleton
    fun provideArtistDao(
        application: Application,
    ): ArtistDao = MusicWithYouDataBase.getInstance(application).artistDao()

}