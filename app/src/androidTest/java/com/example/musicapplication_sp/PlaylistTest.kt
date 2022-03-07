package com.example.musicapplication_sp

import com.example.musicapplication_sp.activities.SonglistActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class PlaylistTest {
    private val insertSong = SonglistActivity()

    @Test
    fun insertSong(){

        val userid = "11"
        val userSong = "Song"

        assertEquals(insertSong.postListOfSongs(postSongsModel))
    }
}