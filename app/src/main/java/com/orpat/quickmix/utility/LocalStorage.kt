package com.orpat.quickmix.utility


import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class LocalStorage(context: Context){
    var AUTH_TOKEN_KEY = "X-AUTH-TOKEN"
    var IS_USER_LOGGED_IN_KEY = "isUserLoggedIn"
    var IS_TOKEN_PRESENT_KEY = "isTokenPresent"
    var USER_DATA_KEY = "userdata"


    /// Get instance of shared preferences in private mode
    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences(Constants.APP_PREFS_QUICKMIX, Context.MODE_PRIVATE)


    /// Get instance of Editor
    private val editor: SharedPreferences.Editor = sharedPrefs.edit()

    fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when(value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }



    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }


    var token: String
        get() = sharedPrefs.getString(AUTH_TOKEN_KEY, "").toString()
        set(value) = sharedPrefs.edit().putString(AUTH_TOKEN_KEY, value).apply()


    var isTokenPresent: Boolean
        get() = sharedPrefs.getBoolean(IS_TOKEN_PRESENT_KEY, false)
        set(value) = sharedPrefs.edit().putBoolean(IS_TOKEN_PRESENT_KEY, value).apply()

    var isUserRegistered: Boolean
        get() = sharedPrefs.getBoolean(IS_USER_LOGGED_IN_KEY, false)
        set(value) = sharedPrefs.edit().putBoolean(IS_USER_LOGGED_IN_KEY, value).apply()


    var userObject : String
        get() = sharedPrefs.getString(USER_DATA_KEY,"").toString()
        set(value) = sharedPrefs.edit().putString(USER_DATA_KEY,value).apply()

//    fun clearPref() {
//        putValue("", USER_THEME_PREF_KEY)
//        putValue("", USER_LANG_PREF_KEY)
//    }
}