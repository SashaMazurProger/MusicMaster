package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExternalMetadata : Parcelable {

    @SerializedName("deezer")
    @Expose
    var deezer: Deezer? = null
    @SerializedName("spotify")
    @Expose
    var spotify: Spotify? = null
    @SerializedName("youtube")
    @Expose
    var youtube: Youtube? = null

    protected constructor(`in`: Parcel) {
        this.deezer = `in`.readValue(Deezer::class.java.classLoader) as Deezer
        this.spotify = `in`.readValue(Spotify::class.java.classLoader) as Spotify
        this.youtube = `in`.readValue(Youtube::class.java.classLoader) as Youtube
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param spotify
     * @param youtube
     * @param deezer
     */
    constructor(deezer: Deezer, spotify: Spotify, youtube: Youtube) : super() {
        this.deezer = deezer
        this.spotify = spotify
        this.youtube = youtube
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(deezer)
        dest.writeValue(spotify)
        dest.writeValue(youtube)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExternalMetadata> {
        override fun createFromParcel(parcel: Parcel): ExternalMetadata {
            return ExternalMetadata(parcel)
        }

        override fun newArray(size: Int): Array<ExternalMetadata?> {
            return arrayOfNulls(size)
        }
    }


}
