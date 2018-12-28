package com.acrcloud.ui.select;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Environment;
import android.util.Log;

import com.acrcloud.ui.Song;
import com.acrcloud.ui.base.BaseViewModel;
import com.acrcloud.ui.edit.MainNavigator;
import com.acrcloud.utils.SongRenamer;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SelectMusicViewModel extends BaseViewModel<MainNavigator> {

    public final DispatchWorkSubject<Song> openSongEvent = DispatchWorkSubject.create(getSchedulerProvider().ui());
    public final DispatchWorkSubject<Song> itemSongChangedEvent = DispatchWorkSubject.create(getSchedulerProvider().ui());

    public final ObservableField<String> folderPath = new ObservableField<>(Environment.getExternalStorageDirectory().toString());
    public final ObservableArrayList<Song> songs = new ObservableArrayList<>();
    private ObservableBoolean isEditing = new ObservableBoolean(false);
    private Disposable editingFolderDisposable;


    public SelectMusicViewModel() {

        openSongEvent.subscribe(song -> {
            if (getNavigator() != null) {
                getNavigator().onItemSongSelected(song);
            }
        });
    }

    public void loadFolder(String path) {


        if (path.equals("/")) {
            return;
        }

        this.folderPath.set(path);
        loadFolder();
    }

    public void loadFolder() {
        songs.clear();
        if (folderPath.get() != null) {
            File folder = new File(folderPath.get());
            if (folder.exists() && folder.listFiles() != null) {
                if (folder.listFiles().length != 0) {
                    for (File file : folder.listFiles()) {

                        if (file.isDirectory()) {
                            Song song = new Song(file.getAbsolutePath(), file.getName());
                            song.setType(Song.TYPE.FOLDER);
                            songs.add(song);
                        } else if (isMusicFile(file.getAbsolutePath())) {
                            songs.add(new Song(file.getAbsolutePath(), file.getName()));
                        }
                    }
                }
            }
        }
    }

    public void toParentFolder() {
        File curFolder = new File(folderPath.get());

        //TODO costylik
        if (curFolder.getParent().endsWith("/emulated")) {
            loadFolder(curFolder.getParentFile().getParent());
            return;
        }

        loadFolder(curFolder.getParent());
    }

    //mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
    private boolean isMusicFile(String absolutePath) {
        return absolutePath.endsWith(".mp3") || absolutePath.endsWith(".mp4") || absolutePath.endsWith(".wav") || absolutePath.endsWith(".m4a") || absolutePath.endsWith(".aac") || absolutePath.endsWith(".amr") || absolutePath.endsWith(".ape") || absolutePath.endsWith(".flv") || absolutePath.endsWith(".flac") || absolutePath.endsWith(".ogg") || absolutePath.endsWith(".wma") || absolutePath.endsWith(".caf") || absolutePath.endsWith(".alac");
    }


    public void stopEditCurrentFolderNow() {
        if (isEditing.get()) {
            if (editingFolderDisposable != null) {
                editingFolderDisposable.dispose();
            }

            isEditing.set(false);
        }
    }

    public void editCurrentFolderNow() {

        if (isEditing.get()) {
            return;
        }

        isEditing.set(true);

        ExecutorService poolExecutor = Executors.newFixedThreadPool(1);
        Scheduler scheduler = Schedulers.from(poolExecutor);
        editingFolderDisposable = Observable.fromIterable(songs)
                .filter(song -> song.getType() == Song.TYPE.SONG)
                .flatMap((Function<Song, ObservableSource<Song>>) song ->
                        getDataManager().recognizeSong(song.getPath())
                                .subscribeOn(scheduler)
                                .doOnSubscribe((d) -> song.setIsEditing(true))
                                .doOnNext(r -> song.setIsEditing(false))
                                .map(result -> {
                                    Song edited = SongRenamer.editSong(song, result);

                                    if (edited != null) {

                                        updateSongInLists(edited, song);

                                        if (songs.indexOf(song) == songs.size() - 1) {
                                            isEditing.set(false);
                                        }

                                        return edited;
                                    }

                                    return song;

                                }))
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        for (Song item : songs) {
                            item.setIsEditing(false);
                        }
                    }
                })
                .subscribe(newSong -> {
                            getMessage().onNext(newSong.getTitle());
                        },
                        throwable -> {
                            Log.e("error", "renameAll: ", throwable);
                        });
    }

    public void onItemSelected(Song song) {
        if (song.getType() == Song.TYPE.SONG) {
            openSongEvent.onNext(song);
        } else {

            String path = song.getPath();

            //TODO costylik
            if (path.endsWith("/emulated")) {
                path = path.concat("/0");
            }

            loadFolder(path);
        }
    }

    public void editSelectedSongNow(Song song) {

        getDataManager().recognizeSong(song.getPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> song.setIsEditing(true))
                .doOnNext(r -> song.setIsEditing(false))
                .subscribe(result -> {
                    Song edited = SongRenamer.editSong(song, result);

                    if (edited != null) {
                        updateSongInLists(edited, song);
                    }

                }, throwable -> {
                    Log.e("error", "rename: ", throwable);
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

    public void editSelectedFolderNow(Song song) {

    }

    public ObservableBoolean getIsEditing() {
        return isEditing;
    }
}
