package com.chenzhang.mvi.datamodel

import java.util.*

data class Recording(
        val id: String,
        val title: String,
        val recordingType: RecordingType,
        val description: String? = null,
        val recordingTime: Date? = null
)

enum class RecordingType {MOVIE, TV, MV, SPORT}
