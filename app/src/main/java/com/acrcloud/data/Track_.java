package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track_ implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    public final static Creator<Track_> CREATOR = new Creator<Track_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Track_ createFromParcel(Parcel in) {
            return new Track_(in);
        }

        public Track_[] newArray(int size) {
            return (new Track_[size]);
        }

    };

    protected Track_(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Track_() {
    }

    /**
     * @param id
     * @param name
     */
    public Track_(String name, String id) {
        super();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}
