

import com.rentpulse.data.models.*
import com.rentpulse.data.models.payments.PaymentRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    //  AUTH
    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("auth/register.php")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("auth/login.php")
    suspend fun login(@Body request: Map<String, String>): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("otp/send_otp.php")
    suspend fun sendOtp(@Body request: Map<String, String>): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("otp/verify_otp.php")
    suspend fun verifyOtp(@Body request: Map<String, String>): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("api/update_role.php")
    suspend fun updateRole(@Body request: UpdateRoleRequest): ApiResponse


    //  LANDLORDS

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("landlords/landlords.php")
    suspend fun addLandlord(
        @Body request: LandlordRequest
    ): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @GET("landlords/landlords.php")
    suspend fun getLandlord(
        @Header("Authorization") token: String
    ): LandlordResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @PUT("landlords/landlords.php")
    suspend fun updateLandlord(
        @Header("Authorization") token: String,
        @Body request: LandlordRequest
    ): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @DELETE("landlords/landlords.php")
    suspend fun deleteLandlord(
        @Header("Authorization") token: String,
        @Query("id") id: Int? = null // required only for admin delete
    ): ApiResponse

    @GET("landlords/landlord_profile.php")
    suspend fun getLandlordProfile(
        @Query("user_id") userId: Int
    ): LandlordResponse




    //  TENANTS
    @POST("tenants/tenants.php")
    suspend fun addTenant(
        @Body request: TenantRequest
    ): ApiResponse

    @GET("tenants/tenants.php")
    suspend fun getTenant(
        @Header("Authorization") token: String
    ): TenantOuterResponse

    @PUT("tenants/tenants.php")
    suspend fun updateTenant(
        @Header("Authorization") token: String,
        @Body request: TenantRequest
    ): ApiResponse

    @DELETE("tenants/delete_tenant.php")
    suspend fun deleteTenant(
        @Header("Authorization") token: String
    ): ApiResponse


//  PROPERTIES

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @POST("properties/properties.php")
    suspend fun addProperty(
        @Body request: PropertyRequest
    ): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @GET("properties/properties.php")
    suspend fun getProperties(
        @Header("Authorization") token: String
    ): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @PUT("properties/properties.php")
    suspend fun updateProperty(
        @Header("Authorization") token: String,
        @Body request: PropertyRequest
    ): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )
    @DELETE("properties/properties.php")
    suspend fun deleteProperty(
        @Header("Authorization") token: String,
        @Query("id") id: Int? = null // required only for admin delete
    ): ApiResponse

    @GET("api/get_locations.php")
    suspend fun getLocations(): LocationResponse

    @GET("api/get_amenities.php")
    suspend fun getAmenities(): AmenityResponse

    @Multipart
    @POST("api/upload_media.php")
    suspend fun uploadMedia(@Part media: MultipartBody.Part): UploadResponse


// NOTIFICATION

    @GET("api/notifications/get_Notifications.php")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): NotificationResponse

    @POST("api/notifications/mark_Notification_Read.php")
    suspend fun markNotificationRead(
        @Header("Authorization") token: String,
        @Body body: Map<String, Int>
    ): BaseResponseNotification

    @Headers("Content-Type: application/json")
    @POST("users/update_fcm_token.php")
    suspend fun updateFcmToken(
        @Body request: TokenRequest
    ): ApiResponse


    // PAYMENTS

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )

    @POST("payments/payments.php")
    suspend fun savePaymentInfo(
        @Body request: PaymentRequest
    ): ApiResponse

    @Headers(
        "X-App-Username: rentpulse_app_001",
        "X-App-Password: b99marasighan/X"
    )

    @GET("payments/get_payment_info.php")
    suspend fun getPaymentInfo(
    ): PaymentInfo
}