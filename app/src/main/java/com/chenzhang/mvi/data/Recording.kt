package com.chenzhang.mvi.data

data class Recording(
        val id: String,
        val title: String,
        val description: String? = null,
        val inProgress: Boolean = false
)
