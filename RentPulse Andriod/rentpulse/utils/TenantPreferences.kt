package com.rentpulse.utils

import android.content.Context
import com.rentpulse.data.models.TenantData

object TenantPreferences {
    private const val PREFS_NAME = "RentPulseTenantPrefs"
    private const val KEY_TENANT_NAME = "tenant_name"
    private const val KEY_TENANT_PHONE = "tenant_phone"
    private const val KEY_TENANT_ID_NUMBER = "tenant_id_number"
    private const val KEY_TENANT_PROFILE_IMAGE = "tenant_profile_image"
    private const val KEY_TENANT_ALLOW_UPDATES = "tenant_allow_updates"

    // ✅ Save Tenant details
    fun saveTenantDetails(
        context: Context,
        fullName: String,
        phone: String,
        idNumber: String,
        profileImage: String?,
        allowUpdates: Boolean = true
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_TENANT_NAME, fullName)
            .putString(KEY_TENANT_PHONE, phone)
            .putString(KEY_TENANT_ID_NUMBER, idNumber)
            .putString(KEY_TENANT_PROFILE_IMAGE, profileImage ?: "")
            .putBoolean(KEY_TENANT_ALLOW_UPDATES, allowUpdates)
            .apply()
    }

    // ✅ Read all details as TenantData object
    fun readTenantDetails(context: Context): TenantData? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_TENANT_NAME, null)
        val phone = prefs.getString(KEY_TENANT_PHONE, null)
        val idNumber = prefs.getString(KEY_TENANT_ID_NUMBER, null)
        val profileImage = prefs.getString(KEY_TENANT_PROFILE_IMAGE, null)
        val allowUpdates = prefs.getBoolean(KEY_TENANT_ALLOW_UPDATES, true)

        return if (name != null && phone != null && idNumber != null) {
            TenantData(
                name = name,
                phone = phone,
                id_number = idNumber,
                profile_image = profileImage ?: "",
                allow_updates = allowUpdates,
                house_number = null
            )
        } else {
            null
        }
    }

    // === Existing individual getters (kept for compatibility) ===
    fun getTenantName(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TENANT_NAME, null)

    fun getTenantPhone(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TENANT_PHONE, null)

    fun getTenantIdNumber(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TENANT_ID_NUMBER, null)

    fun getTenantProfileImage(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TENANT_PROFILE_IMAGE, null)

    fun getTenantAllowUpdates(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_TENANT_ALLOW_UPDATES, true)

    // ✅ Clear all tenant details
    fun clearTenantDetails(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}

