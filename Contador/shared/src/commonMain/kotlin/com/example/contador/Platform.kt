package com.example.contador

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform