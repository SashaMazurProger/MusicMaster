package com.acrcloud.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.acrcloud.ui.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private RenameMusicViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(RenameMusicViewModel.class);
        binding.setViewModel(viewModel);

        RecyclerView listView = binding.list;
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new SongAdapter(viewModel));

    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"items"})
    public static void list(RecyclerView listView, ObservableList<RenameMusicViewModel.Song> songList) {
        ((SongAdapter) listView.getAdapter()).songs = songList;
        listView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
