package com.chenzhang.mvi.data

data class Recording(
        val id: String,
        val title: String,
        val recordingType: RecordingType,
        val description: String? = null,
        val recordingTime: RecordingTime? = null
)

enum class RecordingType {MOVIE, TV, MV, SPORT}
