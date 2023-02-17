package com.example.musicwithyou.data.local.room.dao

import androidx.room.*
import com.example.musicwithyou.data.local.room.models.*
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>


    @Transaction
    @Query("SELECT * FROM playlist_table WHERE id = :id LIMIT 1")
    suspend fun getPlaylistWithSongsById(id: Long): PlaylistWithSongs?

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE id = :id LIMIT 1")
    fun getPlaylistWithSongsByIdFlow(id: Long): Flow<PlaylistWithSongs?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun deletePlaylist(id: Long)

    @Query(
        """
        UPDATE song_playlist_table SET position =
        CASE
        WHEN position < :from THEN position + 1
        WHEN position > :from THEN position - 1
        ELSE :to
       END
       WHERE playlistId = :playlistId AND position BETWEEN MIN(:from, :to) and MAX(:from, :to)
    """
    )
    suspend fun move(playlistId: Long, from: Int, to: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistCrossRefs(
        songPlaylistCrossRefs: List<SongPlaylistCrossRef>,
    )

    @Delete
    suspend fun deleteSongPlaylistRefs(songPlaylistCrossRefs: List<SongPlaylistCrossRef>)

    @Query("DELETE FROM song_playlist_table WHERE playlistId = :playlistId and songId = :songId")
    suspend fun deleteSongPlaylistRefById(playlistId: Long, songId: Long)

    @Query("DELETE FROM song_playlist_table WHERE songId = :songId")
    suspend fun deleteSongFromSongPlaylistRefs(songId: Long)

    @Transaction
    suspend fun createPlaylist(playlist: PlaylistEntity, songs: List<SongEntity>) {
        val playlistId = insertPlaylist(playlist)
        val crossRefs = songs.mapIndexed { index, song ->
            SongPlaylistCrossRef(song.id, playlistId, index)
        }
        insertSongPlaylistCrossRefs(crossRefs)
    }


}