package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Metadata implements Parcelable {

    @SerializedName("music")
    @Expose
    private List<Music> music = new ArrayList<Music>();
    @SerializedName("timestamp_utc")
    @Expose
    private String timestampUtc;
    public final static Creator<Metadata> CREATOR = new Creator<Metadata>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Metadata createFromParcel(Parcel in) {
            return new Metadata(in);
        }

        public Metadata[] newArray(int size) {
            return (new Metadata[size]);
        }

    };

    protected Metadata(Parcel in) {
        in.readList(this.music, (Music.class.getClassLoader()));
        this.timestampUtc = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Metadata() {
    }

    /**
     * @param music
     * @param timestampUtc
     */
    public Metadata(List<Music> music, String timestampUtc) {
        super();
        this.music = music;
        this.timestampUtc = timestampUtc;
    }

    public List<Music> getMusic() {
        return music;
    }

    public void setMusic(List<Music> music) {
        this.music = music;
    }

    public String getTimestampUtc() {
        return timestampUtc;
    }

    public void setTimestampUtc(String timestampUtc) {
        this.timestampUtc = timestampUtc;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(music);
        dest.writeValue(timestampUtc);
    }

    public int describeContents() {
        return 0;
    }

}
