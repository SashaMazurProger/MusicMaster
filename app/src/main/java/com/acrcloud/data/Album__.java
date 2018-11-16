
package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album__ implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<Album__> CREATOR = new Creator<Album__>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Album__ createFromParcel(Parcel in) {
            return new Album__(in);
        }

        public Album__[] newArray(int size) {
            return (new Album__[size]);
        }

    }
    ;

    protected Album__(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Album__() {
    }

    /**
     * 
     * @param name
     */
    public Album__(String name) {
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
        return  0;
    }

}
