package com.epn.mockcrud.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.epn.mockcrud.repository.SecretsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { SecretsRepository(context) }
    val scope = rememberCoroutineScope()

    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedMechanism by remember { mutableStateOf("SharedPreferences") }

    val options = listOf("SharedPreferences", "DataStore", "EncryptedSharedPreferences")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Módulo 3: Secretos y Config") },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Configuración Segura", 
                style = MaterialTheme.typography.headlineSmall, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                "Almacena y recupera información sensible usando mecanismos nativos de Android.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            OutlinedTextField(
                value = key,
                onValueChange = { key = it },
                label = { Text("Llave (Key)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Key, null) },
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text("Valor (Value)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Solo necesario para Guardar") },
                shape = RoundedCornerShape(12.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Seleccionar Mecanismo:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    options.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = (selectedMechanism == option),
                                onClick = { selectedMechanism = option }
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            if (key.isBlank() || value.isBlank()) {
                                result = "⚠️ Llave y Valor son requeridos para guardar."
                                return@launch
                            }
                            when (selectedMechanism) {
                                "SharedPreferences" -> repo.saveSharedPref(key, value)
                                "DataStore" -> repo.saveDataStore(key, value)
                                "EncryptedSharedPreferences" -> repo.saveEncrypted(key, value)
                            }
                            result = "✅ Guardado exitosamente en $selectedMechanism"
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar")
                }

                Button(
                    onClick = {
                        scope.launch {
                            if (key.isBlank()) {
                                result = "⚠️ Ingrese la Llave para recuperar."
                                return@launch
                            }
                            val found = when (selectedMechanism) {
                                "SharedPreferences" -> repo.getSharedPref(key)
                                "DataStore" -> repo.getDataStore(key)
                                "EncryptedSharedPreferences" -> repo.getEncrypted(key)
                                else -> null
                            }
                            result = if (found != null) "🔑 Encontrado: $found"
                                     else "❌ Secreto no encontrado en $selectedMechanism"
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Recuperar")
                }
            }

            if (result.isNotEmpty()) {
                Surface(
                    color = if (result.startsWith("✅") || result.startsWith("🔑")) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (result.startsWith("✅") || result.startsWith("🔑")) Color(0xFF4CAF50) else Color(0xFFFF9800)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (result.startsWith("✅") || result.startsWith("🔑")) Color(0xFF2E7D32) else Color(0xFFE65100)
                    )
                }
            }
        }
    }
}
