/**
 * @author qinxue.pan E-mail: xue@acrcloud.com
 * @version 1.0.0
 * @create 2015.10.01
 */

package com.acrcloud.utils

/*

ACRCloudExtrTool Copyright 2015 ACRCloud v1.0.0

     This module can create "ACRCloud Fingerprint" from most of audio/video file.(
       *          Audio: mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac).

 Functions:
     createFingerprintByFile(file_name, start_time_seconds, audio_len_seconds, is_db_fingerprint);
         file_name: Path of input file;
         start_time_seconds: Start time of input file, default is 0;
         audio_len_seconds: Length of audio data you need. if you create recogize frigerprint, default is 12 seconds, if you create db frigerprint, it is not usefully;
         is_db_fingerprint: If it is True, it will create db frigerprint;

     createFingerprintByFileBuffer(data_buffer, data_buffer_len, start_time_seconds, audio_len_seconds, is_db_fingerprint);
         data_buffer: data buffer of input file;
         start_time_seconds: Start time of input file, default is 0;
         audio_len_seconds: Length of audio data you need. if you create recogize frigerprint, default is 12 seconds, if you create db frigerprint, it is not usefully;
         is_db_fingerprint: If it is True, it will create db frigerprint;

     decodeAudioByFile(file_name, start_time_seconds, audio_len_seconds);
         It will return the audio data(RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 8000 Hz);
             file_name: Path of input file;
             start_time_seconds: Start time of input file, default is 0;
             audio_len_seconds: Length of audio data you need, if it is 0, will decode all the audio;

     decodeAudioByFileBuffer(data_buffer, data_buffer_len, start_time_seconds, audio_len_seconds);
         It will return the audio data(RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 8000 Hz);
             data_buffer: data buffer of input file;
             start_time_seconds: Start time of input file, default is 0;
             audio_len_seconds: Length of audio data you need, if it is 0, will decode all the audio;

     get_doc()
         return the doc of this module

     set_debug()
         set debug, and print all info.

*/
class ACRCloudExtrTool {
    companion object {

        init {
            try {
                System.loadLibrary("ACRCloudExtrTool")
                native_init()
            } catch (e: Exception) {
                System.err.println("ACRCloudExtrTool loadLibrary error!")
            }

        }

        fun createFingerprintByFile(fileName: String?, startTimeSeconds: Int, audioLenSeconds: Int, isDB: Boolean): ByteArray? {
            return if (fileName == null || "" == fileName) {
                null
            } else native_create_fingerprint_by_file(fileName, startTimeSeconds, audioLenSeconds, isDB)
        }

        fun createFingerprintByFileBuffer(dataBuffer: ByteArray?, dataBufferLen: Int, startTimeSeconds: Int, audioLenSeconds: Int, isDB: Boolean): ByteArray? {
            return if (dataBuffer == null || dataBufferLen <= 0) {
                null
            } else native_create_fingerprint_by_filebuffer(dataBuffer, dataBufferLen, startTimeSeconds, audioLenSeconds, isDB)
        }

        fun createFingerprint(dataBuffer: ByteArray?, dataBufferLen: Int, isDB: Boolean): ByteArray? {
            return if (dataBuffer == null || dataBufferLen <= 0) {
                null
            } else native_create_fingerprint(dataBuffer, dataBufferLen, isDB)
        }

        fun decodeAudioByFile(fileName: String?, startTimeSeconds: Int, audioLenSeconds: Int): ByteArray? {
            return if (fileName == null || "" == fileName) {
                null
            } else native_decode_audio_by_file(fileName, startTimeSeconds, audioLenSeconds)
        }

        fun decodeAudioByFileBuffer(dataBuffer: ByteArray?, dataBufferLen: Int, startTimeSeconds: Int, audioLenSeconds: Int): ByteArray? {
            return if (dataBuffer == null || dataBufferLen <= 0) {
                null
            } else native_decode_audio_by_filebuffer(dataBuffer, dataBufferLen, startTimeSeconds, audioLenSeconds)
        }

        fun setDebug() {
            native_set_debug()
        }

        val doc: String
            get() = native_get_doc()

        private external fun native_init()

        private external fun native_create_fingerprint_by_file(file_name: String, start_time_seconds: Int, audio_len_seconds: Int, is_db_fingerprint: Boolean): ByteArray

        private external fun native_create_fingerprint_by_filebuffer(data_buffer: ByteArray, data_buffer_len: Int, start_time_seconds: Int, audio_len_seconds: Int, is_db_fingerprint: Boolean): ByteArray

        private external fun native_create_fingerprint(wav_data_buffer: ByteArray, wav_data_buffer_len: Int, is_db_fingerprint: Boolean): ByteArray

        private external fun native_decode_audio_by_file(file_name: String, start_time_seconds: Int, audio_len_seconds: Int): ByteArray

        private external fun native_decode_audio_by_filebuffer(data_buffer: ByteArray, data_buffer_len: Int, start_time_seconds: Int, audio_len_seconds: Int): ByteArray

        private external fun native_set_debug()

        private external fun native_get_doc(): String
    }
}
