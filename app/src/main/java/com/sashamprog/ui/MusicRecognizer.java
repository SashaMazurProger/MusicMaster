/**
 * @author qinxue.pan E-mail: xue@acrcloud.com
 * @version 1.0.0
 * @create 2015.10.01
 **/

/*
Copyright 2015 ACRCloud Recognizer v1.0.0

This module can recognize ACRCloud by most of audio/video file. 
        Audio: mp3, wav, m4a, flac, aac, amr, ape, ogg ...
        Video: mp4, mkv, wmv, flv, ts, avi ...
*/

package com.sashamprog.ui;

import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.acrcloud.rec.engine.ACRCloudRecognizeEngine;
import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.recognizer.ACRCloudRecognizerBothImpl;
import com.sashamprog.utils.ACRCloudExtrTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class MusicRecognizer {

    private String host = "ap-southeast-1.api.acrcloud.com";
    private String accessKey = "";
    private String accessSecret = "";
    private int timeout = 5 * 1000; // ms
    private boolean debug = false;

    private static final String TAG = "ACRCloud";
    private ACRCloudConfig mConfig;
    private ACRCloudClient mClient;

    public MusicRecognizer(Map<String, Object> config) {
        if (config.get("host") != null) {
            this.host = (String) config.get("host");
        }
        if (config.get("access_key") != null) {
            this.accessKey = (String) config.get("access_key");
        }
        if (config.get("access_secret") != null) {
            this.accessSecret = (String) config.get("access_secret");
        }
        if (config.get("timeout") != null) {
            this.timeout = 1000 * ((Integer) config.get("timeout")).intValue();
        }
        if (config.get("debug") != null) {
            this.debug = ((Boolean) config.get("debug")).booleanValue();
            if (this.debug) {
                ACRCloudExtrTool.setDebug();
            }
        }
    }

    /**
     * recognize by wav audio buffer(RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 8000 Hz)
     *
     * @param wavAudioBuffer    query audio buffer
     * @param wavAudioBufferLen the length of wavAudioBuffer
     * @return result
     **/
    public String recognize(byte[] wavAudioBuffer, int wavAudioBufferLen) {
        byte[] fp = ACRCloudExtrTool.createFingerprint(wavAudioBuffer, wavAudioBufferLen, false);
        if (fp == null) {
            return ACRCloudStatusCode.DECODE_AUDIO_ERROR;
        }
        if (fp.length <= 0) {
            return ACRCloudStatusCode.NO_RESULT;
        }
        return this.doRecogize(fp);
    }

    /**
     * recognize by buffer of (Audio/Video file)
     * Audio: mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
     *
     * @param fileBuffer    query buffer
     * @param fileBufferLen the length of fileBufferLen
     * @param startSeconds  skip (startSeconds) seconds from from the beginning of fileBuffer
     * @return result
     **/
    public String recognizeByFileBuffer(byte[] fileBuffer, int fileBufferLen, int startSeconds) {
        byte[] fp = ACRCloudExtrTool.createFingerprintByFileBuffer(fileBuffer, fileBufferLen, startSeconds, 12, false);
        if (fp == null) {
            return ACRCloudStatusCode.DECODE_AUDIO_ERROR;
        }
        if (fp.length <= 0) {
            return ACRCloudStatusCode.NO_RESULT;
        }
        return this.doRecogize(fp);
    }

    /**
     * recognize by file songPath of (Audio/Video file)
     * Audio: mp3, mp4, wav, m4a, aac, amr, ape, flv, flac, ogg, wma, caf, alac
     *
     * @param filePath     query file songPath
     * @param startSeconds skip (startSeconds) seconds from from the beginning of (filePath)
     * @return result
     **/
    public String recognizeByFile(String filePath, int startSeconds) {

//        Acr
//        byte[] fp = ACRCloudExtrTool.createFingerprintByFile(filePath, startSeconds, 12, false);
//
//        if (fp == null) {
//            return ACRCloudStatusCode.DECODE_AUDIO_ERROR;
//        }
//        if (fp.length <= 0) {
//            return ACRCloudStatusCode.NO_RESULT;
//        }
//
//        //Log.e(TAG, ""+fp.length);
//        return this.doRecogize(fp);


        File file = new File(filePath);
        byte[] buffer = new byte[3 * 1024 * 1024];
        if (!file.exists()) {
            return null;
        }
        FileInputStream fin = null;
        int bufferLen = 0;
        try {
            fin = new FileInputStream(file);
            bufferLen = fin.read(buffer, 0, buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("bufferLen=" + bufferLen);
//
//        if (bufferLen <= 0)
//            return null;

//        Map<String, Object> config = new HashMap<String, Object>();
//        config.put("access_key", "37a24216f7bdbfd272dab7035927e4cd");
//        config.put("access_secret", "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK");
//        config.put("host", "identify-eu-west-1.acrcloud.com");
//        config.put("debug", false);
//        config.put("timeout", 5);


//        this.mConfig = new ACRCloudConfig();
////        this.mConfig.acrcloudListener = this;
//
//        // If you implement IACRCloudResultWithAudioListener and override "onResult(ACRCloudResult result)", you can get the Audio data.
//        //this.mConfig.acrcloudResultWithAudioListener = this;
//
////        this.mConfig.context = this;
//        this.mConfig.host = "identify-eu-west-1.acrcloud.com";
////        this.mConfig.dbPath = path; // offline db path, you can change it with other path which this app can access.
//        this.mConfig.accessKey = "37a24216f7bdbfd272dab7035927e4cd";
//        this.mConfig.accessSecret = "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK";
//        this.mConfig.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTP; // PROTOCOL_HTTPS
//        this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
//        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
//        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;
//
////        ACRCloudRecognizerBothImpl recognizerBoth = new ACRCloudRecognizerBothImpl(mConfig, null);
//
//        Looper.prepare();
//        this.mClient = new ACRCloudClient();
//        // If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
//        // the function initWithConfig is used to load offline db, and it may cost long time.
//        this.mClient.initWithConfig(this.mConfig);
//        return mClient.recognize(buffer, bufferLen);


        String method = "POST";
        String httpURL = "/v1/identify";
        String dataType = "fingerprint";
        String sigVersion = "1";
        String timestamp = getUTCTimeSeconds();

        //String reqURL = "http://" + host + httpURL;

        String sigStr = method + "\n" + httpURL + "\n" + accessKey + "\n" + dataType + "\n" + sigVersion + "\n" + timestamp;
        String signature = encryptByHMACSHA1(sigStr.getBytes(), this.accessSecret.getBytes());

        byte[] fp = ACRCloudRecognizeEngine.genFP(buffer, bufferLen, 100);

        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("access_key", this.accessKey);
        postParams.put("sample_bytes", fp.length + "");
        postParams.put("sample", fp);
        postParams.put("timestamp", timestamp);
        postParams.put("signature", signature);
        postParams.put("data_type", dataType);
        postParams.put("signature_version", sigVersion);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://ap-southeast-1.api.acrcloud.com");
        builder.addConverterFactory(GsonConverterFactory.create());

        ACRService acrService = builder.build().create(ACRService.class);
        Call<RecognizeResponse> response = acrService.get(postParams);
        try {
            RecognizeResponse recognizeResponse = response.execute().body();
            String code = recognizeResponse.status.code;
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    interface ACRService {

        @POST("/v1/identify")
        Call<RecognizeResponse> get(@QueryMap Map<String, Object> map);
    }

    private String doRecogize(byte[] fp) {

        System.out.println("" + fp.length);

        String method = "POST";
        String httpURL = "/v1/identify";
        String dataType = "fingerprint";
        String sigVersion = "1";
        String timestamp = getUTCTimeSeconds();

        String reqURL = "http://" + host + httpURL;

        String sigStr = method + "\n" + httpURL + "\n" + accessKey + "\n" + dataType + "\n" + sigVersion + "\n" + timestamp;
        String signature = encryptByHMACSHA1(sigStr.getBytes(), this.accessSecret.getBytes());

        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("access_key", this.accessKey);
        postParams.put("sample_bytes", fp.length + "");
        postParams.put("sample", fp);
        postParams.put("timestamp", timestamp);
        postParams.put("signature", signature);
        postParams.put("data_type", dataType);
        postParams.put("signature_version", sigVersion);

        String res = postHttp(reqURL, postParams, this.timeout);

        try {
            JSONObject json_res = new JSONObject(res);
        } catch (JSONException e) {
            Log.e(TAG, "json error: " + res);
            res = ACRCloudStatusCode.JSON_ERROR;
        }

        return res;
    }

    private String encodeBase64(byte[] bstr) {
        return new String(Base64.encode(bstr, Base64.DEFAULT));
    }

    private String encryptByHMACSHA1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data);
            return encodeBase64(rawHmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getUTCTimeSeconds() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis() / 1000 + "";
    }

    private String postHttp(String posturl, Map<String, Object> params, int timeOut) {
        String res = "";
        String BOUNDARYSTR = "*****2015.10.01.acrcloud.rec.copyright." + System.currentTimeMillis() + "*****";
        String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
        String ENDBOUNDARY = "--" + BOUNDARYSTR + "--\r\n\r\n";
        String stringKeyHeader = BOUNDARY + "Content-Disposition:form-data;name=\"%s\"" + "\r\n\r\n%s\r\n";
        String filePartHeader = BOUNDARY + "Content-Disposition: form-data;name=\"%s\";filename=\"%s\"\r\n" + "Content-Type:application/octet-stream\r\n\r\n";
        URL url = null;
        HttpURLConnection conn = null;
        BufferedOutputStream out = null;
        BufferedReader reader = null;
        ByteArrayOutputStream postBufferStream = new ByteArrayOutputStream();
        try {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof String || value instanceof Integer) {
                    postBufferStream.write(String.format(stringKeyHeader, key, (String) value).getBytes());
                } else if (value instanceof byte[]) {
                    postBufferStream.write(String.format(filePartHeader, key, key).getBytes());
                    postBufferStream.write((byte[]) value);
                    postBufferStream.write("\r\n".getBytes());
                }
            }
            postBufferStream.write(ENDBOUNDARY.getBytes());

            url = new URL(posturl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            conn.connect();

            out = new BufferedOutputStream(conn.getOutputStream());

            out.write(postBufferStream.toByteArray());
            out.flush();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String tmpRes = "";
                while ((tmpRes = reader.readLine()) != null) {
                    if (tmpRes.length() > 0)
                        res = res + tmpRes;
                }
            } else {
                System.out.println("http error response code " + responseCode);
                res = ACRCloudStatusCode.HTTP_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = ACRCloudStatusCode.HTTP_ERROR;
        } finally {
            try {
                if (postBufferStream != null) {
                    postBufferStream.close();
                    postBufferStream = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}

class ACRCloudStatusCode {
    public static String HTTP_ERROR = "{\"status\":{\"msg\":\"Http Error\", \"code\":3000}}";
    public static String NO_RESULT = "{\"status\":{\"msg\":\"No Result\", \"code\":1001}}";
    public static String DECODE_AUDIO_ERROR = "{\"status\":{\"msg\":\"Can not decode audio data\", \"code\":2005}}";
    public static String RECORD_ERROR = "{\"status\":{\"msg\":\"Record Error\", \"code\":2000}}";
    public static String JSON_ERROR = "{\"status\":{\"msg\":\"json error\", \"code\":2002}}";
    public static String UNKNOW_ERROR = "{\"status\":{\"msg\":\"unknow error\", \"code\":2010}}";
}
