package com.acrcloud.data;

import com.acrcloud.utils.ACRCloudRecognizer;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class DataManager implements IDataManager {

    @Override
    public Observable<ACRRecognizeResponse> recognizeSong(String path) {

        return Observable.fromCallable(() -> {

            Map<String, Object> config = new HashMap<String, Object>();
            config.put("access_key", "37a24216f7bdbfd272dab7035927e4cd");
            config.put("access_secret", "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK");
            config.put("host", "identify-eu-west-1.acrcloud.com");
            config.put("debug", false);
            config.put("timeout", 5);

            ACRCloudRecognizer re = new ACRCloudRecognizer(config);
            String resultStr = re.recognizeByFile(path, 10);

            return resultStr;
        }).map(s -> {
            Gson gson = new Gson();
            ACRRecognizeResponse response = gson.fromJson(s, ACRRecognizeResponse.class);
            return response;
        });
    }

}
