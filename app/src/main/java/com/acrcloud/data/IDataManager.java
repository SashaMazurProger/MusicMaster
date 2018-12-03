package com.acrcloud.data;

import com.acrcloud.data.ACRRecognizeResponse;

import io.reactivex.Observable;

public interface IDataManager {
    Observable<ACRRecognizeResponse> recognizeSong(String path);
}
