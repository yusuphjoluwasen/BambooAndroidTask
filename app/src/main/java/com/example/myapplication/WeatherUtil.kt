package com.example.myapplication

enum class TempType{
    CELCIUS, FAHRENEHEIT
}

fun Float.toFaherenheit():String{
    return "$this ${"\u2109"}"
}

fun Float.toCelcius():String{
    val cel = (this - 32) * 5/9
    return "${String.format("%.2f", cel)} ${"\u2103"}"
}

object AppUtil {
    var BaseUrl = "https://api.openweathermap.org/data/2.5/"
    var AppId = "5429b6a8bee19bb06bb4ef54409fe206"
}