package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Youtube implements Parcelable {

    @SerializedName("vid")
    @Expose
    private String vid;
    public final static Creator<Youtube> CREATOR = new Creator<Youtube>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Youtube createFromParcel(Parcel in) {
            return new Youtube(in);
        }

        public Youtube[] newArray(int size) {
            return (new Youtube[size]);
        }

    };

    protected Youtube(Parcel in) {
        this.vid = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Youtube() {
    }

    /**
     * @param vid
     */
    public Youtube(String vid) {
        super();
        this.vid = vid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(vid);
    }

    public int describeContents() {
        return 0;
    }

}
