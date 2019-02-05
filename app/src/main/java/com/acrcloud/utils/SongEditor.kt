package com.acrcloud.utils

import com.acrcloud.data.ACRRecognizeResponse
import com.acrcloud.data.Music
import com.acrcloud.ui.EditSong

import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.CannotWriteException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.TagException

import java.io.File
import java.io.IOException

object SongEditor {

    fun editSong(editSong: EditSong, recognizeResponse: ACRRecognizeResponse?): EditSong? {
        var music: Music? = null

        if (recognizeResponse != null
                && recognizeResponse.metadata != null
                && recognizeResponse.metadata!!.music != null
                && !recognizeResponse.metadata!!.music.isEmpty()) {

            music = recognizeResponse.metadata!!.music[0]
        }

        if (music != null) {

            try {
                val audioFile = AudioFileIO.read(File(editSong.path))

                if (audioFile != null) {
                    val tag = audioFile.tagOrCreateAndSetDefault

                    tag.setField(FieldKey.ARTIST, music.artists[0].name)
                    tag.setField(FieldKey.ALBUM, music.album!!.name)
                    tag.setField(FieldKey.TITLE, music.title)
                    audioFile.commit()

                    val newFileName = music.artists[0].name + " - " + music.title
                    val file = File(editSong.path)
                    val newName = file.parent + "/" + newFileName + CommonUtils.getFileExtension(file)
                    val fileD = File(newName)
                    file.renameTo(fileD)
                    editSong.title = fileD.name
                    editSong.path = fileD.path

                    return editSong
                }
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
            } catch (e: CannotWriteException) {
                e.printStackTrace()
            }


        }

        return null

    }
}