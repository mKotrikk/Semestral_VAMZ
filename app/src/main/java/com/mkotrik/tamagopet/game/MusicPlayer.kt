package com.mkotrik.tamagopet.game

import android.content.Context
import android.media.MediaPlayer

/** Objekt, pomocou ktorého je možné spúšťať hudbu v hre*/
object MusicPlayer {
    private var mediaplayer : MediaPlayer?= null
    private var timePlayed : Int = 0

    fun playSong(context: Context, source: Int) {
        this.stopSong()
        mediaplayer = MediaPlayer.create(context, source)
        mediaplayer?.isLooping = true
        mediaplayer?.start()

    }

    fun muteSong() {
        mediaplayer?.setVolume(0F, 0F)
    }

    fun unmuteSong() {
        mediaplayer?.setVolume(1F, 1F)
    }

    fun stopSong() {
        mediaplayer?.stop()
    }

    fun pauseSong() {
        mediaplayer?.pause()
        this.timePlayed = mediaplayer?.currentPosition ?: 0
    }

    fun resumeSong() {
        mediaplayer?.seekTo(this.timePlayed)
        mediaplayer?.start()
    }
}