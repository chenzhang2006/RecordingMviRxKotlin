package com.chenzhang.mvi.util

import org.apache.commons.lang3.time.FastDateFormat
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateExtTest {

    @Test
    fun testDateFormat() {
        val date = Date(1520650800000)
        assertEquals("Mar 9 10:00 PM", date.toFormatSafe(FastDateFormat.getInstance("MMM d h:mm a")))
    }

    @Test
    fun testNullDateReturnEmptyString() {
        val date: Date? = null
        assertEquals("", date.toFormatSafe(FastDateFormat.getInstance("MMM d h:mm a")))
    }

}