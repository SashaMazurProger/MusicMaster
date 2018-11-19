package com.acrcloud.ui;

import com.acrcloud.data.ACRRecognizeResponse;

import io.reactivex.Observable;

public interface IDataManager {
    Observable<ACRRecognizeResponse> recognizeSong(String path);
}
