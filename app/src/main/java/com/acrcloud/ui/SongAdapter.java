package com.acrcloud.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.acrcloud.ui.databinding.SongItemLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    public List<RenameMusicViewModel.Song> songs = new ArrayList<>();

    private RenameMusicViewModel viewModel;

    public SongAdapter(RenameMusicViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        SongItemLayoutBinding binding = SongItemLayoutBinding.inflate(layoutInflater);
        return new SongHolder(binding);
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
        viewHolder.binding.setListener(song -> {
            viewModel.search(song);
//            viewModel.editSong(song);
        });
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
