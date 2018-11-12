package com.acrcloud.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Environment;

import com.acrcloud.utils.ACRCloudRecognizer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RenameMusicViewModel extends ViewModel {

    public final ObservableField<String> songPath = new ObservableField<>("hello");
    public final ObservableField<String> folderPath = new ObservableField<>(Environment.getExternalStorageDirectory().toString() + "/Download/soundloadie");
    public final ObservableField<String> result = new ObservableField<>();
    public final ObservableArrayList<Song> songs = new ObservableArrayList<>();
    public final ObservableBoolean loading = new ObservableBoolean(false);

    public void loadFolder(String path) {
        this.folderPath.set(path);
        File folder = new File(folderPath.get());
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".mp3")) {
                songs.add(new Song(file.getAbsolutePath()));
            }
        }
    }

    public void search(Song song) {
        this.songPath.set(song.title);
        new RecThread().start();
    }

    public class Song {
        public String title;

        public Song(String title) {
            this.title = title;
        }
    }


    class RecThread extends Thread {

        public void run() {
            loading.set(true);
            Map<String, Object> config = new HashMap<String, Object>();
            // Replace "xxxxxxxx" below with your project's host, access_key and access_secret.
            config.put("access_key", "37a24216f7bdbfd272dab7035927e4cd");
            config.put("access_secret", "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK");
            config.put("host", "identify-eu-west-1.acrcloud.com");
            config.put("debug", false);
            config.put("timeout", 5);

            ACRCloudRecognizer re = new ACRCloudRecognizer(config);
            String resultStr = re.recognizeByFile(songPath.get(), 10);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loading.set(false);
            result.set(resultStr);
        }
    }
}
