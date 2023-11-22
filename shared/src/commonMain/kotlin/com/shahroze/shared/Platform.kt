package com.shahroze.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform