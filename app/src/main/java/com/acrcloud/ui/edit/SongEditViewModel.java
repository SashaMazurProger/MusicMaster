package com.acrcloud.ui.edit;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.util.Log;

import com.acrcloud.data.ACRRecognizeResponse;
import com.acrcloud.data.Music;
import com.acrcloud.ui.select.SelectMusicViewModel;
import com.acrcloud.ui.base.BaseViewModel;
import com.acrcloud.utils.AppLogger;
import com.acrcloud.utils.rx.SchedulerProvider;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

import java.io.File;
import java.io.IOException;

import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SongEditViewModel extends BaseViewModel {

    private final MutableLiveData<SelectMusicViewModel.Song> editSong = new MutableLiveData<>();

    private final ObservableField<ACRRecognizeResponse> result = new ObservableField<>();

    private final ObservableBoolean isFromNetwork = new ObservableBoolean(false);
    private final ObservableBoolean isEditFileName = new ObservableBoolean(false);
    private final ObservableField<String> artist = new ObservableField<>();
    private final ObservableField<String> fileName = new ObservableField<>();
    private final ObservableField<String> album = new ObservableField<>();
    private final ObservableField<String> title = new ObservableField<>();
    private final ObservableField<String> comment = new ObservableField<>();
    private final ObservableField<TagField> coverArt = new ObservableField<>();

    private final DispatchWorkSubject onApplyEditEvent = DispatchWorkSubject.create(getSchedulerProvider().ui());

    private AudioFile audioFile;

    public SongEditViewModel() {

        isFromNetwork.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (isFromNetwork.get()) {
                    readNetworkMetadata();
                } else {
                    readMetadata();
                }
            }
        });

        Observable.OnPropertyChangedCallback fileNameCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                fileName.set(artist.get() + " - " + title.get());
            }
        };
        artist.addOnPropertyChangedCallback(fileNameCallback);
        title.addOnPropertyChangedCallback(fileNameCallback);

    }

    private String getFileExtension() {
        File file = new File(editSong.getValue().getPath());
        String ext = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
        return ext;
    }

    private void readNetworkMetadata() {

        getDataManager().recognizeSong(editSong.getValue().getPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> getLoading().set(true))
                .doOnNext(r -> getLoading().set(false))
                .subscribe(recognizeResponse -> {
                    result.set(recognizeResponse);
                    Music music = recognizeResponse.getMetadata().getMusic().get(0);
                    if (music != null) {
                        artist.set(music.getArtists().get(0).getName());
                        album.set(music.getAlbum().getName());
                        title.set(music.getTitle());
//                comment.set("");
//                coverArt.set(music.);
                    }
                }, throwable -> {
                    Log.e("error", "renameAll: ", throwable);
                });

    }

    public void saveMetadata() {

        if (audioFile != null) {
            Tag tag = audioFile.getTagOrCreateAndSetDefault();

            try {
                tag.setField(FieldKey.ARTIST, artist.get());
                tag.setField(FieldKey.ALBUM, album.get());
                tag.setField(FieldKey.TITLE, title.get());
                tag.setField(FieldKey.COMMENT, comment.get());
                audioFile.commit();

            } catch (FieldDataInvalidException e) {
                getMessage().onNext("Error");
                e.printStackTrace();
            } catch (CannotWriteException e) {
                getMessage().onNext("Error");
                e.printStackTrace();
            }

            if (isEditFileName.get()) {
                File file = new File(editSong.getValue().getPath());
                String newName = file.getParent() + "/" + fileName.get() + getFileExtension();
                File fileD = new File(newName);
                file.renameTo(fileD);
            }

            getMessage().onNext("Apply");
            onApplyEditEvent.onNext(new Object());
        }
    }

    public void readMetadata() {

        try {
            audioFile = AudioFileIO.read(new File(editSong.getValue().getPath()));
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
        }

        if (audioFile != null) {
            Tag tag = audioFile.getTagOrCreateAndSetDefault();

            artist.set(tag.getFirst(FieldKey.ARTIST));
            album.set(tag.getFirst(FieldKey.ALBUM));
            title.set(tag.getFirst(FieldKey.TITLE));
            comment.set(tag.getFirst(FieldKey.COMMENT));
            coverArt.set(tag.getFirstField(FieldKey.COVER_ART));

            String s = tag.getFirst(FieldKey.ARTIST)
                    + tag.getFirst(FieldKey.ALBUM) + "\n"
                    + tag.getFirst(FieldKey.TITLE) + "\n"
                    + tag.getFirst(FieldKey.COMMENT) + "\n"
                    + tag.getFirst(FieldKey.YEAR) + "\n"
                    + audioFile.getAudioHeader().getBitRate() + "\n"
                    + audioFile.getAudioHeader().getFormat() + "\n"
                    + audioFile.getAudioHeader().getTrackLength() + "\n";

            AppLogger.d(s);
        }
    }

    public MutableLiveData<SelectMusicViewModel.Song> getEditSong() {
        return editSong;
    }

    public ObservableField<ACRRecognizeResponse> getResult() {
        return result;
    }

    public ObservableField<String> getArtist() {
        return artist;
    }

    public ObservableField<String> getAlbum() {
        return album;
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public ObservableField<String> getComment() {
        return comment;
    }

    public ObservableField<TagField> getCoverArt() {
        return coverArt;
    }

    public AudioFile getAudioFile() {
        return audioFile;
    }

    public ObservableBoolean getIsFromNetwork() {
        return isFromNetwork;
    }

    public ObservableField<String> getFileName() {
        return fileName;
    }

    public ObservableBoolean getIsEditFileName() {
        return isEditFileName;
    }

    public DispatchWorkSubject getOnApplyEditEvent() {
        return onApplyEditEvent;
    }
}
