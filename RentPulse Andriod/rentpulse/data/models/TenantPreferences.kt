package com.rentpulse.data.models


import android.content.Context
import android.content.SharedPreferences

class TenantPreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "tenant_prefs"
        private const val KEY_TENANT_ID = "tenant_id"
        private const val KEY_TENANT_NAME = "tenant_name" // Optional
        private const val KEY_TENANT_EMAIL = "tenant_email" // Optional
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Save the logged-in tenant's ID
     */
    fun saveTenantId(userId: Int) {
        sharedPrefs.edit().putInt(KEY_TENANT_ID, userId).apply()
    }

    /**
     * Get the logged-in tenant's ID
     * Returns null if not found
     */
    fun getTenantId(): Int? {
        return if (sharedPrefs.contains(KEY_TENANT_ID)) {
            sharedPrefs.getInt(KEY_TENANT_ID, -1).takeIf { it != -1 }
        } else null
    }

    /**
     * Optional: Save tenant name
     */
    fun saveTenantName(name: String) {
        sharedPrefs.edit().putString(KEY_TENANT_NAME, name).apply()
    }

    /**
     * Optional: Get tenant name
     */
    fun getTenantName(): String? {
        return sharedPrefs.getString(KEY_TENANT_NAME, null)
    }

    /**
     * Optional: Save tenant email
     */
    fun saveTenantEmail(email: String) {
        sharedPrefs.edit().putString(KEY_TENANT_EMAIL, email).apply()
    }

    /**
     * Optional: Get tenant email
     */
    fun getTenantEmail(): String? {
        return sharedPrefs.getString(KEY_TENANT_EMAIL, null)
    }

    /**
     * Clear all saved tenant preferences (logout)
     */
    fun clear() {
        sharedPrefs.edit().clear().apply()
    }
}
