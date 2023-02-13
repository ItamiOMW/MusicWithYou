package com.example.musicwithyou.presentation

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.media.exoplayer.MediaPlayerServiceConnection
import com.example.musicwithyou.media.service.MediaPlayerService
import com.example.musicwithyou.media.utils.MEDIA_ROOT_ID
import com.example.musicwithyou.media.utils.PLAYBACK_UPDATE_INTERVAL
import com.example.musicwithyou.media.utils.currentPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    serviceConnection: MediaPlayerServiceConnection,
) : ViewModel() {

    val songQueue = serviceConnection.songQueue

    val currentPlayingSong = serviceConnection.currentPlayingSong

    val repeatMode = serviceConnection.repeatMode

    val shuffleMode = serviceConnection.shuffleMode

    var currentPlaybackPosition by mutableStateOf(0L)
        private set

    val isSongPlaying = serviceConnection.isPlaying

    val currentDuration: Long
        get() = MediaPlayerService.currentDuration

    var currentSongProgress = mutableStateOf(0f)
        private set

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

    fun addToQueue(song: Song) {
        serviceConnection.addSongToQueue(song)
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

    private fun updatePlayback() {
        viewModelScope.launch {
            val position = playbackState.value?.currentPosition ?: 0

            if (currentPlaybackPosition != position) {
                currentPlaybackPosition = position
            }

            if (currentDuration > 0) {
                currentSongProgress.value = (
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