package com.example.musicwithyou.media.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.musicwithyou.R
import com.example.musicwithyou.media.exoplayer.MediaPlayerNotificationManager
import com.example.musicwithyou.media.exoplayer.MediaSource
import com.example.musicwithyou.media.utils.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject


@AndroidEntryPoint
class MediaPlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: CacheDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaSource: MediaSource

    private val serviceJob = SupervisorJob()

    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat

    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var mediaPlayerNotificationManager: MediaPlayerNotificationManager

    private var currentPlayingMedia: MediaMetadataCompat? = null

    private var isForegroundService: Boolean = false

    companion object {

        private const val TAG = "MediaPlayerService"

        var currentDuration: Long = 0L
            private set

    }

    override fun onCreate() {
        super.onCreate()
        val sessionActivityIntent = packageManager
            ?.getLaunchIntentForPackage(packageName)
            ?.let { sessionIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    sessionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

        mediaSession = MediaSessionCompat(this, TAG).apply {
            setSessionActivity(sessionActivityIntent)
            setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        mediaPlayerNotificationManager = MediaPlayerNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
            )
            setPlaybackPreparer(AudioMediaPlayBackPreparer())
            setQueueNavigator(MediaQueueNavigator(mediaSession))
            setQueueEditor(MediaSessionConnectorQueueHandler())
            setPlayer(exoPlayer)
        }

        mediaPlayerNotificationManager.showNotification(exoPlayer)

    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }


    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = mediaSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(mediaSource.asMediaItem())
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }

            }
            else -> Unit
        }
    }


    override fun onCustomAction(
        action: String,
        extras: Bundle?,
        result: Result<Bundle>,
    ) {
        super.onCustomAction(action, extras, result)
        when (action) {
            START_MEDIA_PLAY_ACTION -> {
                mediaPlayerNotificationManager.showNotification(exoPlayer)
            }
            REFRESH_MEDIA_PLAY_ACTION -> {
                mediaSource.refresh()
                notifyChildrenChanged(MEDIA_ROOT_ID)
            }
            SET_SONG_AS_NEXT_ACTION -> {
                val metadata =
                    extras?.getParcelable(MEDIA_METADATA_KEY) as MediaMetadataCompat?
                        ?: return
                val item = mediaSource.audioMediaMetaData.value.find {
                    it.description.mediaId == metadata.description.mediaId
                }
                if (item != null) {
                    val index = mediaSource.audioMediaMetaData.value.indexOf(item)
                    exoPlayer.removeMediaItem(index)
                    mediaSource.deleteMedia(metadata)
                }
                metadata.description.mediaUri?.let { MediaItem.fromUri(it) }
                    ?.let { exoPlayer.addMediaItem(exoPlayer.currentMediaItemIndex + 1, it) }
                mediaSource.addMedia(metadata, exoPlayer.currentMediaItemIndex + 1)
            }
            SET_SONGS_AS_NEXT_ACTION -> {
                val metadataArray = extras?.getParcelableArray(
                    MEDIA_METADATA_ARRAY_KEY
                ) as Array<*>? ?: return
                metadataArray.forEach {
                    val metadata = it as MediaMetadataCompat
                    val item = mediaSource.audioMediaMetaData.value.find {
                        it.description.mediaId == metadata.description.mediaId
                    }
                    if (item != null) {
                        val index = mediaSource.audioMediaMetaData.value.indexOf(item)
                        exoPlayer.removeMediaItem(index)
                        mediaSource.deleteMedia(metadata)
                    }
                    metadata.description.mediaUri?.let { MediaItem.fromUri(it) }
                        ?.let { exoPlayer.addMediaItem(exoPlayer.currentMediaItemIndex + 1, it) }
                    mediaSource.addMedia(metadata, exoPlayer.currentMediaItemIndex + 1)
                }
            }
            MOVE_SONG_ACTION -> {
                val fromIndex = extras?.getInt(MOVE_FROM_KEY) ?: return
                val toIndex = extras.getInt(MOVE_TO_KEY)
                exoPlayer.moveMediaItem(fromIndex, toIndex)
                mediaSource.moveMedia(fromIndex, toIndex)
            }
            else -> Unit
        }

    }

    inner class PlayerNotificationListener : PlayerNotificationManager.NotificationListener {

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isForegroundService = false
            stopSelf()
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean,
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(
                        applicationContext,
                        this@MediaPlayerService.javaClass
                    )
                )
                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }
    }

    inner class MediaSessionConnectorQueueHandler : MediaSessionConnector.QueueEditor {
        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?,
        ): Boolean {
            return true
        }

        override fun onAddQueueItem(player: Player, description: MediaDescriptionCompat) {
            val metadata = description.toMediaMetadata()
            description.mediaUri?.let { MediaItem.fromUri(it) }?.let { exoPlayer.addMediaItem(it) }
            mediaSource.addMedia(metadata)
        }

        override fun onAddQueueItem(
            player: Player,
            description: MediaDescriptionCompat,
            index: Int,
        ) {
            val metadata = description.toMediaMetadata()
            description.mediaUri?.let { MediaItem.fromUri(it) }
                ?.let { exoPlayer.addMediaItem(index, it) }
            mediaSource.addMedia(metadata, index)
        }

        override fun onRemoveQueueItem(player: Player, description: MediaDescriptionCompat) {
            val mediaMetadata = mediaSource.audioMediaMetaData.value.first {
                it.description.mediaId == description.mediaId
            }
            val index = mediaSource.audioMediaMetaData.value.indexOf(mediaMetadata)
            exoPlayer.removeMediaItem(index)
            mediaSource.deleteMedia(mediaMetadata)
        }

    }

    inner class AudioMediaPlayBackPreparer : MediaSessionConnector.PlaybackPreparer {

        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?,
        ): Boolean {
            return true
        }

        override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

        override fun onPrepare(playWhenReady: Boolean) = Unit

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?,
        ) {
            mediaSource.whenReady {

                val itemToPlay = mediaSource.audioMediaMetaData.value.find {
                    it.description.mediaId == mediaId
                }

                currentPlayingMedia = itemToPlay

                preparePlayer(
                    mediaMetadata = mediaSource.audioMediaMetaData.value,
                    itemToPlay = itemToPlay,
                    playWhenReady = playWhenReady
                )
            }
        }

        override fun onPrepareFromSearch(
            query: String,
            playWhenReady: Boolean,
            extras: Bundle?,
        ) = Unit

        override fun onPrepareFromUri(
            uri: Uri,
            playWhenReady: Boolean,
            extras: Bundle?,
        ) = Unit

    }

    inner class MediaQueueNavigator(
        mediaSessionCompat: MediaSessionCompat,
    ) : TimelineQueueNavigator(mediaSessionCompat) {

        override fun getMediaDescription(
            player: Player,
            windowIndex: Int,
        ): MediaDescriptionCompat {

            if (windowIndex < mediaSource.audioMediaMetaData.value.size) {
                return mediaSource.audioMediaMetaData.value[windowIndex].description
            }

            return MediaDescriptionCompat.Builder().build()

        }

    }

    private fun preparePlayer(
        mediaMetadata: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
    ) {
        val indexToPlay = if (currentPlayingMedia == null) 0
        else mediaMetadata.indexOf(itemToPlay)

        val items: List<MediaItem> = mediaMetadata.map {
            it.description.mediaUri?.let { it1 -> MediaItem.fromUri(it1) }!!
        }

        exoPlayer.addListener(PlayerEventListener())
        exoPlayer.setMediaItems(items)
        exoPlayer.prepare()
        exoPlayer.seekTo(indexToPlay, 0)
        exoPlayer.playWhenReady = playWhenReady

    }


    private inner class PlayerEventListener : Player.Listener {


        override fun onPlaybackStateChanged(playbackState: Int) {

            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY,
                -> {
                    mediaPlayerNotificationManager.showNotification(exoPlayer)
                }
                else -> {
                    mediaPlayerNotificationManager.hideNotification()
                }
            }

        }


        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            currentDuration = player.duration
        }

        override fun onPlayerError(error: PlaybackException) {

            var message = R.string.generic_error

            if (error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
                message = R.string.error_media_not_found
            }

            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

        }


    }

}