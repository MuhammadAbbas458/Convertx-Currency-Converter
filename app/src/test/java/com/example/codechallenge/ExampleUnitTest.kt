package com.example.codechallenge

import com.example.codechallenge.utils.Utils
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {

    @Test
    fun testTimeDifference(){
        assertTrue(Utils.getDifferenceInSeconds(1692543255152,1692543255152 + 1900000) > 30)
        assertFalse(Utils.getDifferenceInSeconds(1692543255152,1692543255152 + 1800) > 30)
    }
}