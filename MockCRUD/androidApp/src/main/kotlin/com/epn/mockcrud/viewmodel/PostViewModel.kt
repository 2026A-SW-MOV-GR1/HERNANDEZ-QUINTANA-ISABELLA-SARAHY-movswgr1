package com.epn.mockcrud.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epn.mockcrud.model.Post
import com.epn.mockcrud.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val repo = PostRepository()

    var post by mutableStateOf<Post?>(null)
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf("")

    fun fetchPost(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                post = repo.getPost(id)
                message = "Cargado correctamente"
            } catch (e: Exception) {
                message = "Error: ${e.message}"
            }
            isLoading = false
        }
    }

    fun updatePost(id: Int, title: String, body: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val updated = repo.updatePost(id, Post(id, title, body))
                post = updated
                message = "✅ Actualizado correctamente (200 OK)"
            } catch (e: Exception) {
                message = "Error: ${e.message}"
            }
            isLoading = false
        }
    }
}
