package com.acrcloud.ui.edit

import android.arch.lifecycle.MutableLiveData
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.util.Log
import com.acrcloud.data.ACRRecognizeResponse
import com.acrcloud.ui.Song
import com.acrcloud.ui.base.BaseViewModel
import com.acrcloud.utils.AppLogger
import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.CannotWriteException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldDataInvalidException
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.TagException
import java.io.File
import java.io.IOException

class SongEditViewModel : BaseViewModel<MainNavigator>() {

    val editSong = MutableLiveData<Song>()

    val result = ObservableField<ACRRecognizeResponse>()

    val isFromNetwork = ObservableBoolean(false)
    val isEditFileName = ObservableBoolean(false)
    val artist = ObservableField<String>()
    val fileName = ObservableField<String>()
    val album = ObservableField<String>()
    val title = ObservableField<String>()
    val comment = ObservableField<String>()
    val coverArt = ObservableField<Bitmap>()

    val onApplyEditEvent: DispatchWorkSubject<Any> = DispatchWorkSubject.create<Any>(schedulerProvider.ui())

    var audioFile: AudioFile? = null
        private set

    private val fileExtension: String
        get() {
            val file = File(editSong.value!!.path)
            return file.name.substring(file.name.lastIndexOf("."), file.name.length)
        }

    init {

        isFromNetwork.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (isFromNetwork.get()) {
                    readNetworkMetadata()
                } else {
                    readMetadata()
                }
            }
        })

        val fileNameCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                fileName.set(artist.get() + " - " + title.get())
            }
        }
        artist.addOnPropertyChangedCallback(fileNameCallback)
        title.addOnPropertyChangedCallback(fileNameCallback)

        onApplyEditEvent.subscribe { o ->
            if (navigator != null) {
                navigator!!.onApplyEditOpenedSong()
            }
        }

    }

    private fun readNetworkMetadata() {

        dataManager.recognizeSong(editSong.value!!.path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { d -> loading.set(true) }
                .doOnNext { r -> loading.set(false) }
                .subscribe({ recognizeResponse ->
                    result.set(recognizeResponse)
                    val music = recognizeResponse.metadata!!.music[0]
                    if (music != null) {
                        artist.set(music.artists[0].name)
                        album.set(music.album!!.name)
                        title.set(music.title)
                        //                comment.set("");
                        //                coverArt.set(music.);
                    }
                }, { throwable -> Log.e("error", "renameAll: ", throwable) })

    }

    fun saveMetadata() {

        if (audioFile != null) {
            val tag = audioFile!!.tagOrCreateAndSetDefault

            try {
                tag.setField(FieldKey.ARTIST, artist.get())
                tag.setField(FieldKey.ALBUM, album.get())
                tag.setField(FieldKey.TITLE, title.get())
                tag.setField(FieldKey.COMMENT, comment.get())
                audioFile!!.commit()

            } catch (e: FieldDataInvalidException) {
                message.onNext("Error")
                e.printStackTrace()
            } catch (e: CannotWriteException) {
                message.onNext("Error")
                e.printStackTrace()
            }

            if (isEditFileName.get()) {
                val file = File(editSong.value!!.path)
                val newName = file.parent + "/" + fileName.get() + fileExtension
                val fileD = File(newName)
                file.renameTo(fileD)
            }

            message.onNext("Apply")
            onApplyEditEvent.onNext(Any())
        }
    }

    fun readMetadata() {

        try {
            audioFile = AudioFileIO.read(File(editSong.value!!.path))
        } catch (e: CannotReadException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TagException) {
            e.printStackTrace()
        } catch (e: ReadOnlyFileException) {
            e.printStackTrace()
        } catch (e: InvalidAudioFrameException) {
            e.printStackTrace()
        }

        if (audioFile != null) {
            val tag = audioFile!!.tagOrCreateAndSetDefault

            artist.set(tag.getFirst(FieldKey.ARTIST))
            album.set(tag.getFirst(FieldKey.ALBUM))
            title.set(tag.getFirst(FieldKey.TITLE))
            comment.set(tag.getFirst(FieldKey.COMMENT))

            //            try {
            //                byte[] clipArtBytes = new byte[0];
            //                if (tag.getFirst(FieldKey.COVER_ART) != null) {
            //                    clipArtBytes = tag.getFirst(FieldKey.COVER_ART).getRawContent();
            //                    Bitmap bitmap = BitmapFactory.decodeByteArray(clipArtBytes, 0, clipArtBytes.length);
            //                    coverArt.set(bitmap);
            //                }
            //            } catch (UnsupportedEncodingException e) {
            //                e.printStackTrace();
            //            }


            val s = (tag.getFirst(FieldKey.ARTIST)
                    + tag.getFirst(FieldKey.ALBUM) + "\n"
                    + tag.getFirst(FieldKey.TITLE) + "\n"
                    + tag.getFirst(FieldKey.COMMENT) + "\n"
                    + tag.getFirst(FieldKey.YEAR) + "\n"
                    + audioFile!!.audioHeader.bitRate + "\n"
                    + audioFile!!.audioHeader.format + "\n"
                    + audioFile!!.audioHeader.trackLength + "\n")

            AppLogger.d(s)
        }
    }
}
