package com.example.myapplication

import android.app.AlertDialog
import android.os.AsyncTask
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val weather = intent.getSerializableExtra("weather") as WeatherRequest

        AsyncTask.execute {
            getCurrentData(weather)
        }

    }

    private fun getCurrentData(weather:WeatherRequest) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(weather.city, weather.lat, weather.lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    weatherResponse?.let { updateView(it) }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("error oo", t.message.toString())
                showError()
            }
        })
    }

    private fun updateView(weatherResponse:WeatherResponse){
       val imageUrl = "https://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}@2x.png"
        val weatherImage:ImageView = findViewById(R.id.weatherImage);
        Glide.with(this).load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(weatherImage);

        val cityTextView: TextView = findViewById(R.id.city)
        cityTextView.text = weatherResponse.name.toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        val tempTextView: TextView = findViewById(R.id.temp)
        tempTextView.text = "${weatherResponse.main?.temp.toString()} ${"\u2103"}"

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = buildItemListFromWeather(weatherResponse)
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

    companion object {
        var BaseUrl = "https://api.openweathermap.org/data/2.5/"
        var AppId = "5429b6a8bee19bb06bb4ef54409fe206"
    }

//    fun toggleTempType(){
//        val segment = findViewById<SegmentedButton>(R.id.segmented)
//        segment.initialCheckedIndex = 0
//
//        // init with segments programmatically without RadioButton as a child in xml
//        segment.initWithItems {
//            // takes only list of strings
//            listOf("Today", "This week")
//        }
//        segment.onSegmentChecked { segment ->
//            Log.d("creageek:segmented", "Segment ${segment.text} checked")
//        }
//    }

    private fun buildItemListFromWeather(weatherResponse:WeatherResponse) : List<ItemsViewModel>{
        val data = ArrayList<ItemsViewModel>()
        data.add(ItemsViewModel("Min Temperature", weatherResponse.main?.temp_min.toString()))
        data.add(ItemsViewModel("Max Temperature", weatherResponse.main?.temp_min.toString()))
        data.add(ItemsViewModel("Cloud Coverage", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Latitude", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Latitude", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Longitude", weatherResponse.clouds?.all.toString()))
        data.add(ItemsViewModel("Sunrise", weatherResponse.sys?.sunrise.toString()))
        data.add(ItemsViewModel("Sunset", weatherResponse.sys?.sunset.toString()))
        return data
    }
}