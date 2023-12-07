package com.shahroze.shared.data.source.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: Error,
    val success: Boolean
)