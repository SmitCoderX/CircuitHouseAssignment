package com.smitcoderx.circuithouseassignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.smitcoderx.circuithouseassignment.data.Article
import com.smitcoderx.circuithouseassignment.databinding.ActivityMainBinding
import com.smitcoderx.circuithouseassignment.ui.base.BaseViewModel
import com.smitcoderx.circuithouseassignment.ui.home.HomeFragment
import com.smitcoderx.circuithouseassignment.utils.ResponseState
import com.smitcoderx.circuithouseassignment.utils.hasInternetConnection
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel by viewModels<BaseViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private var category: String = ""
    private var country: String = "in"
    private val LONG_PRESS_TIMEOUT = 2500L
    private var handler = Handler(Looper.getMainLooper())
    private var isLongPress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isNetworkConnectedLiveData.value = this.hasInternetConnection()

        ArrayAdapter.createFromResource(
            this,
            R.array.country_names,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.countrySpinner.adapter = adapter
        }
        binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.countrySpinner.setSelection(position)
                    val list = resources.getStringArray(R.array.country_codes).toList()
                    country = list[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.categorySpinner.setSelection(position)
                    category = if (parent?.getItemAtPosition(position).toString() != "Select Category") {
                        parent?.getItemAtPosition(position).toString().lowercase(Locale.ROOT)
                    } else {
                        ""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.list_fragment, homeFragment)
        transaction.commit()

        viewModel.getTopHeadlines(country, category)
        viewModel.topHeadlines.observe(this) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    binding.listFragment.visibility = View.VISIBLE
                    homeFragment.bindData(it.data!!)
                }

                is ResponseState.Error -> {
                    hideLoading()
                    binding.listFragment.visibility = View.GONE
                    binding.title.text = it.message
                }

                is ResponseState.Loading -> {
                    showLoading()
                }
            }
        }

        homeFragment.setOnContentSelectedListener {
            updateBanner(it)
        }

    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                if (event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    isLongPress = false
                    handler.postDelayed(longPressRunnable, LONG_PRESS_TIMEOUT)
                    return true
                }
            }
            KeyEvent.ACTION_UP -> {
                if (event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    handler.removeCallbacks(longPressRunnable)
                    if (!isLongPress) {
                        // Handle short press: move focus to the next element
                        moveFocusToNextElement()
                    }
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }


    private fun moveFocusToNextElement() {
        // Logic to move focus to the next element
        currentFocus?.focusSearch(View.FOCUS_DOWN)?.requestFocus()
    }

    private val longPressRunnable = Runnable {
        isLongPress = true
        // Call ViewModel function to refresh
        viewModel.getTopHeadlines(country, category)
    }

    private fun updateBanner(dataList: Article) {
        binding.title.text = dataList.title
        binding.description.text = dataList.description
        binding.author.text = dataList.author
        Glide.with(this).load(dataList.urlToImage).into(binding.imgBanner)
    }

    private fun hideLoading() {
        binding.apply {
            progress.visibility = View.GONE
            content.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.apply {
            content.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }

}