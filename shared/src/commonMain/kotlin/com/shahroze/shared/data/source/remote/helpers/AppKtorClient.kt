package com.shahroze.shared.data.source.remote.helpers

import io.ktor.client.*
import io.ktor.client.plugins.logging.*

class AppKtorClient {

}

object KtorClientConstants {
    const val baseUrl = "http://api.exchangeratesapi.io"
    val client = HttpClient {
        install(Logging) {
            level = LogLevel.ALL
        }
    }
}
