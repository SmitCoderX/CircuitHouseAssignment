package com.smitcoderx.circuithouseassignment.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.smitcoderx.circuithouseassignment.R
import com.smitcoderx.circuithouseassignment.data.Article

class HomePresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {

        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.layout_cateogries, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {

        val content = item as? Article

        val imageview = viewHolder?.view?.findViewById<ImageView>(R.id.iv_image)
        val textView = viewHolder?.view?.findViewById<TextView>(R.id.tv_headline)

        textView?.text = content?.title

        Glide.with(viewHolder?.view?.context!!)
            .load(content?.urlToImage)
            .into(imageview!!)

    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
    }

}