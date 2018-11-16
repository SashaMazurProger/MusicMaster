package com.acrcloud.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acrcloud.ui.databinding.FragmentMusicFolderBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFolderFragment extends Fragment {

    private RenameMusicViewModel viewModel;


    public MusicFolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(RenameMusicViewModel.class);
        viewModel.editSongPublishSubject.subscribe(song -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song", song);
            ((MainActivity) getActivity()).navController.navigate(R.id.action_musicFolderFragment_to_songEditFragment, bundle);
        });


        FragmentMusicFolderBinding binding = FragmentMusicFolderBinding.bind(view);
        binding.setViewModel(viewModel);

        RecyclerView listView = binding.list;

        viewModel.editSongSuccPublishSubject.subscribe(song -> {
            listView.getAdapter().notifyDataSetChanged();
        });

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(new SongAdapter(viewModel));
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"items"})
    public static void list(RecyclerView listView, ObservableList<RenameMusicViewModel.Song> songList) {
        ((SongAdapter) listView.getAdapter()).songs = songList;
        listView.getAdapter().notifyDataSetChanged();
    }
}
