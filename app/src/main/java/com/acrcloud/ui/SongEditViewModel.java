package com.acrcloud.ui;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.acrcloud.data.ACRRecognizeResponse;
import com.acrcloud.data.Music;
import com.acrcloud.ui.base.BaseViewModel;
import com.acrcloud.utils.AppLogger;

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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SongEditViewModel extends BaseViewModel {

    private final MutableLiveData<SelectMusicViewModel.Song> editSong = new MutableLiveData<>();

    private final ObservableField<ACRRecognizeResponse> result = new ObservableField<>();

    private final ObservableBoolean isFromNetwork = new ObservableBoolean();
    private final ObservableField<String> artist = new ObservableField<>();
    private final ObservableField<String> album = new ObservableField<>();
    private final ObservableField<String> title = new ObservableField<>();
    private final ObservableField<String> comment = new ObservableField<>();
    private final ObservableField<TagField> coverArt = new ObservableField<>();
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
    }

    private void readNetworkMetadata() {

        getDataManager().recognizeSong(editSong.getValue().getPath()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(recognizeResponse -> {
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
            Log.e("error", "search: ", throwable);
        });

    }

    public void saveMetadata() {
        if (audioFile != null) {
            Tag tag = audioFile.getTag();

            try {
                tag.setField(FieldKey.ARTIST, artist.get());
                tag.setField(FieldKey.ALBUM, album.get());
                tag.setField(FieldKey.TITLE, title.get());
                tag.setField(FieldKey.COMMENT, comment.get());
                audioFile.commit();

            } catch (FieldDataInvalidException e) {
                e.printStackTrace();
            } catch (CannotWriteException e) {
                e.printStackTrace();
            }
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
            Tag tag = audioFile.getTag();

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
}