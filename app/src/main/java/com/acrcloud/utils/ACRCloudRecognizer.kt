/**
 * @author qinxue.pan E-mail: xue@acrcloud.com
 * @version 1.0.0
 * @create 2015.10.01
 */

/*
Copyright 2015 ACRCloud Recognizer v1.0.0

This module can recognize ACRCloud by most of audio/video file.
        Audio: mp3, wav, m4a, flac, aac, amr, ape, ogg ...
        Video: mp4, mkv, wmv, flv, ts, avi ...
*/

package com.acrcloud.utils

import android.util.Base64
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class ACRCloudRecognizer(config: Map<String, Any>) {

    private var host = "ap-southeast-1.api.acrcloud.com"
    private var accessKey = ""
    private var accessSecret = ""
    private var timeout = 5 * 1000 // ms
    private var debug = false

    private val utcTimeSeconds: String
        get() {
            val cal = Calendar.getInstance()
            val zoneOffset = cal.get(Calendar.ZONE_OFFSET)
            val dstOffset = cal.get(Calendar.DST_OFFSET)
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
            return (cal.timeInMillis / 1000).toString() + ""
        }

    init {
        if (config["host"] != null) {
            this.host = config["host"] as String
        }
        if (config["access_key"] != null) {
            this.accessKey = config["access_key"] as String
        }
        if (config["access_secret"] != null) {
            this.accessSecret = config["access_secret"] as String
        }
        if (config["timeout"] != null) {
            this.timeout = 1000 * (config["timeout"] as Int).toInt()
        }
        if (config["debug"] != null) {
            this.debug = config["debug"] as Boolean
            if (this.debug) {
                ACRCloudExtrTool.setDebug()
            }
        }
    }

    /**
     *
     * recognize by wav audio buffer(RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 8000 Hz)
     *
     * @param wavAudioBuffer query audio buffer
     * @param wavAudioBufferLen the length of wavAudioBuffer
     *
     * @return result
     */
    fun recognize(wavAudioBuffer: ByteArray, wavAudioBufferLen: Int): String {
        val fp = ACRCloudExtrTool.createFingerprint(wavAudioBuffer, wavAudioBufferLen, false)
                ?: return ACRCloudStatusCode.DECODE_AUDIO_ERROR
        return if (fp.size <= 0) {
            ACRCloudStatusCode.NO_RESULT
        } else this.doRecogize(fp)
    }

    /**
     *
     * recognize by buffer of (Audio/Video file)
     * Audio: mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
     *
     * @param fileBuffer query buffer
     * @param fileBufferLen the length of fileBufferLen
     * @param startSeconds skip (startSeconds) seconds from from the beginning of fileBuffer
     *
     * @return result
     */
    fun recognizeByFileBuffer(fileBuffer: ByteArray, fileBufferLen: Int, startSeconds: Int): String {
        val fp = ACRCloudExtrTool.createFingerprintByFileBuffer(fileBuffer, fileBufferLen, startSeconds, 12, false)
                ?: return ACRCloudStatusCode.DECODE_AUDIO_ERROR
        return if (fp.size <= 0) {
            ACRCloudStatusCode.NO_RESULT
        } else this.doRecogize(fp)
    }

    /**
     *
     * recognize by file songPath of (Audio/Video file)
     * Audio: mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
     *
     * @param filePath query file songPath
     * @param startSeconds skip (startSeconds) seconds from from the beginning of (filePath)
     *
     * @return result
     */
    fun recognizeByFile(filePath: String, startSeconds: Int): String {
        val fp = ACRCloudExtrTool.createFingerprintByFile(filePath, startSeconds, 12, false)
                ?: return ACRCloudStatusCode.DECODE_AUDIO_ERROR

        return if (fp.size <= 0) {
            ACRCloudStatusCode.NO_RESULT
        } else this.doRecogize(fp)

        //Log.e(TAG, ""+fp.length);
    }

    private fun doRecogize(fp: ByteArray): String {

        println("" + fp.size)

        val method = "POST"
        val httpURL = "/v1/identify"
        val dataType = "fingerprint"
        val sigVersion = "1"
        val timestamp = utcTimeSeconds

        val reqURL = "http://$host$httpURL"

        val sigStr = method + "\n" + httpURL + "\n" + accessKey + "\n" + dataType + "\n" + sigVersion + "\n" + timestamp
        val signature = encryptByHMACSHA1(sigStr.toByteArray(), this.accessSecret.toByteArray())

        val postParams = HashMap<String, Any>()
        postParams["access_key"] = this.accessKey
        postParams["sample_bytes"] = fp.size.toString() + ""
        postParams["sample"] = fp
        postParams["timestamp"] = timestamp
        postParams["signature"] = signature
        postParams["data_type"] = dataType
        postParams["signature_version"] = sigVersion

        var res = postHttp(reqURL, postParams, this.timeout)

        try {
            val json_res = JSONObject(res)
        } catch (e: JSONException) {
            Log.e(TAG, "json error: $res")
            res = ACRCloudStatusCode.JSON_ERROR
        }

        return res
    }

    private fun encodeBase64(bstr: ByteArray): String {
        return String(Base64.encode(bstr, Base64.DEFAULT))
    }

    private fun encryptByHMACSHA1(data: ByteArray, key: ByteArray): String {
        try {
            val signingKey = SecretKeySpec(key, "HmacSHA1")
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(signingKey)
            val rawHmac = mac.doFinal(data)
            return encodeBase64(rawHmac)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun postHttp(posturl: String, params: Map<String, Any>, timeOut: Int): String {
        var res = ""
        val BOUNDARYSTR = "*****2015.10.01.acrcloud.rec.copyright." + System.currentTimeMillis() + "*****"
        val BOUNDARY = "--$BOUNDARYSTR\r\n"
        val ENDBOUNDARY = "--$BOUNDARYSTR--\r\n\r\n"
        val stringKeyHeader = BOUNDARY + "Content-Disposition:form-data;name=\"%s\"" + "\r\n\r\n%s\r\n"
        val filePartHeader = BOUNDARY + "Content-Disposition: form-data;name=\"%s\";filename=\"%s\"\r\n" + "Content-Type:application/octet-stream\r\n\r\n"
        var url: URL? = null
        var conn: HttpURLConnection? = null
        var out: BufferedOutputStream? = null
        var reader: BufferedReader? = null
        var postBufferStream: ByteArrayOutputStream? = ByteArrayOutputStream()
        try {
            for (key in params.keys) {
                val value = params[key]
                if (value is String || value is Int) {
                    postBufferStream!!.write(String.format(stringKeyHeader, key, value as String?).toByteArray())
                } else if (value is ByteArray) {
                    postBufferStream!!.write(String.format(filePartHeader, key, key).toByteArray())
                    postBufferStream.write(value as ByteArray?)
                    postBufferStream.write("\r\n".toByteArray())
                }
            }
            postBufferStream!!.write(ENDBOUNDARY.toByteArray())

            url = URL(posturl)
            conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = timeOut
            conn.readTimeout = timeOut
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.doInput = true
            conn.setRequestProperty("Accept-Charset", "utf-8")
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=$BOUNDARYSTR")
            conn.connect()

            out = BufferedOutputStream(conn.outputStream)

            out.write(postBufferStream.toByteArray())
            out.flush()
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                var tmpRes = ""
                while (tmpRes != null) {
                    if (tmpRes.length > 0)
                        res = res + tmpRes

                    tmpRes = reader.readLine()
                }
            } else {
                println("http error response code $responseCode")
                res = ACRCloudStatusCode.HTTP_ERROR
            }
        } catch (e: Exception) {
            e.printStackTrace()
            res = ACRCloudStatusCode.HTTP_ERROR
        } finally {
            try {
                if (postBufferStream != null) {
                    postBufferStream.close()
                    postBufferStream = null
                }
                if (out != null) {
                    out.close()
                    out = null
                }
                if (reader != null) {
                    reader.close()
                    reader = null
                }
                if (conn != null) {
                    conn.disconnect()
                    conn = null
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return res
    }

    companion object {

        private val TAG = "ACRCloud"
    }
}

internal object ACRCloudStatusCode {
    var HTTP_ERROR = "{\"status\":{\"msg\":\"Http Error\", \"code\":3000}}"
    var NO_RESULT = "{\"status\":{\"msg\":\"No Result\", \"code\":1001}}"
    var DECODE_AUDIO_ERROR = "{\"status\":{\"msg\":\"Can not decode audio data\", \"code\":2005}}"
    var RECORD_ERROR = "{\"status\":{\"msg\":\"Record Error\", \"code\":2000}}"
    var JSON_ERROR = "{\"status\":{\"msg\":\"json error\", \"code\":2002}}"
    var UNKNOW_ERROR = "{\"status\":{\"msg\":\"unknow error\", \"code\":2010}}"
}
