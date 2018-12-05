package com.acrcloud.ui.select;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.acrcloud.data.ACRRecognizeResponse;
import com.acrcloud.data.Music;
import com.acrcloud.ui.BR;
import com.acrcloud.ui.base.BaseViewModel;
import com.acrcloud.utils.CommonUtils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SelectMusicViewModel extends BaseViewModel {

    public final DispatchWorkSubject<Song> openSongEvent = DispatchWorkSubject.create(getSchedulerProvider().ui());
    public final DispatchWorkSubject<Song> itemSongChangedEvent = DispatchWorkSubject.create(getSchedulerProvider().ui());


    public final ObservableField<String> folderPath = new ObservableField<>(Environment.getExternalStorageDirectory().toString() + "/Download/soundloadie/");
    public final ObservableArrayList<Song> songs = new ObservableArrayList<>();
    //private List<SongAdapter.ObservableSong> observableSongs = new ArrayList<>();

    public SelectMusicViewModel() {

//        songs.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Song>>() {
//            @Override
//            public void onChanged(ObservableList<Song> sender) {
//                observableSongs.clear();
//                for (SelectMusicViewModel.Song song : songs) {
//                    observableSongs.add(new SongAdapter.ObservableSong(song));
//                }
//            }
//
//            @Override
//            public void onItemRangeChanged(ObservableList<Song> sender, int positionStart, int itemCount) {
//
//            }
//
//            @Override
//            public void onItemRangeInserted(ObservableList<Song> sender, int positionStart, int itemCount) {
//
//            }
//
//            @Override
//            public void onItemRangeMoved(ObservableList<Song> sender, int fromPosition, int toPosition, int itemCount) {
//
//            }
//
//            @Override
//            public void onItemRangeRemoved(ObservableList<Song> sender, int positionStart, int itemCount) {
//
//            }
//        });
    }

    public void loadFolder(String path) {
        this.folderPath.set(path);
        songs.clear();
        File folder = new File(folderPath.get());
        if (folder.listFiles().length != 0) {
            for (File file : folder.listFiles()) {
                if (isMusicFile(file.getAbsolutePath())) {
                    songs.add(new Song(file.getName(), file.getAbsolutePath()));
                }
            }
        }
    }

    public void loadFolder() {
        songs.clear();
        if (folderPath.get() != null) {
            File folder = new File(folderPath.get());

            if (folder.exists()) {
                if (folder.listFiles().length != 0) {
                    for (File file : folder.listFiles()) {
                        if (isMusicFile(file.getAbsolutePath())) {
                            songs.add(new Song(file.getName(), file.getAbsolutePath()));
                        }
                    }
                }
            } else {
                toParentFolder();
            }
        }
    }

    public void toParentFolder() {
        File curFolder = new File(folderPath.get());

        folderPath.set(curFolder.getParent());
        loadFolder();
    }

    //mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
    private boolean isMusicFile(String absolutePath) {
        return absolutePath.endsWith(".mp3") || absolutePath.endsWith(".mp4") || absolutePath.endsWith(".wav") || absolutePath.endsWith(".m4a") || absolutePath.endsWith(".aac") || absolutePath.endsWith(".amr") || absolutePath.endsWith(".ape") || absolutePath.endsWith(".flv") || absolutePath.endsWith(".flac") || absolutePath.endsWith(".ogg") || absolutePath.endsWith(".wma") || absolutePath.endsWith(".caf") || absolutePath.endsWith(".alac");
    }


    public void editFolderNow() {
        ExecutorService poolExecutor = Executors.newFixedThreadPool(1);
        Scheduler scheduler = Schedulers.from(poolExecutor);
        Observable.fromIterable(songs)
                .flatMap(new Function<Song, ObservableSource<Song>>() {

                    @Override
                    public ObservableSource<Song> apply(Song newSong) throws Exception {
                        return getDataManager().recognizeSong(newSong.getPath())
                                .subscribeOn(scheduler)
//                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe((d) -> newSong.setIsEditing(true))
                                .doOnNext(r -> newSong.setIsEditing(false))
                                .map(new ResultConsumer(newSong));
                    }
                })
                .subscribe(newSong -> {
                            getMessage().onNext(newSong.title);
                        },
                        throwable -> {
                            Log.e("error", "renameAll: ", throwable);
                        });
    }

    public void editSong(Song song) {
        openSongEvent.onNext(song);
    }

    public void editSongNow(Song newSong) {

        getDataManager().recognizeSong(newSong.getPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> newSong.setIsEditing(true))
                .doOnNext(r -> newSong.setIsEditing(false))
                .subscribe(new ResultConsumer(newSong), throwable -> {
                    Log.e("error", "renameAll: ", throwable);
                });

    }

    private void updateSongInLists(Song newSong, Song oldSong) {
        int index = -1;

        for (Song s : songs) {
            if (s.getPath().equals(oldSong.getPath())) {
                index = songs.indexOf(s);
                break;
            }
        }
        if (index > -1) {
            songs.set(index, newSong);
        }
    }

    public static class Song extends BaseObservable implements Parcelable {
        public static final String KEY = "song";

        private String title;
        private String path;
        private boolean editing = false;

        public Song(String title, String path) {
            this.title = title;
            this.path = path;
        }

        public Song(String title, String path, boolean editing) {
            this.title = title;
            this.path = path;
            setIsEditing(editing);
        }

        protected Song(Parcel in) {
            title = in.readString();
            path = in.readString();
        }


        @Bindable
        public boolean getIsEditing() {
            return editing;
        }

        public void setIsEditing(boolean isEditing) {
            editing = isEditing;
            notifyPropertyChanged(BR.isEditing);
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

    private class ResultConsumer implements Consumer<ACRRecognizeResponse>, Function<ACRRecognizeResponse, Song> {

        private Song newSong;

        private ResultConsumer(Song newSong) {
            this.newSong = newSong;
        }

        @Override
        public void accept(ACRRecognizeResponse recognizeResponse) throws Exception {
            changeSong(recognizeResponse);
        }

        private void changeSong(ACRRecognizeResponse recognizeResponse) {
            Music music = null;

            if (recognizeResponse != null
                    && recognizeResponse.getMetadata() != null
                    && recognizeResponse.getMetadata().getMusic() != null
                    && !recognizeResponse.getMetadata().getMusic().isEmpty()) {

                music = recognizeResponse.getMetadata().getMusic().get(0);
            }

            if (music != null) {

                try {
                    AudioFile audioFile = AudioFileIO.read(new File(newSong.getPath()));

                    if (audioFile != null) {
                        Tag tag = audioFile.getTagOrCreateAndSetDefault();

                        tag.setField(FieldKey.ARTIST, music.getArtists().get(0).getName());
                        tag.setField(FieldKey.ALBUM, music.getAlbum().getName());
                        tag.setField(FieldKey.TITLE, music.getTitle());
                        audioFile.commit();

                        String newFileName = music.getArtists().get(0).getName() + " - " + music.getTitle();
                        File file = new File(newSong.getPath());
                        String newName = file.getParent() + "/" + newFileName + CommonUtils.getFileExtension(file);
                        File fileD = new File(newName);
                        file.renameTo(fileD);
                        newSong.setTitle(fileD.getName());
                        newSong.setPath(fileD.getPath());

                        getMessage().onNext("Apply");
                        songs.set(songs.indexOf(newSong), newSong);
                        //updateSongInLists(newSong, oldSong);
                        //itemSongChangedEvent.onNext(song);
                        return;
                    }
                } catch (CannotReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TagException e) {
                    e.printStackTrace();
                } catch (ReadOnlyFileException e) {
                    e.printStackTrace();
                } catch (InvalidAudioFrameException e) {
                    e.printStackTrace();
                } catch (CannotWriteException e) {
                    e.printStackTrace();
                }

                getMessage().onNext("Error");
            }
        }

        @Override
        public Song apply(ACRRecognizeResponse acrRecognizeResponse) throws Exception {
            changeSong(acrRecognizeResponse);
            return newSong;
        }
    }
}
