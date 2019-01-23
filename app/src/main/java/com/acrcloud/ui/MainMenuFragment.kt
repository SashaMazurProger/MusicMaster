package com.acrcloud.ui


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.acrcloud.ui.base.BaseFragment
import com.acrcloud.ui.databinding.FragmentMainMenuBinding
import com.acrcloud.ui.edit.MainNavigator
import kotlinx.android.synthetic.main.fragment_main_menu.*

/**
 * A simple [Fragment] subclass.
 */
class MainMenuFragment : BaseFragment<FragmentMainMenuBinding, MenuViewModel>() {

    override val viewModel: MenuViewModel
        get() {
            val vm = ViewModelProviders.of(this).get(MenuViewModel::class.java)
            vm.navigator = activity as MainNavigator
            return vm
        }


    override val bindingVariable: Int
        get() {
            return BR.viewModel
        }

    override val layoutId: Int
        get() {
            return R.layout.fragment_main_menu
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuCard = MenuCard(context!!)
        menuCard.setTitle("Edit music")
        menuCard.setIcon(R.drawable.ic_if_play)
        menuCard.view.setOnClickListener {
            viewModel.editMusicScreen()
        }
        cards_c!!.addView(menuCard.view)

        val menuCard1 = MenuCard(context!!)
        menuCard1.setTitle("Recognize music")
        menuCard1.setIcon(R.drawable.ic_file)
        cards_c!!.addView(menuCard1.view)
        menuCard1.view.setOnClickListener { viewModel.listenMusicScreen() }

        val menuCard2 = MenuCard(context!!)
        menuCard2.setTitle("Settings")
        menuCard2.setIcon(R.drawable.ic_player)
        cards_c!!.addView(menuCard2.view)
    }
}
