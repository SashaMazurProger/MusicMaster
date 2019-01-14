package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ACRRecognizeResponse : Parcelable {

    @SerializedName("status")
    @Expose
    var status: Status? = null
    @SerializedName("metadata")
    @Expose
    var metadata: Metadata? = null
    @SerializedName("cost_time")
    @Expose
    var costTime: Double? = null
    @SerializedName("result_type")
    @Expose
    var resultType: Int? = null

    protected constructor(`in`: Parcel) {
        this.status = `in`.readValue(Status::class.java.classLoader) as Status
        this.metadata = `in`.readValue(Metadata::class.java.classLoader) as Metadata
        this.costTime = `in`.readValue(Double::class.java.classLoader) as Double
        this.resultType = `in`.readValue(Int::class.java.classLoader) as Int
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param status
     * @param resultType
     * @param costTime
     * @param metadata
     */
    constructor(status: Status, metadata: Metadata, costTime: Double?, resultType: Int?) : super() {
        this.status = status
        this.metadata = metadata
        this.costTime = costTime
        this.resultType = resultType
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(metadata)
        dest.writeValue(costTime)
        dest.writeValue(resultType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ACRRecognizeResponse> {
        override fun createFromParcel(parcel: Parcel): ACRRecognizeResponse {
            return ACRRecognizeResponse(parcel)
        }

        override fun newArray(size: Int): Array<ACRRecognizeResponse?> {
            return arrayOfNulls(size)
        }
    }


}
