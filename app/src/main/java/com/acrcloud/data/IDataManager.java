package com.acrcloud.data;

import io.reactivex.Observable;

public interface IDataManager {
    Observable<ACRRecognizeResponse> recognizeSong(String path);
}
