package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist__ implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<Artist__> CREATOR = new Creator<Artist__>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Artist__ createFromParcel(Parcel in) {
            return new Artist__(in);
        }

        public Artist__[] newArray(int size) {
            return (new Artist__[size]);
        }

    };

    protected Artist__(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Artist__() {
    }

    /**
     * @param name
     */
    public Artist__(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
