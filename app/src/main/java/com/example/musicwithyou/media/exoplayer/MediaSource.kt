package com.example.musicwithyou.media.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.media.utils.toMediaMetadata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaSource @Inject constructor() {

    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()

    private val _audioMediaMetaData: MutableStateFlow<List<MediaMetadataCompat>> =
        MutableStateFlow(emptyList())
    val audioMediaMetaData: StateFlow<List<MediaMetadataCompat>>
        get() = _audioMediaMetaData

    private var state: AudioSourceState = AudioSourceState.STATE_CREATED
        set(value) {
            if (value == AudioSourceState.STATE_CREATED
                || value == AudioSourceState.STATE_ERROR
            ) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener: OnReadyListener ->
                        listener.invoke(isReady)
                    }
                }
            } else {
                field = value
            }
        }

    fun moveMedia(from: Int, to: Int) {
        val newList = audioMediaMetaData.value.toMutableList()
        val item = newList[from]
        newList.removeAt(from)
        newList.add(to, item)
        _audioMediaMetaData.value = newList
    }

    fun addMedia(mediaMetadataCompat: MediaMetadataCompat) {
        val newList = audioMediaMetaData.value.toMutableList()
        newList.add(mediaMetadataCompat)
        _audioMediaMetaData.value = newList
    }

    fun addMedia(mediaMetadataCompat: MediaMetadataCompat, index: Int) {
        val newList = audioMediaMetaData.value.toMutableList()
        newList.add(index, mediaMetadataCompat)
        _audioMediaMetaData.value = newList
    }

    fun deleteMedia(mediaMetadataCompat: MediaMetadataCompat) {
        val itemToDelete = audioMediaMetaData.value.find {
            it.description.mediaId == mediaMetadataCompat.description.mediaId
        }
        val newList = audioMediaMetaData.value.toMutableList()
        val index = newList.indexOf(itemToDelete)
        newList.removeAt(index)
        _audioMediaMetaData.value = newList
    }

    fun loadData(data: List<Song>) {
        state = AudioSourceState.STATE_INITIALIZING
        _audioMediaMetaData.value = data.map { song ->
            song.toMediaMetadata()
        }
        state = AudioSourceState.STATE_INITIALIZED
    }

    fun asMediaItem() = audioMediaMetaData.value.map { metaData ->

        val description = MediaDescriptionCompat.Builder()
            .setMediaId(metaData.description.mediaId)
            .setSubtitle(metaData.description.title)
            .setMediaUri(metaData.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE or FLAG_BROWSABLE)

    }.toMutableList()

    fun refresh() {
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }

    fun whenReady(listener: OnReadyListener): Boolean {
        return if (
            state == AudioSourceState.STATE_CREATED
            || state == AudioSourceState.STATE_INITIALIZING
        ) {
            onReadyListeners += listener
            false
        } else {
            listener.invoke(isReady)
            true
        }
    }

    private val isReady: Boolean
        get() = state == AudioSourceState.STATE_INITIALIZED

}

typealias OnReadyListener = (Boolean) -> Unit

enum class AudioSourceState {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}
