package com.acrcloud.data

import io.reactivex.Observable

interface IDataManager {
    fun recognizeSong(path: String?): Observable<ACRRecognizeResponse>
}
