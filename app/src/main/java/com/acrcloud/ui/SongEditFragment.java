package com.acrcloud.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acrcloud.ui.base.BaseFragment;
import com.acrcloud.ui.databinding.FragmentSongEditBinding;
import com.acrcloud.utils.AppLogger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongEditFragment extends BaseFragment<FragmentSongEditBinding, SongEditViewModel> {


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
    public SongEditViewModel getViewModel() {
        SongEditViewModel viewModel = ViewModelProviders.of(this).get(SongEditViewModel.class);
        viewModel.getEditSong().setValue(getArguments().getParcelable(SelectMusicViewModel.Song.KEY));
        viewModel.setNavigator((SongEditNavigator) getActivity());
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().readMetadata();
        getViewModel().getMessage().observe(this, (o) -> {
            Toast.makeText(getContext(), o.toString(), Toast.LENGTH_LONG).show();
        });
    }

}
