package com.acrcloud.ui.edit;

import com.acrcloud.ui.Song;

public interface MainNavigator {

    void onApplyEditOpenedSong();

    void onItemSongSelected(Song song);

    void openEditMusicScreen();
}
