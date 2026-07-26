package com.example.aplicaciondisney

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform