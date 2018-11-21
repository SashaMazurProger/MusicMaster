package com.acrcloud.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.acrcloud.ui.base.BaseActivity;
import com.acrcloud.ui.databinding.ActivityMainBinding;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends BaseActivity<ActivityMainBinding, SelectMusicViewModel> {

    NavController navController;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public SelectMusicViewModel getViewModel() {
        return ViewModelProviders.of(this).get(SelectMusicViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(getViewDataBinding().toolbar);
        setTitle(getString(R.string.app_name));

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        getViewModel().editSong.observe(this, song -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song", song);
            navController.navigate(R.id.action_musicFolderFragment_to_songEditFragment, bundle);
        });

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
