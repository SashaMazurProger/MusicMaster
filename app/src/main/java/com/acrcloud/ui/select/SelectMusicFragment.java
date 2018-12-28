package com.acrcloud.ui.select;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.acrcloud.ui.BR;
import com.acrcloud.ui.R;
import com.acrcloud.ui.base.BaseFragment;
import com.acrcloud.ui.databinding.FragmentMusicFolderBinding;
import com.acrcloud.ui.edit.MainNavigator;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectMusicFragment extends BaseFragment<FragmentMusicFolderBinding, SelectMusicViewModel> {


    public SelectMusicFragment() {
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
        SelectMusicViewModel viewModel = ViewModelProviders.of(getActivity()).get(SelectMusicViewModel.class);
        viewModel.setNavigator((MainNavigator) getActivity());
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView listView = getBinding().list;
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(new SongAdapter(getViewModel()));

        getViewModel().loadFolder();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();

        disposable(getViewModel().itemSongChangedEvent.subscribe(song -> {
            int index = getViewModel().songs.indexOf(song);
            getBinding().list.getAdapter().notifyItemChanged(index);
        }));

    }
}
