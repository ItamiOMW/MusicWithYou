package com.example.musicwithyou.di

import android.app.Application
import com.example.musicwithyou.data.local.room.MusicWithYouDataBase
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

}