package com.acrcloud.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.acrcloud.ui.base.BaseActivity;
import com.acrcloud.ui.databinding.ActivityMainBinding;
import com.acrcloud.ui.edit.MainNavigator;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator {

    private static final int STORAGE_PERMISSION = 1;
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
    public MainViewModel getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(getViewDataBinding().toolbar);
        setTitle(getString(R.string.app_name));

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (!(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))) {

            final String[] perms = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

            requestPermissionsSafely(perms, STORAGE_PERMISSION);
        }
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

    @Override
    public void onApplyEditOpenedSong() {
        navController.popBackStack();
    }

    @Override
    public void onItemSongSelected(Song song) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Song.KEY, song);
        navController.navigate(R.id.action_musicFolderFragment_to_songEditFragment, bundle);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case STORAGE_PERMISSION: {
                if (grantResults.length > 1) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        finish();
                    }
                }
            }
        }
    }

}
