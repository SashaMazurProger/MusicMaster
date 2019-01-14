package com.acrcloud.ui.select

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Environment
import android.util.Log
import com.acrcloud.ui.Song
import com.acrcloud.ui.base.BaseViewModel
import com.acrcloud.ui.edit.MainNavigator
import com.acrcloud.utils.SongRenamer
import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.Executors

class SelectMusicViewModel : BaseViewModel<MainNavigator>() {

    val openSongEvent = DispatchWorkSubject.create<Song>(schedulerProvider.ui())
    val itemSongChangedEvent = DispatchWorkSubject.create<Song>(schedulerProvider.ui())

    val folderPath = ObservableField(Environment.getExternalStorageDirectory().toString())
    val songs = ObservableArrayList<Song>()
    val isEditing = ObservableBoolean(false)
    private var editingFolderDisposable: Disposable? = null

    init {

        openSongEvent.subscribe { song ->
            if (navigator != null) {
                navigator!!.onItemSongSelected(song)
            }
        }
    }

    fun loadFolder(path: String) {


        if (path == "/") {
            return
        }

        this.folderPath.set(path)
        loadFolder()
    }

    fun loadFolder() {
        songs.clear()
        if (folderPath.get() != null) {
            val folder = File(folderPath.get())
            if (folder.exists() && folder.listFiles() != null) {
                if (folder.listFiles().size != 0) {
                    for (file in folder.listFiles()) {

                        if (file.isDirectory) {
                            val song = Song(file.absolutePath, file.name)
                            song.type = Song.TYPE.FOLDER
                            songs.add(song)
                        } else if (isMusicFile(file.absolutePath)) {
                            songs.add(Song(file.absolutePath, file.name))
                        }
                    }
                }
            }
        }
    }

    fun toParentFolder() {
        val curFolder = File(folderPath.get())

        //TODO costylik
        if (curFolder.parent.endsWith("/emulated")) {
            loadFolder(curFolder.parentFile.parent)
            return
        }

        loadFolder(curFolder.parent)
    }

    //mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
    private fun isMusicFile(absolutePath: String): Boolean {
        return absolutePath.endsWith(".mp3") || absolutePath.endsWith(".mp4") || absolutePath.endsWith(".wav") || absolutePath.endsWith(".m4a") || absolutePath.endsWith(".aac") || absolutePath.endsWith(".amr") || absolutePath.endsWith(".ape") || absolutePath.endsWith(".flv") || absolutePath.endsWith(".flac") || absolutePath.endsWith(".ogg") || absolutePath.endsWith(".wma") || absolutePath.endsWith(".caf") || absolutePath.endsWith(".alac")
    }


    fun stopEditCurrentFolderNow() {
        if (isEditing.get()) {
            if (editingFolderDisposable != null) {
                editingFolderDisposable!!.dispose()
            }

            isEditing.set(false)
        }
    }

    fun editCurrentFolderNow() {

        if (isEditing.get()) {
            return
        }

        isEditing.set(true)

        val poolExecutor = Executors.newFixedThreadPool(1)
        val scheduler = Schedulers.from(poolExecutor)
        editingFolderDisposable = Observable.fromIterable(songs)
                .filter { song -> song.type == Song.TYPE.SONG }
                .flatMap({ song: Song ->
                    dataManager.recognizeSong(song.path)
                            .subscribeOn(scheduler)
                            .doOnSubscribe { d -> song.isEditing = true }
                            .doOnNext { r -> song.isEditing = false }
                            .map<Song> { result ->
                                val edited = SongRenamer.editSong(song, result)

                                if (edited != null) {

                                    updateSongInLists(edited, song)

                                    if (songs.indexOf(song) == songs.size - 1) {
                                        isEditing.set(false)
                                    }
                                    edited
                                }

                                song

                            }
                } as Function<Song, ObservableSource<Song>>)
                .doOnDispose {
                    for (item in songs) {
                        item.isEditing = false
                    }
                }
                .subscribe({ newSong -> message.onNext(newSong.title!!) },
                        { throwable -> Log.e("error", "renameAll: ", throwable) })
    }

    fun onItemSelected(song: Song) {
        if (song.type == Song.TYPE.SONG) {
            openSongEvent.onNext(song)
        } else {

            var path = song.path

            //TODO costylik
            if (path!!.endsWith("/emulated")) {
                path = "$path/0"
            }

            loadFolder(path)
        }
    }

    fun editSelectedSongNow(song: Song) {

        dataManager.recognizeSong(song.path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { d -> song.isEditing = true }
                .doOnNext { r -> song.isEditing = false }
                .subscribe({ result ->
                    val edited = SongRenamer.editSong(song, result)

                    if (edited != null) {
                        updateSongInLists(edited, song)
                    }

                }, { throwable -> Log.e("error", "rename: ", throwable) })

    }

    private fun updateSongInLists(newSong: Song, oldSong: Song) {
        var index = -1

        for (s in songs) {
            if (s.path == oldSong.path) {
                index = songs.indexOf(s)
                break
            }
        }
        if (index > -1) {
            songs[index] = newSong
        }
    }

    fun editSelectedFolderNow(song: Song) {

    }
}
