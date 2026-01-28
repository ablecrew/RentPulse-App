package com.rentpulse.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rentpulse.data.models.LandlordData

object LandlordPreferences {
    private const val PREFS_NAME = "RentPulseLandlordPrefs"

    // Basic landlord info
    private const val KEY_LANDLORD_USER_ID = "landlord_user_id"
    private const val KEY_LANDLORD_NAME = "landlord_name"
    private const val KEY_LANDLORD_PHONE = "landlord_phone"
    private const val KEY_LANDLORD_EMAIL = "landlord_email"
    private const val KEY_LANDLORD_PROFILE_IMAGE = "landlord_profile_image"
    private const val KEY_LANDLORD_ID_NUMBER = "landlord_id_number"
    private const val KEY_ALLOW_UPDATES = "allow_updates"

    // Payment info
    private const val KEY_PAYMENT_METHOD = "payment_method"
    private const val KEY_ACCOUNT_DETAILS = "account_details"
    private const val KEY_MULTIPLE_ACCOUNTS = "multiple_accounts"
    private const val KEY_TENANT_FEES = "tenant_fees"

    // Dashboard
    private const val KEY_PROPERTIES = "properties"
    private const val KEY_TENANTS = "tenants"
    private const val KEY_TOTAL_EARNINGS = "total_earnings"
    private const val KEY_PENDING_PAYMENTS = "pending_payments"

    fun saveLandlordDetails(
        context: Context,
        fullName: String,
        phone: String,
        email: String,
        idNumber: String?,
        profileImage: String?,
        properties: List<String>?,
        tenants: List<String>?,
        totalEarnings: Double,
        pendingPayments: Double,
        allowUpdates: Boolean = true
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()

        prefs.edit()
            .putString(KEY_LANDLORD_NAME, fullName)
            .putString(KEY_LANDLORD_PHONE, phone)
            .putString(KEY_LANDLORD_EMAIL, email)
            .putString(KEY_LANDLORD_ID_NUMBER, idNumber ?: "")
            .putString(KEY_LANDLORD_PROFILE_IMAGE, profileImage ?: "")
            .putString(KEY_PROPERTIES, gson.toJson(properties ?: emptyList<String>()))
            .putString(KEY_TENANTS, gson.toJson(tenants ?: emptyList<String>()))
            .putFloat(KEY_TOTAL_EARNINGS, totalEarnings.toFloat())
            .putFloat(KEY_PENDING_PAYMENTS, pendingPayments.toFloat())
            .putBoolean(KEY_ALLOW_UPDATES, allowUpdates)
            .apply()
    }

    fun readLandlordDetails(context: Context): LandlordData? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type

        val propertiesJson = prefs.getString(KEY_PROPERTIES, null)
        val tenantsJson = prefs.getString(KEY_TENANTS, null)

        val properties: List<String>? = propertiesJson?.let { gson.fromJson(it, listType) }
        val tenants: List<String>? = tenantsJson?.let { gson.fromJson(it, listType) }

        return LandlordData(
            user_id = prefs.getInt(KEY_LANDLORD_USER_ID, 0),
            name = prefs.getString(KEY_LANDLORD_NAME, "") ?: "",
            phone = prefs.getString(KEY_LANDLORD_PHONE, "") ?: "",
            email = prefs.getString(KEY_LANDLORD_EMAIL, "") ?: "",
            id_number = prefs.getString(KEY_LANDLORD_ID_NUMBER, "") ?: "",
            profile_image = prefs.getString(KEY_LANDLORD_PROFILE_IMAGE, "") ?: "",
            properties = properties,
            tenants = tenants,
            total_earnings = prefs.getFloat(KEY_TOTAL_EARNINGS, 0f).toDouble(),
            pending_payments = prefs.getFloat(KEY_PENDING_PAYMENTS, 0f).toDouble(),
            allow_updates = prefs.getBoolean(KEY_ALLOW_UPDATES, true)
        )
    }


    fun savePaymentPreferences(
        context: Context,
        method: String?,
        accountDetails: String?,
        multipleAccounts: Boolean,
        tenantFees: Boolean,
        allowUpdates: Boolean
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_PAYMENT_METHOD, method)
            .putString(KEY_ACCOUNT_DETAILS, accountDetails)
            .putBoolean(KEY_MULTIPLE_ACCOUNTS, multipleAccounts)
            .putBoolean(KEY_TENANT_FEES, tenantFees)
            .putBoolean(KEY_ALLOW_UPDATES, allowUpdates)
            .apply()
    }




        fun getLandlordName(context: Context): String? =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANDLORD_NAME, null)

        fun getLandlordPhone(context: Context): String? =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANDLORD_PHONE, null)

        fun getLandlordEmail(context: Context): String? =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANDLORD_EMAIL, null)

        fun getLandlordProfileImage(context: Context): String? =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANDLORD_PROFILE_IMAGE, null)


        // Payment preferences getters
        fun getPaymentMethod(context: Context): String? =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_PAYMENT_METHOD, null)

        fun getAccountDetails(context: Context): String? =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_ACCOUNT_DETAILS, null)

        fun hasMultipleAccounts(context: Context): Boolean =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_MULTIPLE_ACCOUNTS, false)

        fun tenantPaysFees(context: Context): Boolean =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_TENANT_FEES, false)

        fun allowProfileUpdates(context: Context): Boolean =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_ALLOW_UPDATES, false)

        fun clearLandlordDetails(context: Context) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        }
    }
