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