package com.example.imovies.data.repository.common.local.preferences

import android.content.SharedPreferences
import javax.inject.Inject

/** Uses Shared Preferences as persistence mechanism
 * because this app only saves string in key-value format **/
class AppPreferences @Inject constructor (
    private val sharedPrefs: SharedPreferences
) {

    fun saveString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPrefs.getString(key, "")
    }
}