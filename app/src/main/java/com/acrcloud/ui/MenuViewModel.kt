package com.acrcloud.ui

import com.acrcloud.ui.base.BaseViewModel
import com.acrcloud.ui.edit.MainNavigator

class MenuViewModel: BaseViewModel<MainNavigator>() {

    fun editMusicScreen(){
        navigator!!.openEditMusicScreen()
    }

    fun listenMusicScreen() {
        navigator!!.openListenMusicScreen()
    }
}