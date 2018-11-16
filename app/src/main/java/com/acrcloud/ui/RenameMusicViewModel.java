package com.acrcloud.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Environment;
import android.util.Log;

import com.acrcloud.data.ACRRecognizeResponse;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RenameMusicViewModel extends ViewModel {

    public final ObservableField<String> songPath = new ObservableField<>();
    public final ObservableField<String> folderPath = new ObservableField<>(Environment.getExternalStorageDirectory().toString() + "/Download/soundloadie");
    public final ObservableField<ACRRecognizeResponse> result = new ObservableField<>();
    public final ObservableArrayList<Song> songs = new ObservableArrayList<>();
    public final ObservableBoolean loading = new ObservableBoolean(false);

    public void loadFolder(String path) {
        this.folderPath.set(path);
        File folder = new File(folderPath.get());
        for (File file : folder.listFiles()) {
            if (isMusicFile(file.getAbsolutePath())) {
                songs.add(new Song(file.getName(), path));
            }
        }
    }

    //mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
    private boolean isMusicFile(String absolutePath) {
        return absolutePath.endsWith(".mp3")
                || absolutePath.endsWith(".mp4")
                || absolutePath.endsWith(".wav")
                || absolutePath.endsWith(".m4a")
                || absolutePath.endsWith(".aac")
                || absolutePath.endsWith(".amr")
                || absolutePath.endsWith(".ape")
                || absolutePath.endsWith(".flv")
                || absolutePath.endsWith(".flac")
                || absolutePath.endsWith(".ogg")
                || absolutePath.endsWith(".wma")
                || absolutePath.endsWith(".caf")
                || absolutePath.endsWith(".alac");
    }

    public void search(Song song) {
        this.songPath.set(song.title);

        DataManager.recognizeSong(songPath.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recognizeResponse -> {
                            result.set(recognizeResponse);
                        },
                        throwable -> {
                            Log.e("error", "search: ", throwable);
                        });
    }

    public class Song {
        private String title;
        private String path;

        public Song(String title, String path) {
            this.title = title;
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
