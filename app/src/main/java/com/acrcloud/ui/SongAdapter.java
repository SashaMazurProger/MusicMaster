package com.acrcloud.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
//            viewHolder.title.setText(songs.get(i).title);

//        SongItemLayoutBinding binding = SongItemLayoutBinding.bind(viewHolder.itemView);
        viewHolder.binding.setSong(songs.get(i));
        viewHolder.binding.setListener(new SongItemListener() {
            @Override
            public void click(RenameMusicViewModel.Song song) {
                viewModel.search(song);
            }
        });
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
