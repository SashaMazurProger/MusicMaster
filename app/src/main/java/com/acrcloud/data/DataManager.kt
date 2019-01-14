package com.acrcloud.data

import com.acrcloud.utils.ACRCloudRecognizer
import com.google.gson.Gson
import io.reactivex.Observable
import java.util.*

class DataManager : IDataManager {

    override fun recognizeSong(path: String?): Observable<ACRRecognizeResponse> {

        if (path == null) return Observable.empty()

        return Observable.fromCallable {

            val config = HashMap<String, Any>()
            config["access_key"] = "37a24216f7bdbfd272dab7035927e4cd"
            config["access_secret"] = "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK"
            config["host"] = "identify-eu-west-1.acrcloud.com"
            config["debug"] = false
            config["timeout"] = 5

            val re = ACRCloudRecognizer(config)
            val resultStr = re.recognizeByFile(path, 10)

            resultStr
        }.map { s ->
            val gson = Gson()
            val response = gson.fromJson(s, ACRRecognizeResponse::class.java)
            response
        }
    }

}
