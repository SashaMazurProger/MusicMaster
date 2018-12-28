package com.acrcloud.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

public class Song extends BaseObservable implements Parcelable {
    public static final String KEY = "song";


    public enum TYPE {SONG, FOLDER}

    private TYPE type = TYPE.SONG;
    private String title;
    private String path;
    private boolean editing = false;

    public Song(String path, String title) {
        this.title = title;
        this.path = path;
    }

    public Song(String title, String path, boolean editing) {
        this.title = title;
        this.path = path;
        setIsEditing(editing);
    }


    @Bindable
    public boolean getIsEditing() {
        return editing;
    }

    public void setIsEditing(boolean isEditing) {
        editing = isEditing;
        notifyPropertyChanged(BR.isEditing);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    protected Song(Parcel in) {
        title = in.readString();
        path = in.readString();
        editing = in.readByte() != 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(path);
        dest.writeByte((byte) (editing ? 1 : 0));
    }

}
