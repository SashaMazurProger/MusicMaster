package com.acrcloud.ui.select;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.acrcloud.ui.databinding.SongItemLayoutBinding;
import com.acrcloud.ui.BR;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private List<SelectMusicViewModel.Song> songs = new ArrayList<>();

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

    public void setSongs(List<SelectMusicViewModel.Song> songs) {
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
        viewHolder.binding.setListener(new SongItemListener() {
            @Override
            public void click(SelectMusicViewModel.Song song) {
                viewModel.editSong(song);
            }

            @Override
            public void editNow(SelectMusicViewModel.Song song) {
                viewModel.editSongNow(song);
            }
        });
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    private static class ObservableSong extends BaseObservable {
        private SelectMusicViewModel.Song song;
        private boolean editing;

        public ObservableSong(SelectMusicViewModel.Song song) {
            this.song = song;
            notifyPropertyChanged(BR.title);
            setIsEditing(true);
        }

        @Bindable
        public String getTitle() {
            return song.getTitle();
        }

        @Bindable
        public boolean getIsEditing() {
            return editing;
        }


        public void setTitle(String title) {
            song.setTitle(title);
            notifyPropertyChanged(BR.title);
        }

        public void setIsEditing(boolean isEditing) {
            editing = isEditing;
            notifyPropertyChanged(BR.isEditing);
        }

    }
}
