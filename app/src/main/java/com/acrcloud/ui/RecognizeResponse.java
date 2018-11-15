package com.acrcloud.ui;

import com.google.gson.annotations.SerializedName;

class RecognizeResponse {

    @SerializedName("status")
    public Status status;
}
class Status{
    @SerializedName("code")
    String code;
}
