package com.app.mvvm.model.request

data class EventRequest(
    val `data`: Data
) {
    data class Data(
        val deviceId: String,
        val deviceType: String,
        val filterType: String,
        val langType: String,
        val limit: String,
        val page: String,
        val search: String,
        val token: String
    )
}