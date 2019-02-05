package com.acrcloud.ui.select

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Environment
import android.util.Log
import com.acrcloud.ui.EditSong
import com.acrcloud.ui.base.BaseViewModel
import com.acrcloud.ui.edit.MainNavigator
import com.acrcloud.utils.SongEditor
import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.Executors

class SelectMusicViewModel : BaseViewModel<MainNavigator>() {

    val openSongEvent = DispatchWorkSubject.create<EditSong>(schedulerProvider.ui())
    val itemSongChangedEvent = DispatchWorkSubject.create<EditSong>(schedulerProvider.ui())

    val folderPath = ObservableField(Environment.getExternalStorageDirectory().path)
    val files = ObservableArrayList<EditSong>()
    val isEditingFolder = ObservableBoolean(false)
    private var editingFolderDisposable: Disposable? = null

    init {

        openSongEvent.subscribe { song ->
            if (navigator != null) {
                navigator!!.onItemSongSelected(song)
            }
        }

        loadItems()
    }

    private fun loadItems() {
        files.clear()
        if (folderPath.get() != null) {
            val folder = File(folderPath.get())
            if (folder.exists() && folder.listFiles() != null) {
                if (folder.listFiles().size != 0) {
                    for (file in folder.listFiles()) {

                        if (file.isDirectory) {
                            val song = EditSong(file.absolutePath, file.name)
                            song.type = EditSong.TYPE.FOLDER
                            files.add(song)
                        } else if (isMusicFile(file.absolutePath)) {
                            files.add(EditSong(file.absolutePath, file.name))
                        }
                    }
                    files.sortBy { it.type }
                }
            }
        }
    }

    fun toParentFolder() {
        val curFolder = File(folderPath.get())
        if (curFolder.parentFile != null && curFolder.parentFile.canRead()) {
            folderPath.set(curFolder.parent)
            checkAndLoadItems()
        }
    }

    private fun checkAndLoadItems() {
        val curFolder = File(folderPath.get())

        if (curFolder != null || curFolder.canRead()) {
            loadItems()
        }
    }

    //mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
    private fun isMusicFile(absolutePath: String): Boolean {
        return absolutePath.endsWith(".mp3") || absolutePath.endsWith(".mp4") || absolutePath.endsWith(".wav") || absolutePath.endsWith(".m4a") || absolutePath.endsWith(".aac") || absolutePath.endsWith(".amr") || absolutePath.endsWith(".ape") || absolutePath.endsWith(".flv") || absolutePath.endsWith(".flac") || absolutePath.endsWith(".ogg") || absolutePath.endsWith(".wma") || absolutePath.endsWith(".caf") || absolutePath.endsWith(".alac")
    }


    fun stopEditCurrentFolderNow() {
        if (isEditingFolder.get()) {
            if (editingFolderDisposable != null) {
                editingFolderDisposable!!.dispose()
            }

            isEditingFolder.set(false)
        }
    }

    fun editCurrentFolderNow() {

        if (isEditingFolder.get() && files.any { it.type == EditSong.TYPE.SONG }) {
            return
        }

        isEditingFolder.set(true)

        val poolExecutorSubscribe = Executors.newFixedThreadPool(1)
        val schedulerSubscribe = Schedulers.from(poolExecutorSubscribe)

        editingFolderDisposable = Observable.fromIterable(files)
                .filter { song -> song.type == EditSong.TYPE.SONG }
                .flatMap { editSong: EditSong ->
                    dataManager.recognizeSong(editSong.path)
                            .subscribeOn(schedulerSubscribe)
                            .doOnSubscribe { d -> editSong.isEditing = true }
                            .doOnNext { r -> editSong.isEditing = false }
                            .map<EditSong> { result ->
                                val edited = SongEditor.editSong(editSong, result)

                                if (edited != null) {

                                    updateSongInLists(edited, editSong)

                                    //last song
                                    if (files.indexOf(editSong) == files.size - 1) {
                                        isEditingFolder.set(false)
                                    }

                                    edited
                                }

                                editSong

                            }
                }
                .doOnDispose {
                    for (item in files) {
                        item.isEditing = false
                    }
                }
                .subscribe({ newSong -> message.onNext(newSong.title!!) },
                        { throwable -> Log.e("error", "renameAll: ", throwable) })
    }

    fun onItemSelected(editSong: EditSong) {
        if (editSong.type == EditSong.TYPE.SONG) {
            openSongEvent.onNext(editSong)
        } else {

            var folder = editSong.path
            if (folder != null) {
                folderPath.set(folder)
                checkAndLoadItems()
            }
        }
    }

    fun editSelectedSongNow(editSong: EditSong) {

        dataManager.recognizeSong(editSong.path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { d -> editSong.isEditing = true }
                .doOnNext { r -> editSong.isEditing = false }
                .subscribe({ result ->
                    val edited = SongEditor.editSong(editSong, result)

                    if (edited != null) {
                        updateSongInLists(edited, editSong)
                    }

                }, { throwable -> Log.e("error", "rename: ", throwable) })

    }

    private fun updateSongInLists(newEditSong: EditSong, oldEditSong: EditSong) {
        var index = -1

        for (s in files) {
            if (s.path == oldEditSong.path) {
                index = files.indexOf(s)
                break
            }
        }
        if (index > -1) {
            files[index] = newEditSong
        }
    }

    fun editSelectedFolderNow(editSong: EditSong) {

    }
}
