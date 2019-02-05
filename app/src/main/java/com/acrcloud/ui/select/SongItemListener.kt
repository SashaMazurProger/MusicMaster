package com.acrcloud.ui.select

import com.acrcloud.ui.EditSong

interface SongItemListener {

    fun click(editSong: EditSong)

    fun editNow(editSong: EditSong)
}
