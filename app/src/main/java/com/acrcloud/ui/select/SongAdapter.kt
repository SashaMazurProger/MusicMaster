package com.acrcloud.ui.select

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.acrcloud.ui.Song
import com.acrcloud.ui.databinding.SongItemLayoutBinding


class SongAdapter(val viewModel: SelectMusicViewModel) : RecyclerView.Adapter<SongAdapter.SongHolder>() {

    companion object {
        const val FOLDER_ITEM = 1
        const val SONG_ITEM = 2
    }


    private var songs = ArrayList<Song>()

    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): SongHolder {

        var binding = SongItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context))
        return SongHolder(binding)
    }

    fun setSongs(songs: List<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

    }

    class SongHolder(val binding: SongItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(@NonNull viewHolder: SongHolder, i: Int) {

        viewHolder.binding.song = songs.get(i)
        if (getItemViewType(i) == SONG_ITEM) {
            viewHolder.binding.listener = object : SongItemListener {
                override fun click(song: Song) {
                    viewModel.onItemSelected(song)
                }

                override fun editNow(song: Song) {
                    viewModel.editSelectedSongNow(song)
                }
            }
        } else {
            viewHolder.binding.listener = object : SongItemListener {
                override fun click(song: Song) {
                    viewModel.onItemSelected(song)
                }

                override fun editNow(song: Song) {
                    viewModel.editSelectedFolderNow(song)
                }
            }
        }

        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return when (songs[position].type) {
            Song.TYPE.SONG -> SONG_ITEM
            else -> FOLDER_ITEM
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}
