package com.acrcloud.ui;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.acrcloud.data.ACRRecognizeResponse;
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

public class SongEditViewModel extends BaseViewModel {

    public final MutableLiveData<SelectMusicViewModel.Song> editSong = new MutableLiveData<>();

    public final ObservableField<ACRRecognizeResponse> result = new ObservableField<>();

    public final ObservableField<String> artist = new ObservableField<>();
    public final ObservableField<String> album = new ObservableField<>();
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> comment = new ObservableField<>();
    public final ObservableField<TagField> coverArt = new ObservableField<>();
    private AudioFile audioFile;


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
}
