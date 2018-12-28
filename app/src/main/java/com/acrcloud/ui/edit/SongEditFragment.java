package com.acrcloud.ui.edit;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.acrcloud.ui.BR;
import com.acrcloud.ui.R;
import com.acrcloud.ui.Song;
import com.acrcloud.ui.base.BaseFragment;
import com.acrcloud.ui.databinding.FragmentSongEditBinding;


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
        viewModel.setNavigator((MainNavigator) getActivity());
        viewModel.getEditSong().setValue(getArguments().getParcelable(Song.KEY));
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().readMetadata();

    }

//    @Override
//    protected void bindEvents() {
//        super.bindEvents();
//
//        getViewModel().getCoverArt().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                ((ImageView) getBinding().imageView).setImageBitmap(getViewModel().getCoverArt().get());
//            }
//        });
//    }
}
