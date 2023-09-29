package com.example.imovies.common

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.imovies.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

object Extensions {

    fun String.convertToCurrency(amount: Double): String {
        val locale = Locale.getDefault()
        val currency = Currency.getInstance(this)

        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        numberFormat.currency = currency

        return numberFormat.format(amount)
    }

    fun ImageView.loadImage(imagePath: String) {
        val transformations = mutableListOf<Transformation<Bitmap>>(
            CenterCrop()
        )
        Glide.with(this)
            .load(imagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(*transformations.toTypedArray())
            .into(this)
    }

    private fun <T> debounce(
        waitMs: Long = 300L,
        scope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

    fun EditText.textDebounce(
        waitMs: Long = 300L,
        scope: CoroutineScope,
        destinationFunction: (Editable?) -> Unit
    ) {
        val debounceTextChange = debounce(waitMs, scope, destinationFunction)
        this.addTextChangedListener(afterTextChanged = debounceTextChange)
    }

    fun Activity.hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = currentFocus
        focusedView?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun Context.hasInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    fun String.formatDate(from: String, to: String): String? {
        val inputDateFormat = SimpleDateFormat(from, Locale.US)
        val outputDateFormat = SimpleDateFormat(to, Locale.US)

        val inputDateStr = "Fri Sep 29 15:59:14 GMT+08:00 2023"

        try {
            val date = inputDateFormat.parse(this)
            return date?.let { outputDateFormat.format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}