package com.acrcloud.ui.edit


import android.arch.lifecycle.ViewModelProviders
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView

import com.acrcloud.ui.BR
import com.acrcloud.ui.R
import com.acrcloud.ui.Song
import com.acrcloud.ui.base.BaseFragment
import com.acrcloud.ui.databinding.FragmentSongEditBinding


/**
 * A simple [Fragment] subclass.
 */
class SongEditFragment : BaseFragment<FragmentSongEditBinding, SongEditViewModel>() {


    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_song_edit

    override val viewModel: SongEditViewModel
        get() {
            val viewModel = ViewModelProviders.of(this).get(SongEditViewModel::class.java)
            viewModel.navigator = activity as MainNavigator?
            viewModel.editSong.value = arguments!!.getParcelable(Song.KEY)
            return viewModel
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.readMetadata()

    }

    //    @Override
    //    protected void bindEvents() {
    //        super.bindEvents();
    //
    //        getViewModel().getCoverArt().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
    //            @Override
    //            public void onPropertyChanged(Observable sender, int propertyId) {
    //                ((ImageView) getBinding().imageView).setImageBitmap(getViewModel().getCoverArt().get());
    //            }
    //        });
    //    }
}// Required empty public constructor
