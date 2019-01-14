package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Music : Parcelable {

    @SerializedName("external_ids")
    @Expose
    var externalIds: ExternalIds? = null
    @SerializedName("play_offset_ms")
    @Expose
    var playOffsetMs: Int? = null
    @SerializedName("external_metadata")
    @Expose
    var externalMetadata: ExternalMetadata? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("genres")
    @Expose
    var genres: List<Genre> = ArrayList()
    @SerializedName("artists")
    @Expose
    var artists: List<Artist__> = ArrayList()
    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("duration_ms")
    @Expose
    var durationMs: Int? = null
    @SerializedName("album")
    @Expose
    var album: Album__? = null
    @SerializedName("acrid")
    @Expose
    var acrid: String? = null
    @SerializedName("result_from")
    @Expose
    var resultFrom: Int? = null
    @SerializedName("score")
    @Expose
    var score: Int? = null

    protected constructor(`in`: Parcel) {
        this.externalIds = `in`.readValue(ExternalIds::class.java.classLoader) as ExternalIds
        this.playOffsetMs = `in`.readValue(Int::class.java.classLoader) as Int
        this.externalMetadata = `in`.readValue(ExternalMetadata::class.java.classLoader) as ExternalMetadata
        this.releaseDate = `in`.readValue(String::class.java.classLoader) as String
        this.title = `in`.readValue(String::class.java.classLoader) as String
        `in`.readList(this.genres, Genre::class.java.classLoader)
        `in`.readList(this.artists, Artist__::class.java.classLoader)
        this.label = `in`.readValue(String::class.java.classLoader) as String
        this.durationMs = `in`.readValue(Int::class.java.classLoader) as Int
        this.album = `in`.readValue(Album__::class.java.classLoader) as Album__
        this.acrid = `in`.readValue(String::class.java.classLoader) as String
        this.resultFrom = `in`.readValue(Int::class.java.classLoader) as Int
        this.score = `in`.readValue(Int::class.java.classLoader) as Int
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param acrid
     * @param artists
     * @param externalIds
     * @param durationMs
     * @param title
     * @param resultFrom
     * @param releaseDate
     * @param externalMetadata
     * @param playOffsetMs
     * @param genres
     * @param album
     * @param score
     * @param label
     */
    constructor(externalIds: ExternalIds, playOffsetMs: Int?, externalMetadata: ExternalMetadata, releaseDate: String, title: String, genres: List<Genre>, artists: List<Artist__>, label: String, durationMs: Int?, album: Album__, acrid: String, resultFrom: Int?, score: Int?) : super() {
        this.externalIds = externalIds
        this.playOffsetMs = playOffsetMs
        this.externalMetadata = externalMetadata
        this.releaseDate = releaseDate
        this.title = title
        this.genres = genres
        this.artists = artists
        this.label = label
        this.durationMs = durationMs
        this.album = album
        this.acrid = acrid
        this.resultFrom = resultFrom
        this.score = score
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(externalIds)
        dest.writeValue(playOffsetMs)
        dest.writeValue(externalMetadata)
        dest.writeValue(releaseDate)
        dest.writeValue(title)
        dest.writeList(genres)
        dest.writeList(artists)
        dest.writeValue(label)
        dest.writeValue(durationMs)
        dest.writeValue(album)
        dest.writeValue(acrid)
        dest.writeValue(resultFrom)
        dest.writeValue(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }


}
