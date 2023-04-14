package com.example.myapplication

import java.io.Serializable

data class WeatherRequest(
     var city: String = "",
    var lat: String = "",
    var lon: String = ""
    ): Serializable

