package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.recycler.CustomAdapter
import com.example.myapplication.recycler.ItemsViewModel
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : AppCompatActivity() {

    var weatherObject:WeatherResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val weather = intent.getSerializableExtra("weather") as WeatherRequest
        getCurrentData(weather)

        toggleTempType()
    }

    private fun getCurrentData(weather:WeatherRequest) {
        val retrofit = Retrofit.Builder()
            .baseUrl(AppUtil.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(weather.city, weather.lat, weather.lon, AppUtil.AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    weatherObject = weatherResponse
                    weatherResponse?.let { updateView(it, TempType.CELCIUS) }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("error oo", t.message.toString())
                showError()
            }
        })
    }

    private fun updateView(weatherResponse:WeatherResponse, tempType: TempType){
        var imageUrl = ""
        if (weatherResponse.weather.isNotEmpty()){
            imageUrl = "https://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}@2x.png"
        }
        val weatherImage:ImageView = findViewById(R.id.weatherImage);
        Glide.with(this).load(imageUrl)
            .error(R.drawable.ic_launcher_background)
            .into(weatherImage);

        val cityTextView: TextView = findViewById(R.id.city)
        cityTextView.text = weatherResponse.name.toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        val tempTextView: TextView = findViewById(R.id.temp)
        val temp = if (tempType == TempType.CELCIUS) weatherResponse.main?.temp?.toCelcius().toString() else weatherResponse.main?.temp?.toFaherenheit().toString()
        tempTextView.text = temp

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = buildItemListFromWeather(weatherResponse, tempType)
        val adapter = CustomAdapter(data)
        recyclerview.adapter = adapter
    }

    private fun showError(){
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Not Found")
        alertDialog.setMessage("Sorry we could not found the city, " +
                "please try again with correct entry")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun buildItemListFromWeather(weatherResponse:WeatherResponse, tempType: TempType) : List<ItemsViewModel>{
        val data = ArrayList<ItemsViewModel>()
        val mintemp: String
        val maxtemp: String
        if (tempType == TempType.FAHRENEHEIT){
            mintemp = weatherResponse.main?.temp_min?.toFaherenheit().toString()
            maxtemp =  weatherResponse.main?.temp_max?.toFaherenheit().toString()
        }else{
            mintemp = weatherResponse.main?.temp_min?.toCelcius().toString()
            maxtemp =  weatherResponse.main?.temp_max?.toCelcius().toString()
        }
        data.add(ItemsViewModel("Min Temperature", mintemp))
        data.add(ItemsViewModel("Max Temperature", maxtemp))
        data.add(ItemsViewModel("Cloud Coverage", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Latitude", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Latitude", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Longitude", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Sunrise", weatherResponse.sys?.sunrise.toString()))
        data.add(ItemsViewModel("Sunset", weatherResponse.sys?.sunset.toString()))
        return data
    }

    private fun toggleTempType() {
        val tabLayout: TabLayout =
            findViewById(R.id.weatherToggleTab); // get the reference of TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == 0) weatherObject?.let { updateView(it, TempType.CELCIUS) } else  weatherObject?.let { updateView(it, TempType.FAHRENEHEIT) }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}





