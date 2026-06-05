package com.epn.mockcrud

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform