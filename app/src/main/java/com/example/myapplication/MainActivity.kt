package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpButtons()
    }

    private fun setUpButtons(){
        findViewById<View>(R.id.buttonDelhi).setOnClickListener {
            getCity("Delhi")
        }

        findViewById<View>(R.id.buttonBerlin).setOnClickListener {
            getCity("Berlin")
        }

        findViewById<View>(R.id.buttonToronto).setOnClickListener {
            getCity("Toronto")
        }

        findViewById<View>(R.id.searchButton).setOnClickListener {
           getDataFromTextFields()
        }
    }

    private fun getCity(selectedCity:String){
        val weather = WeatherRequest(selectedCity)
        moveToNextPage(weather)
    }

    private fun getDataFromTextFields(){
        val lat:EditText = findViewById(R.id.editTextLatitude)
        val long:EditText = findViewById(R.id.editTextLatitude)
        val city:EditText = findViewById(R.id.editTextLatitude)
        val weather = WeatherRequest(city.text?.toString() ?: "", lat.text?.toString() ?: "", long.text?.toString() ?: "")
        moveToNextPage(weather)
    }

    private fun moveToNextPage(weather:WeatherRequest){
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("weather", weather)
        startActivity(intent)
    }
}