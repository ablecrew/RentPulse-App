package com.rentpulse.utils

import android.content.Context

object AuthPreferences {
    private const val PREFS_NAME = "RentPulsePrefs"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_USER_ROLE = "user_role"

    // ✅ Save token
    fun saveAuthToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    // ✅ Read token
    fun readAuthToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_AUTH_TOKEN, null)
    }

    // ✅ Clear token
    fun clearAuthToken(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .remove(KEY_AUTH_TOKEN)
            .apply()
    }

    // ✅ Save role (tenant / landlord)
    fun saveUserRole(context: Context, role: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .putString(KEY_USER_ROLE, role)
            .apply()
    }

    // ✅ Read role
    fun readUserRole(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USER_ROLE, null)
    }

    // ✅ Clear role
    fun clearUserRole(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .remove(KEY_USER_ROLE)
            .apply()
    }
}
