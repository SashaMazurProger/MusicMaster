package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalMetadata implements Parcelable {

    @SerializedName("deezer")
    @Expose
    private Deezer deezer;
    @SerializedName("spotify")
    @Expose
    private Spotify spotify;
    @SerializedName("youtube")
    @Expose
    private Youtube youtube;
    public final static Creator<ExternalMetadata> CREATOR = new Creator<ExternalMetadata>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ExternalMetadata createFromParcel(Parcel in) {
            return new ExternalMetadata(in);
        }

        public ExternalMetadata[] newArray(int size) {
            return (new ExternalMetadata[size]);
        }

    };

    protected ExternalMetadata(Parcel in) {
        this.deezer = ((Deezer) in.readValue((Deezer.class.getClassLoader())));
        this.spotify = ((Spotify) in.readValue((Spotify.class.getClassLoader())));
        this.youtube = ((Youtube) in.readValue((Youtube.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public ExternalMetadata() {
    }

    /**
     * @param spotify
     * @param youtube
     * @param deezer
     */
    public ExternalMetadata(Deezer deezer, Spotify spotify, Youtube youtube) {
        super();
        this.deezer = deezer;
        this.spotify = spotify;
        this.youtube = youtube;
    }

    public Deezer getDeezer() {
        return deezer;
    }

    public void setDeezer(Deezer deezer) {
        this.deezer = deezer;
    }

    public Spotify getSpotify() {
        return spotify;
    }

    public void setSpotify(Spotify spotify) {
        this.spotify = spotify;
    }

    public Youtube getYoutube() {
        return youtube;
    }

    public void setYoutube(Youtube youtube) {
        this.youtube = youtube;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(deezer);
        dest.writeValue(spotify);
        dest.writeValue(youtube);
    }

    public int describeContents() {
        return 0;
    }

}
