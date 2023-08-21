package com.example.codechallenge.utils

object Utils {

    fun getDifferenceInSeconds(startDate: Long, endDate: Long): Long {
        //milliseconds
        var different: Long = endDate - startDate
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli

        return elapsedMinutes
    }

}