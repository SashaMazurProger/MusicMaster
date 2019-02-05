package com.acrcloud.ui.edit

import com.acrcloud.ui.EditSong

interface MainNavigator {

    fun onApplyEditOpenedSong()

    fun onItemSongSelected(editSong: EditSong)

    fun openEditMusicScreen()

    fun openListenMusicScreen()
}
