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

        // Checks if the internet connection is available or not.
        viewModel.isNetworkConnectedLiveData.value = this.hasInternetConnection()

        // Country Spinner Adapter
        ArrayAdapter.createFromResource(
            this,
            R.array.country_names,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.countrySpinner.adapter = adapter
        }

        /* Country Spinner Listener and also storing the selected item in the country variable. Storing the country code in the variable.
        * As the country names list and the country codes are stored in the same manner so retrieving the country code according to the position. */
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

        // Category Spinner Adapter
        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }

        /* Category Spinner Listener and also storing the selected item in the category variable. If the selected item is Select Category it
         * will store empty string else it will store the selected item in the category variable in lowercase to fetch the details accordingly.
         * */
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

        // Initially Calls the getTopHeadline with country India and empty category
        viewModel.getTopHeadlines(country, category)

        // Handles the State of the data received from the ViewModel
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

        // Updates the banner with respect to the selected item from the list.
        homeFragment.setOnContentSelectedListener {
            updateBanner(it)
        }

    }

    /*
     * Handled long press Dpad down to refresh functionality. Refresh works after 2.5 seconds.
     * You can change the time by changing the LONG_PRESS_TIMEOUT variable.
     * Also handle short press of the Dpad down button to focus on the next element by calling moveFocusToNextElement().
     * */
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


    // Function to move focus to the next element
    private fun moveFocusToNextElement() {
        // Logic to move focus to the next element
        currentFocus?.focusSearch(View.FOCUS_DOWN)?.requestFocus()
    }

    // Runnable to handle long press refresh
    private val longPressRunnable = Runnable {
        isLongPress = true
        // Call ViewModel function to refresh
        viewModel.getTopHeadlines(country, category)
    }

    // Function to set data to the banner.
    private fun updateBanner(dataList: Article) {
        binding.title.text = dataList.title
        binding.description.text = dataList.description
        binding.author.text = dataList.author
        Glide.with(this).load(dataList.urlToImage).into(binding.imgBanner)
    }


    // Function to hide the loading progress bar.
    private fun hideLoading() {
        binding.apply {
            progress.visibility = View.GONE
            content.visibility = View.VISIBLE
        }
    }

    // Function to show the loading progress bar.
    private fun showLoading() {
        binding.apply {
            content.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }

}