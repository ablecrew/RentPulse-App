import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object RetrofitClient {
     const val BASE_URL = "http://10.87.238.88/rentpulse-api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
    fun createFilePart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}")
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
        val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), tempFile)
        return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
    }
}

