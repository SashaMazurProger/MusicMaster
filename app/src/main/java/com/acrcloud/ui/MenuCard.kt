package com.acrcloud.ui

import android.content.Context
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MenuCard(context: Context) {

    val view: View

    var title: TextView? = null

    var icon: ImageView? = null


    init {
        view = LayoutInflater.from(context).inflate(R.layout.menu_card_item, null)
        title = view.findViewById(R.id.card_title)
        icon = view.findViewById(R.id.card_icon)
    }

    fun setTitle(title: String) {
        this.title!!.text = title
    }


    fun setIcon(@DrawableRes icon: Int) {
        this.icon!!.setImageResource(icon)
    }
}
