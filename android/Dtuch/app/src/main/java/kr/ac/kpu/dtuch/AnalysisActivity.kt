package kr.ac.kpu.dtuch

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_analysis.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class AnalysisActivity : AppCompatActivity() {

    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: RetrofitAPI
    lateinit var mCallResult : Call<Result>

    companion object{
        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "91197d9c5df4f1f28124f89fd91f86d8"
        var lat = "37.445293"
        var lon = "126.785823"
        var bio_mind = 50.123
        var bio_body = 50.123
        var coffee = "예르가체프"
        var time = "00:00"
        var cTemp = 50.123
        var cHum = 50.123
        var result: Result = Result("예르가체프","10")

        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        // 블루투스 송수신
        m_address= intent.getStringExtra(PreAnalysisAcitivity.EXTRA_ADDRESS).toString()

        ConnectToDevice(this).execute()


        if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) { return }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val bottomSheetFragment = BottomSheetFragment()

        val firstName = intent.getStringExtra("first_name")
        val lastName = intent.getStringExtra("last_name")
        var birthDay = intent.getStringExtra("birthDay")
        var birthMonth = intent.getStringExtra("birthMonth")
        val birthYear = intent.getStringExtra("birthYear")
        var today = LocalDateTime.now()

        if (birthMonth != null) {
            if(birthMonth.toInt()<10){
                birthMonth = "0" + birthMonth
            }
        }

        if (birthDay != null) {
            if(birthDay.toInt()<10){
                birthDay = "0" + birthDay
            }
        }

        val birth_date = LocalDateTime.parse(birthYear + "-" + birthMonth + "-" + birthDay+"T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        var duration = Duration.between(birth_date, today)
        var diff = Math.abs(duration.toMinutes()) / (60 * 24)
        //Log.e("WE_RE:", diff.toString() )

        bio_mind = Math.sin((diff.toDouble() * Math.PI * 2 )/ 28)
        bio_body = Math.sin((diff.toDouble() * Math.PI * 2 )/ 23)

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            lat = location?.latitude.toString()
            lon = location?.longitude.toString()
            Log.e("Tag","result -> lat:"+ lat + "\nlon:" + lon)
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("AnalysisActivity", "result :" + t.message)
            }

            override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
            ) {
                if(response.code() == 200){
                    val weatherResponse = response.body()
                    Log.d("AnalysisActivity", "result: " + weatherResponse.toString())
                    cTemp =  round(weatherResponse!!.main!!.temp - 273.15)  //켈빈을 섭씨로 변환
                    cHum = round(weatherResponse!!.main!!.humidity.toDouble())
                    val stringBuilder =
                            "안녕하세요. " + lastName + firstName + "님.\n당신의 마음을 담은 콜드브루 커피 소닉 더치입니다." + "\n\n" +
                            "현재 " + weatherResponse!!.sys!!.country + "시의 기온은 " + cTemp + "입니다.\n" +
                            "현재 습도는 "+ cHum +"입니다.\n"+
                            "당신이 살아온 날은 총 " + diff.toString() + "입니다.\n" +
                            "오늘 당신의 바이오 리듬의\n감성지수가 "+ round(bio_mind).toString() +"입니다."
                            "감사합니다."
                    weather_tv.text = stringBuilder
                }
            }
        })

        val mOkHttpClient = OkHttpClient.Builder()      // 기본값: 100
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        mRetrofit = Retrofit
            .Builder()
            .baseUrl("http://192.168.124.40:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("temp", cTemp.toString())
            .addFormDataPart("bio_mind", bio_mind.toString())
            .addFormDataPart("humidity", cHum.toString())
            .build()

        val request = Request.Builder().url("http://192.168.124.40:5000/").post(requestBody).build()
        val callFlask = mOkHttpClient.newCall(request)

        callFlask.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("TAG",e.toString())
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                callResult()
            }
        })

    extract_button.setOnClickListener{
        val bundle = Bundle()
        bundle.putString("coffee", coffee)

        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")

        sendMessage(result.toString())

        }
    }

    private fun callResult() {
        mCallResult = mRetrofitAPI.getResult(coffee, time)
        mCallResult.enqueue(mRetrofitCallback)
    }

    private val mRetrofitCallback  = (object : Callback<Result>{
        override fun onFailure(call: Call<Result>, t: Throwable) {
            t.printStackTrace()
            Log.d(TAG, "틀렸다")
        }

        override fun onResponse(call: Call<Result>, response: Response<Result>) {
            result = response.body()!!
            Log.d(TAG, "결과는 => $result")
            Toast.makeText(this@AnalysisActivity,"예측 결과: $result", Toast.LENGTH_SHORT).show()
            coffee_tv.text = result.coffee
            time_tv.text = result.time
        }
    })

    private fun sendMessage(input: String){
        if(m_bluetoothSocket != null){
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch (e:IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect(){
        if(m_bluetoothSocket != null){
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e:IOException){
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {

        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c

        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e:IOException){
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }

}

interface RetrofitAPI {
    @GET("/")
    fun getResult(@Query("coffee") coffee: String,
                    @Query("time") time: String) : Call<Result>
}

data class Result(
    val coffee: String,
    val time: String
)

// ===================================== 클래스 정의 ============================================

interface WeatherService{

    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("appid") appid: String) :
            Call<WeatherResponse>
}

class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("wind") var wind : Wind? = null
    @SerializedName("sys") var sys: Sys? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon : String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country")
    var country: String? = null
    @SerializedName("sunrise")
    var sunrise: Long = 0
    @SerializedName("sunset")
    var sunset: Long = 0
}