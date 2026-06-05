package com.epn.mockcrud.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epn.mockcrud.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    onNavigateBack: () -> Unit,
    viewModel: PostViewModel = viewModel()
) {
    var idInput by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.post) {
        viewModel.post?.let {
            title = it.title
            body = it.body
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Módulo 1: Red (JSONPlaceholder)") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Consulta de Post", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = idInput,
                            onValueChange = { idInput = it },
                            label = { Text("Ingresar ID") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            enabled = !viewModel.isLoading,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { viewModel.fetchPost(idInput.toIntOrNull() ?: 1) },
                            enabled = !viewModel.isLoading && idInput.isNotEmpty(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                            else Text("Obtener")
                        }
                    }
                }
            }

            if (viewModel.post != null) {
                ElevatedCard {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Actualización de Datos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Título") },
                            enabled = !viewModel.isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        OutlinedTextField(
                            value = body,
                            onValueChange = { body = it },
                            label = { Text("Cuerpo") },
                            enabled = !viewModel.isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 4,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = { 
                                val id = idInput.toIntOrNull() ?: viewModel.post?.id ?: 0
                                viewModel.updatePost(id, title, body) 
                            },
                            enabled = !viewModel.isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Actualizar (PUT)")
                        }
                    }
                }
            }

            if (viewModel.message.isNotEmpty()) {
                Surface(
                    color = if (viewModel.message.contains("✅")) Color(0xFFE8F5E9) else Color(0xFFFBE9E7),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        viewModel.message, 
                        modifier = Modifier.padding(12.dp),
                        color = if (viewModel.message.contains("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
