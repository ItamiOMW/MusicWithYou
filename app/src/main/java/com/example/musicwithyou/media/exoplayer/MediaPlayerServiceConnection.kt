package com.example.musicwithyou.media.exoplayer

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.mutableStateOf
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.media.service.MediaPlayerService
import com.example.musicwithyou.media.utils.REFRESH_MEDIA_PLAY_ACTION
import com.example.musicwithyou.media.utils.START_MEDIA_PLAY_ACTION
import com.example.musicwithyou.media.utils.currentPosition
import com.example.musicwithyou.media.utils.isPlaying
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class MediaPlayerServiceConnection @Inject constructor(
    private val mediaSource: MediaSource,
    context: Context,
) {

    private val _playBackState: MutableStateFlow<PlaybackStateCompat?> = MutableStateFlow(null)
    val playBackState: StateFlow<PlaybackStateCompat?>
        get() = _playBackState

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected

    var currentPlayingSong = mutableStateOf<Song?>(null)
        private set

    var isPlaying = mutableStateOf(false)
        private set

    val rootMediaId: String
        get() = mediaBrowser.root

    val transportControl: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls

    var repeatMode = mutableStateOf(PlaybackStateCompat.REPEAT_MODE_NONE)
        private set

    var shuffleMode = mutableStateOf(PlaybackStateCompat.SHUFFLE_MODE_NONE)
        private set

    private lateinit var mediaControllerCompat: MediaControllerCompat

    private var songList = mutableListOf<Song>()

    private val mediaBrowserServiceCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlayerService::class.java),
        mediaBrowserServiceCallback,
        null
    ).apply {
        connect()
    }

    fun playAudio(songs: List<Song>) {
        songList = songs.toMutableList()
        mediaSource.loadData(songs)
        mediaBrowser.sendCustomAction(START_MEDIA_PLAY_ACTION, null, null)
    }


    fun fastForward(seconds: Int = 10) {
        playBackState.value?.currentPosition?.let {
            transportControl.seekTo(it + seconds * 1000)
        }
    }

    fun rewind(seconds: Int = 10) {
        playBackState.value?.currentPosition?.let {
            transportControl.seekTo(it - seconds * 1000)
        }
    }

    fun repeat() {
        repeatMode.value = when (repeatMode.value) {
            PlaybackStateCompat.REPEAT_MODE_NONE -> PlaybackStateCompat.REPEAT_MODE_ALL
            PlaybackStateCompat.REPEAT_MODE_ALL -> PlaybackStateCompat.REPEAT_MODE_ONE
            else -> PlaybackStateCompat.REPEAT_MODE_NONE
        }
        transportControl.setRepeatMode(repeatMode.value)
    }

    fun shuffle() {
        shuffleMode.value = when (shuffleMode.value) {
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> PlaybackStateCompat.SHUFFLE_MODE_ALL
            else -> PlaybackStateCompat.SHUFFLE_MODE_NONE
        }
        transportControl.setShuffleMode(shuffleMode.value)
    }

    fun skipToNext() {
        transportControl.skipToNext()
    }

    fun skipToPrevious() {
        transportControl.skipToPrevious()
    }

    fun subscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback,
    ) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unSubscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback,
    ) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser.sendCustomAction(REFRESH_MEDIA_PLAY_ACTION, null, null)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context,
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            _isConnected.value = true
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallBack())
            }
        }

        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    private inner class MediaControllerCallBack() : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playBackState.value = state
            isPlaying.value = state?.isPlaying == true
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            currentPlayingSong.value = metadata?.let { data ->
                songList.find { song ->
                    song.id.toString() == data.description.mediaId
                }
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserServiceCallback.onConnectionSuspended()
        }

    }

}