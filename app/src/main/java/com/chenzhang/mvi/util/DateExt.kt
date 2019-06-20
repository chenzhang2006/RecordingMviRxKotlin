package com.chenzhang.mvi.util

import org.apache.commons.lang3.time.FastDateFormat
import java.util.*


fun Date?.toFormatSafe(formatter: FastDateFormat): String =
        this?.let {
            try {
                formatter.format(this)
            } catch (t: Throwable) {
                ""
            }
        }.orEmpty()