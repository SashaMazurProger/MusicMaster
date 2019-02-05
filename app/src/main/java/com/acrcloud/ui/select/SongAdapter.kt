package com.acrcloud.ui.select

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.acrcloud.ui.EditSong
import com.acrcloud.ui.databinding.SongItemLayoutBinding


class SongAdapter(val viewModel: SelectMusicViewModel) : RecyclerView.Adapter<SongAdapter.SongHolder>() {

    companion object {
        const val FOLDER_ITEM = 1
        const val SONG_ITEM = 2
    }


    private var songs = ArrayList<EditSong>()

    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): SongHolder {

        var binding = SongItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context))
        return SongHolder(binding)
    }

    fun setSongs(editSongs: List<EditSong>) {
        this.songs.clear()
        this.songs.addAll(editSongs)

    }

    class SongHolder(val binding: SongItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(@NonNull viewHolder: SongHolder, i: Int) {

        viewHolder.binding.editSong = songs.get(i)
        if (getItemViewType(i) == SONG_ITEM) {
            viewHolder.binding.listener = object : SongItemListener {
                override fun click(editSong: EditSong) {
                    viewModel.onItemSelected(editSong)
                }

                override fun editNow(editSong: EditSong) {
                    viewModel.editSelectedSongNow(editSong)
                }
            }
        } else {
            viewHolder.binding.listener = object : SongItemListener {
                override fun click(editSong: EditSong) {
                    viewModel.onItemSelected(editSong)
                }

                override fun editNow(editSong: EditSong) {
                    viewModel.editSelectedFolderNow(editSong)
                }
            }
        }

        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return when (songs[position].type) {
            EditSong.TYPE.SONG -> SONG_ITEM
            else -> FOLDER_ITEM
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}
