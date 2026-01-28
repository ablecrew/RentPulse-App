package com.rentpulse.util

import android.util.Base64
import android.util.Log
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

// Retrofit response model
data class PublicKeyResponse(
    val status: String,
    val message: String? = null,
    @SerializedName("public_key") val publicKey: String? = null
)

// Retrofit API definition
interface PublicKeyApi {
    @GET("publicKey.php")
    fun getPublicKey(): Call<PublicKeyResponse>
}

object RSAEncryptionHelper {
    private var cachedPublicKey: PublicKey? = null

    // ✅ Retrofit client pointing to your rentpulse-api folder
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.87.238.88/rentpulse-api/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(PublicKeyApi::class.java)

    fun fetchPublicKey(callback: (PublicKey?) -> Unit) {
        // Return cached key if already loaded
        if (cachedPublicKey != null) {
            callback(cachedPublicKey)
            return
        }

        api.getPublicKey().enqueue(object : retrofit2.Callback<PublicKeyResponse> {
            override fun onResponse(
                call: Call<PublicKeyResponse>,
                response: retrofit2.Response<PublicKeyResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success" && body.publicKey != null) {
                        try {
                            //  Clean PEM markers and newlines
                            val cleanKey = body.publicKey
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "")
                                .replace("\\s".toRegex(), "")
                                .trim()

                            val keyBytes = Base64.decode(cleanKey, Base64.DEFAULT)
                            val spec = X509EncodedKeySpec(keyBytes)
                            val factory = KeyFactory.getInstance("RSA")
                            cachedPublicKey = factory.generatePublic(spec)

                            Log.i("RSAEncryptionHelper", "✅ Public key fetched and cached")
                            callback(cachedPublicKey)
                        } catch (e: Exception) {
                            Log.e("RSAEncryptionHelper", "❌ Key parse error", e)
                            callback(null)
                        }
                    } else {
                        Log.e(
                            "RSAEncryptionHelper",
                            "❌ Invalid response: status=${body?.status}, message=${body?.message}"
                        )
                        callback(null)
                    }
                } else {
                    Log.e("RSAEncryptionHelper", "❌ Response not successful: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<PublicKeyResponse>, t: Throwable) {
                Log.e("RSAEncryptionHelper", "❌ Network error: ${t.message}")
                callback(null)
            }
        })
    }

    fun encryptWithPublicKey(data: String, publicKey: PublicKey?): String? {
        return try {
            if (publicKey == null) {
                Log.e("RSAEncryptionHelper", "❌ Cannot encrypt, publicKey is null")
                return null
            }
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            Log.e("RSAEncryptionHelper", "❌ Encryption error", e)
            null
        }
    }
}
