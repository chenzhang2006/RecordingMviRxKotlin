package com.chenzhang.mvi.data

data class Recording(
        val id: String,
        val title: String,
        val description: String?,
        val inProgress: Boolean = false
)
