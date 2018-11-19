package com.acrcloud.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.acrcloud.ui.base.BaseFragment;
import com.acrcloud.ui.databinding.FragmentSongEditBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongEditFragment extends BaseFragment<FragmentSongEditBinding, RenameMusicViewModel> {


    public SongEditFragment() {
        // Required empty public constructor
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_song_edit;
    }

    @Override
    public RenameMusicViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RenameMusicViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Toast.makeText(getContext(), getArguments().getParcelable("song").toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), getViewModel().editSong.getValue().getTitle(), Toast.LENGTH_LONG).show();

    }
}
