package com.example.musicwithyou.presentation.screens

import android.app.Application
import android.support.v4.media.MediaBrowserCompat
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.usecase.playlist_usecase.PlaylistUseCases
import com.example.musicwithyou.media.exoplayer.MediaPlayerServiceConnection
import com.example.musicwithyou.media.service.MediaPlayerService
import com.example.musicwithyou.media.utils.MEDIA_ROOT_ID
import com.example.musicwithyou.media.utils.PLAYBACK_UPDATE_INTERVAL
import com.example.musicwithyou.media.utils.currentPosition
import com.example.musicwithyou.utils.InvalidTitleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    serviceConnection: MediaPlayerServiceConnection,
    private val playlistUseCases: PlaylistUseCases,
    private val application: Application,
) : ViewModel() {

    val songQueue = serviceConnection.songQueue

    val currentPlayingSong = serviceConnection.currentPlayingSong

    val repeatMode = serviceConnection.repeatMode

    val shuffleMode = serviceConnection.shuffleMode

    val isSongPlaying = serviceConnection.isPlaying

    var favoriteSongs by mutableStateOf(emptyList<Song>())
        private set

    var currentPlaybackPosition by mutableStateOf(0L)
        private set

    var currentSongProgress by mutableStateOf(0f)
        private set

    var showCreatePlaylistDialog by mutableStateOf(false)
        private set

    var playlists by mutableStateOf(emptyList<Playlist>())
        private set

    private var songsToCreatePlaylist: List<Song>? = null

    val currentDuration: Long
        get() = MediaPlayerService.currentDuration

    private lateinit var rootMediaId: String

    private val isConnected = serviceConnection.isConnected

    private val playbackState = serviceConnection.playBackState

    private var updatePosition = true

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>,
        ) {
            super.onChildrenLoaded(parentId, children)
        }
    }

    private val serviceConnection = serviceConnection.also {
        updatePlayback()
    }

    init {
        viewModelScope.launch {
            isConnected.collect {
                if (it) {
                    rootMediaId = serviceConnection.rootMediaId
                    serviceConnection.playBackState.value?.apply {
                        currentPlaybackPosition = position
                    }
                    serviceConnection.subscribe(rootMediaId, subscriptionCallback)
                }
            }
        }
        collectFavoriteSongs()
        collectPlaylists()
    }

    fun changeFavoriteState(song: Song) {
        viewModelScope.launch {
            if (favoriteSongs.contains(song)) {
                playlistUseCases.deleteSongsFromFavoritePlaylist(listOf(song))
            } else {
                playlistUseCases.addSongsToFavoritePlaylist(listOf(song))
            }
        }

    }

    fun playSong(song: Song, currentSongList: List<Song>) {
        if (song.id == currentPlayingSong.value?.id) {
            if (isSongPlaying.value) {
                serviceConnection.transportControl.pause()
            } else {
                serviceConnection.transportControl.play()
            }
        } else {
            serviceConnection.playQueue(currentSongList)
            serviceConnection.transportControl.playFromMediaId(song.id.toString(), null)
        }
    }

    fun moveSong(from: Int, to: Int) {
        serviceConnection.moveSong(from, to)
    }

    fun deleteFromQueue(song: Song) {
        serviceConnection.deleteQueueItem(song)
    }

    fun playNext(song: Song) {
        serviceConnection.playNext(song)
    }

    fun playNext(songs: List<Song>) {
        serviceConnection.playNext(songs)
    }

    fun addToQueue(song: Song) {
        serviceConnection.addSongToQueue(song)
    }

    fun addToQueue(songs: List<Song>) {
        serviceConnection.addSongsToQueue(songs)
    }

    fun playShuffled(currentSongList: List<Song>) {
        val shuffledList = currentSongList.shuffled()
        serviceConnection.playQueue(shuffledList)
        serviceConnection.transportControl.playFromMediaId(shuffledList.first().id.toString(), null)
    }

    fun shuffleMode() {
        serviceConnection.shuffle()
    }

    fun repeatMode() {
        serviceConnection.repeat()
    }

    fun skipToNext() {
        serviceConnection.skipToNext()
    }

    fun skipToPrevious() {
        serviceConnection.skipToPrevious()
    }

    fun seekTo(value: Float) {
        serviceConnection.transportControl.seekTo(
            (currentDuration * value / 100f).toLong()
        )
        updatePosition = false
        updatePlayback()
        updatePosition = true
    }

    fun addToPlaylist(
        songs: List<Song>,
        playlist: Playlist,
    ) {
        viewModelScope.launch {
            playlistUseCases.addSongsToPlaylist(
                songs,
                playlist
            )
        }
    }

    fun createPlaylist(
        title: String,
    ) {
        viewModelScope.launch {
            try {
                val playlist = Playlist(
                    title = title,
                    songs = songsToCreatePlaylist ?: emptyList(),
                    createdTimeStamp = System.currentTimeMillis(),
                )
                playlistUseCases.createPlaylist(playlist)
                onDismissCreatePlaylistDialog()
            } catch (e: InvalidTitleException) {
                Toast.makeText(
                    application,
                    application.getString(R.string.title_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onShowCreatePlaylistDialog(songs: List<Song>) {
        songsToCreatePlaylist = songs
        showCreatePlaylistDialog = true
    }

    fun onDismissCreatePlaylistDialog() {
        songsToCreatePlaylist = null
        showCreatePlaylistDialog = false
    }

    private fun collectFavoriteSongs() {
        viewModelScope.launch {
            playlistUseCases.getFavoritePlaylistSongs().collect { list ->
                favoriteSongs = list
            }
        }
    }

    private fun collectPlaylists() {
        viewModelScope.launch {
            playlistUseCases.getPlaylists().collect { list ->
                playlists = list
            }
        }
    }

    private fun updatePlayback() {
        viewModelScope.launch {
            val position = playbackState.value?.currentPosition ?: 0

            if (currentPlaybackPosition != position) {
                currentPlaybackPosition = position
            }

            if (currentDuration > 0) {
                currentSongProgress = (
                        currentPlaybackPosition.toFloat() / currentDuration.toFloat() * 100f)
            }
            delay(PLAYBACK_UPDATE_INTERVAL)

            if (updatePosition) {
                updatePlayback()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unSubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {}
        )
        updatePosition = false
    }
}