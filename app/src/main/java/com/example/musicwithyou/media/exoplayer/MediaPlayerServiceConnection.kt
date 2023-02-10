package com.example.musicwithyou.media.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.mutableStateOf
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.media.service.MediaPlayerService
import com.example.musicwithyou.media.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

    val songQueue = mutableStateOf<List<Song>>(emptyList())

    private lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserServiceCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlayerService::class.java),
        mediaBrowserServiceCallback,
        null
    ).apply {
        connect()
    }

    init {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            mediaSource.audioMediaMetaData.map {
                it.map { metadata -> metadata.toSong() }
            }.collect { list ->
                songQueue.value = list
            }
        }
    }

    fun playQueue(songs: List<Song>) {
        songQueue.value = songs.toMutableList()
        mediaSource.loadData(songs)
        mediaBrowser.sendCustomAction(START_MEDIA_PLAY_ACTION, null, null)
    }

    fun addSongToQueue(song: Song) {
        if (songQueue.value.isEmpty()) {
            val newList = songQueue.value.toMutableList()
            newList.add(song)
            mediaSource.loadData(newList)
            transportControl.playFromMediaId(song.id.toString(), null)
        } else {
            if (songQueue.value.contains(song)) {
                return
            } else {
                val descriptionWithExtras = song.toMediaMetadata().toMediaDescription()
                mediaControllerCompat.addQueueItem(descriptionWithExtras)
            }
        }
    }

    fun deleteQueueItem(song: Song) {
        if (!songQueue.value.contains(song)) {
            return
        } else {
            val descriptionWithExtras = song.toMediaMetadata().toMediaDescription()
            mediaControllerCompat.removeQueueItem(descriptionWithExtras)
        }
    }

    fun playNext(song: Song) {
        if (songQueue.value.isEmpty()) {
            val newList = songQueue.value.toMutableList()
            newList.add(song)
            mediaSource.loadData(newList)
            transportControl.playFromMediaId(song.id.toString(), null)
            return
        }
        if (currentPlayingSong.value == song) {
            return
        }
        val metadata = song.toMediaMetadata()
        mediaBrowser.sendCustomAction(
            SET_SONG_AS_NEXT_ACTION,
            Bundle().apply {
                putParcelable(MEDIA_METADATA_KEY, metadata)
            },
            null
        )
    }

    fun moveSong(from: Int, to: Int) {
        mediaBrowser.sendCustomAction(
            MOVE_SONG_ACTION,
            Bundle().apply {
                putInt(MOVE_FROM_KEY, from)
                putInt(MOVE_TO_KEY, to)
            },
            null
        )
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

    private inner class MediaControllerCallBack : MediaControllerCompat.Callback() {

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            super.onQueueChanged(queue)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playBackState.value = state
            isPlaying.value = state?.isPlaying == true
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            currentPlayingSong.value = metadata?.let { data ->
                songQueue.value.find { song ->
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