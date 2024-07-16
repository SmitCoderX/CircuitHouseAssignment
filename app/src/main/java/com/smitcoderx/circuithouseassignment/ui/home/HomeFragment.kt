package com.smitcoderx.circuithouseassignment.ui.home

import android.os.Bundle
import android.view.View
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.smitcoderx.circuithouseassignment.data.Article
import com.smitcoderx.circuithouseassignment.data.NewsData
import com.smitcoderx.circuithouseassignment.ui.base.BaseViewModel
import com.smitcoderx.circuithouseassignment.utils.ResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : RowsSupportFragment() {

    private var itemSelectedListener: ((Article) -> Unit)? = null
    private var rootAdapter: ArrayObjectAdapter =
        ArrayObjectAdapter(ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = rootAdapter

        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    fun bindData(dataList: NewsData) {

        rootAdapter.clear()
        val arrayObjectAdapter = ArrayObjectAdapter(HomePresenter())
        dataList.articles?.forEach {
            arrayObjectAdapter.add(it)
        }

        val listRow = ListRow(arrayObjectAdapter)
        rootAdapter.add(listRow)

    }

    fun setOnContentSelectedListener(listener: (Article) -> Unit) {
        this.itemSelectedListener = listener
    }

    inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Article) {
                itemSelectedListener?.invoke(item)
            }

        }

    }

}