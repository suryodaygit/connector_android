
import com.example.logintask.onboarding.signup.SendOTPRequestModel
import com.example.logintask.onboarding.signup.SendOTPResponseModel
import com.example.logintask.onboarding.signup.ValidateOTPResponseModel
import com.example.logintask.onboarding.splash.CheckVersionModel
import com.example.logintask.onboarding.splash.Data
import com.example.logintask.onboarding.splash.VersionRequestModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * APIService class is used to get singleton Retrofit instance and make call to server to retrieve data from server.
 * It is also used to declare API references with their input parameters.
 *
 * @author Komal Ardekar
 */
interface APIService {

    companion object {

        /**
         * @return Instance of RetrofitAPI class
         */
        fun getBaseUrl(): APIService {

            val instance: APIService by lazy {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getClient())
                    .baseUrl("https://intramashery.suryodaybank.co.in/")
                    .build()
                retrofit.create(APIService::class.java)
            }

            return instance
        }

        /**
         * @return Instance of OkHttpClient class with modified timeout
         */
        private fun getClient(): OkHttpClient {
            val httpTimeout: Long = 20
            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.connectTimeout(httpTimeout, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(httpTimeout, TimeUnit.SECONDS)
            return okHttpClientBuilder.build()
        }
    }

    //http://10.20.25.15:443/aocpv/v1/checkappversion
    @POST("checkappversion")
    fun checkVersion(@Header("X-Correlation-ID") X_Correlation_ID: String,
                     @Header("X-From-ID") X_From_ID: String,
                     @Header("X-To-ID") X_To_ID: String,
                     @Header("X-Transaction-ID")X_Transaction_ID:String,
                     @Header("X-User-ID")X_User_ID : String,
                     @Header("X-Request-ID")X_Request_ID:String,
                     @Header("Content-Type")content_type:String,
                     @Header("Cookie")cookie:String,
                     @Body Data: VersionRequestModel): Call<CheckVersionModel>


    //https://intramashery.suryodaybank.co.in/notification/otp/sms?api_key=kyqak5muymxcrjhc5q57vz9v
    @POST("notification/otp/sms/{api_key}")
    fun sendMobileOTP(
                      @Header("X-Correlation-ID") X_Correlation_ID :String,
                      @Header("X-Request-ID") X_Request_ID :String,
                      @Header("X-User-ID") X_User_ID :String,
                      @Header("X-From-ID") X_From_ID:String,
                      @Header("X-To-ID") X_To_ID:String,
                      @Header("X-Transaction-ID") X_Transaction_ID:String,
                      @Header("Content-Type") Content_Type:String,
                      @Path("api_key") api_key :String,
                      @Body sendOtp:SendOTPRequestModel): Call<SendOTPResponseModel>

    //https://intramashery.suryodaybank.co.in/transaction/OTP/validate?api_key=kyqak5muymxcrjhc5q57vz9v&OTP=422028&TransactionType=D
    @GET("transaction/OTP/validate?{api_key}/{OTP}/{TransactionType}")
    fun validateMobileOTP(@Path("api_key")api_key:String,
                          @Path("OTP") OTP:String,
                          @Path("TransactionType") TransactionType:String,
                          @Header("X-Correlation-ID") X_Correlation_ID:String,
                          @Header("X-Request-ID") X_Request_ID:String,
                          @Header("X-User-ID") X_User_ID:String,
                          @Header("X-From-ID") X_From_ID:String,
                          @Header("X-To-ID") X_To_ID:String,
                          @Header("X-Transaction-ID") X_Transaction_ID:String,
                          @Header("Content-Type") Content_Type:String) : Call<ValidateOTPResponseModel>

}