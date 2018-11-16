package com.acrcloud.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.acrcloud.data.ACRRecognizeResponse;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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
                songs.add(new Song(file.getName(), file.getAbsolutePath()));
            }
        }
    }

    //mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
    private boolean isMusicFile(String absolutePath) {
        return absolutePath.endsWith(".mp3") || absolutePath.endsWith(".mp4") || absolutePath.endsWith(".wav") || absolutePath.endsWith(".m4a") || absolutePath.endsWith(".aac") || absolutePath.endsWith(".amr") || absolutePath.endsWith(".ape") || absolutePath.endsWith(".flv") || absolutePath.endsWith(".flac") || absolutePath.endsWith(".ogg") || absolutePath.endsWith(".wma") || absolutePath.endsWith(".caf") || absolutePath.endsWith(".alac");
    }

    public void search(Song song) {
        this.songPath.set(song.getPath());

        DataManager.recognizeSong(songPath.get()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(recognizeResponse -> {
            result.set(recognizeResponse);
            File file = new File(song.getPath());
            String title = recognizeResponse.getMetadata().getMusic().get(0).getTitle();
            File fileD = new File(folderPath.get().concat("/" + title.concat(file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()))));

            file.renameTo(fileD);
            song.setTitle(fileD.getName());
            song.setPath(fileD.getAbsolutePath());
            editSongSuccPublishSubject.onNext(song);
        }, throwable -> {
            Log.e("error", "search: ", throwable);
        });
    }


    PublishSubject<Song> editSongPublishSubject = PublishSubject.create();
    PublishSubject<Song> editSongSuccPublishSubject = PublishSubject.create();

    public void editSong(Song song) {
        editSongPublishSubject.onNext(song);
    }

    public void recFolder() {

        for (Song song : songs) {
            DataManager.recognizeSong(songPath.get()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(recognizeResponse -> {

            }, throwable -> {
                Log.e("error", "search: ", throwable);
            });
        }

    }

    public static class Song implements Parcelable {
        private String title;
        private String path;

        public Song(String title, String path) {
            this.title = title;
            this.path = path;
        }

        protected Song(Parcel in) {
            title = in.readString();
            path = in.readString();
        }

        public static final Creator<Song> CREATOR = new Creator<Song>() {
            @Override
            public Song createFromParcel(Parcel in) {
                return new Song(in);
            }

            @Override
            public Song[] newArray(int size) {
                return new Song[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(path);
        }
    }
}
