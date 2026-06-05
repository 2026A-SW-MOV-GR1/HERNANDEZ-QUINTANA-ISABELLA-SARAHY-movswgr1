package com.epn.mockcrud.repository

import com.epn.mockcrud.model.Post
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class PostRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getPost(id: Int): Post =
        client.get("https://jsonplaceholder.typicode.com/posts/$id").body()

    suspend fun updatePost(id: Int, post: Post): Post =
        client.put("https://jsonplaceholder.typicode.com/posts/$id") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }.body()
}
