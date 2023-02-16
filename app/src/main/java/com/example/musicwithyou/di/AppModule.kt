package com.example.musicwithyou.di

import android.content.Context
import com.example.musicwithyou.data.local.content_resolver.AlbumContentResolver
import com.example.musicwithyou.data.local.content_resolver.ContentResolverHelper
import com.example.musicwithyou.data.local.content_resolver.SongContentResolver
import com.example.musicwithyou.data.local.room.models.AlbumEntity
import com.example.musicwithyou.data.local.room.models.SongEntity
import com.example.musicwithyou.data.repository.AlbumRepositoryImpl
import com.example.musicwithyou.data.repository.PlaylistRepositoryImpl
import com.example.musicwithyou.data.repository.SongRepositoryImpl
import com.example.musicwithyou.domain.repository.AlbumRepository
import com.example.musicwithyou.domain.repository.PlaylistRepository
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
    fun provideSongContentResolver(
        contentResolver: SongContentResolver,
    ): ContentResolverHelper<SongEntity> = contentResolver

    @Provides
    @Singleton
    fun provideAlbumContentResolver(
        contentResolver: AlbumContentResolver,
    ): ContentResolverHelper<AlbumEntity> = contentResolver

    @Provides
    @Singleton
    fun provideSongRepository(
        songRepositoryImpl: SongRepositoryImpl,
    ): SongRepository = songRepositoryImpl

    @Provides
    @Singleton
    fun providePlaylistRepository(
        playlistRepositoryImpl: PlaylistRepositoryImpl,
    ): PlaylistRepository = playlistRepositoryImpl

    @Provides
    @Singleton
    fun provideAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl,
    ): AlbumRepository = albumRepositoryImpl

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