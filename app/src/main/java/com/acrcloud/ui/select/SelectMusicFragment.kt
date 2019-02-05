package com.acrcloud.ui.select


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.acrcloud.ui.BR
import com.acrcloud.ui.R
import com.acrcloud.ui.base.BaseFragment
import com.acrcloud.ui.databinding.FragmentMusicFolderBinding
import com.acrcloud.ui.edit.MainNavigator


/**
 * A simple [Fragment] subclass.
 */
class SelectMusicFragment : BaseFragment<FragmentMusicFolderBinding, SelectMusicViewModel>() {


    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_music_folder

    override val viewModel: SelectMusicViewModel
        get() {
            val viewModel = ViewModelProviders.of(activity!!).get(SelectMusicViewModel::class.java)
            viewModel.navigator = activity as MainNavigator?
            return viewModel
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val listView = binding!!.list
        listView.layoutManager = LinearLayoutManager(context)
        listView.adapter = SongAdapter(viewModel)


        super.onViewCreated(view, savedInstanceState)
    }

    override fun bindEvents() {
        super.bindEvents()

        disposable(viewModel.itemSongChangedEvent.subscribe { song ->
            val index = viewModel.files.indexOf(song)
            binding!!.list.adapter!!.notifyItemChanged(index)
        })

    }
}// Required empty public constructor
