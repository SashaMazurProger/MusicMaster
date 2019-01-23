package com.acrcloud.ui

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.acrcloud.ui.base.BaseActivity
import com.acrcloud.ui.databinding.ActivityMainBinding
import com.acrcloud.ui.edit.MainNavigator

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {


    override fun openListenMusicScreen() {
        navController!!.navigate(R.id.listenFragment)
    }


    internal var navController: NavController? = null

    override fun openEditMusicScreen() {
        navController!!.navigate(R.id.musicFolderFragment)
    }

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel
        get() {
            return ViewModelProviders.of(this).get(MainViewModel::class.java)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(viewDataBinding!!.toolbar)
        title = getString(R.string.app_name)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        if (!(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) || hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))) {

            val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            requestPermissionsSafely(perms, STORAGE_PERMISSION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {

            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onApplyEditOpenedSong() {
        navController!!.popBackStack()
    }

    override fun onItemSongSelected(song: Song) {
        val bundle = Bundle()
        bundle.putParcelable(Song.KEY, song)
        navController!!.navigate(R.id.action_musicFolderFragment_to_songEditFragment, bundle)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            STORAGE_PERMISSION -> {
                if (grantResults.size > 1) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        finish()
                    }
                }
            }
        }
    }

    companion object {

        private val STORAGE_PERMISSION = 1
    }

}
