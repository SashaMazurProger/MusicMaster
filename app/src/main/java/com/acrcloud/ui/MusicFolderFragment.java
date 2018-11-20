package com.acrcloud.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.acrcloud.ui.base.BaseFragment;
import com.acrcloud.ui.databinding.FragmentMusicFolderBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFolderFragment extends BaseFragment<FragmentMusicFolderBinding, SelectMusicViewModel> {


    public MusicFolderFragment() {
        // Required empty public constructor
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_music_folder;
    }

    @Override
    public SelectMusicViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(SelectMusicViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView listView = getViewDataBinding().list;
        getViewModel().onEditSongSuccess.subscribe(song -> {
            listView.getAdapter().notifyDataSetChanged();
        });
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(new SongAdapter(getViewModel()));

        super.onViewCreated(view, savedInstanceState);
    }
}
