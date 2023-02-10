package com.example.musicwithyou.di

import android.content.Context
import com.example.musicwithyou.data.local.content_resolver.ContentResolverHelper
import com.example.musicwithyou.data.local.content_resolver.SongContentResolver
import com.example.musicwithyou.data.repository.SongRepositoryImpl
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.SongRepository
import com.example.musicwithyou.media.exoplayer.MediaPlayerServiceConnection
import com.example.musicwithyou.media.exoplayer.MediaSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun bindSongContentResolver(
        contentResolver: SongContentResolver,
    ): ContentResolverHelper<Song> = contentResolver

    @Provides
    @Singleton
    fun bindSongRepository(
        songRepository: SongRepositoryImpl,
    ): SongRepository = songRepository

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: MediaSource,
    ): MediaPlayerServiceConnection = MediaPlayerServiceConnection(
        context = context,
        mediaSource = mediaSource,
    )

}