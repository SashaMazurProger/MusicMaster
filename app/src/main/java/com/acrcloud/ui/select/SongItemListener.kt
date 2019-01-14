package com.acrcloud.ui.select

import com.acrcloud.ui.Song

interface SongItemListener {

    fun click(song: Song)

    fun editNow(song: Song)
}
