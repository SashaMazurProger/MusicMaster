package com.acrcloud.ui.select;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.acrcloud.ui.Song;
import com.acrcloud.ui.databinding.SongItemLayoutBinding;

import java.util.ArrayList;
import java.util.List;

import static com.acrcloud.ui.Song.TYPE.SONG;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private static final int FOLDER_ITEM = 1;
    private static final int SONG_ITEM = 2;

    private List<Song> songs = new ArrayList<>();

    private SelectMusicViewModel viewModel;

    public SongAdapter(SelectMusicViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        SongItemLayoutBinding binding = SongItemLayoutBinding.inflate(layoutInflater);
        return new SongHolder(binding);
    }

    public void setSongs(List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);

    }

    class SongHolder extends RecyclerView.ViewHolder {
        SongItemLayoutBinding binding;

        public SongHolder(@NonNull SongItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder viewHolder, int i) {

        viewHolder.binding.setSong(songs.get(i));
        if (getItemViewType(i) == SONG_ITEM) {
            viewHolder.binding.setListener(new SongItemListener() {
                @Override
                public void click(Song song) {
                    viewModel.onItemSelected(song);
                }

                @Override
                public void editNow(Song song) {
                    viewModel.editSelectedSongNow(song);
                }
            });
        } else {
            viewHolder.binding.setListener(new SongItemListener() {
                @Override
                public void click(Song song) {
                    viewModel.onItemSelected(song);
                }

                @Override
                public void editNow(Song song) {
                    viewModel.editSelectedFolderNow(song);
                }
            });
        }
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        return songs.get(position).getType() == SONG ? SONG_ITEM : FOLDER_ITEM;
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

}
