package com.acrcloud.utils;

import com.acrcloud.data.ACRRecognizeResponse;
import com.acrcloud.data.Music;
import com.acrcloud.ui.Song;

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

public final class SongRenamer {

    public static Song editSong(Song song, ACRRecognizeResponse recognizeResponse) {
        Music music = null;

        if (recognizeResponse != null
                && recognizeResponse.getMetadata() != null
                && recognizeResponse.getMetadata().getMusic() != null
                && !recognizeResponse.getMetadata().getMusic().isEmpty()) {

            music = recognizeResponse.getMetadata().getMusic().get(0);
        }

        if (music != null) {

            try {
                AudioFile audioFile = AudioFileIO.read(new File(song.getPath()));

                if (audioFile != null) {
                    Tag tag = audioFile.getTagOrCreateAndSetDefault();

                    tag.setField(FieldKey.ARTIST, music.getArtists().get(0).getName());
                    tag.setField(FieldKey.ALBUM, music.getAlbum().getName());
                    tag.setField(FieldKey.TITLE, music.getTitle());
                    audioFile.commit();

                    String newFileName = music.getArtists().get(0).getName() + " - " + music.getTitle();
                    File file = new File(song.getPath());
                    String newName = file.getParent() + "/" + newFileName + CommonUtils.getFileExtension(file);
                    File fileD = new File(newName);
                    file.renameTo(fileD);
                    song.setTitle(fileD.getName());
                    song.setPath(fileD.getPath());

                    return song;
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


        }

        return null;

    }
}