package com.acrcloud.ui.edit

import com.acrcloud.ui.Song

interface MainNavigator {

    fun onApplyEditOpenedSong()

    fun onItemSongSelected(song: Song)

    fun openEditMusicScreen()
}
