package com.example.imovies.ui.main

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.imovies.common.Constant.LAST_DATE_VISIT
import com.example.imovies.common.Extensions.hasInternet
import com.example.imovies.data.repository.common.local.preferences.AppPreferences
import com.example.imovies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var connectivityManager: ConnectivityManager
    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInternetObserver()
        mainViewModel.setLastDateVisit(appPreferences.getString(LAST_DATE_VISIT).orEmpty())
    }

    private fun setupInternetObserver() {
        showNoInternetConnection()
        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    showNoInternetConnection(true)
                }

                override fun onLost(network: Network) {
                    showNoInternetConnection(false)
                }
            })
    }

    private fun showNoInternetConnection(hasInternet: Boolean = hasInternet()) {
        lifecycleScope.launch {
            binding.noInternetTextView.isVisible = hasInternet.not()
        }
    }
}