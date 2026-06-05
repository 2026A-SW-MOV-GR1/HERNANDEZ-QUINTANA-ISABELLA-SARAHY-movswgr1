package com.epn.mockcrud.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    bookId: Long?, 
    onNavigateBack: () -> Unit,
    viewModel: com.epn.mockcrud.viewmodel.ItemViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val isEdit = bookId != null
    
    val sqlItems by viewModel.sqlItems.collectAsState()
    val noSqlItems = viewModel.noSqlItems
    
    val existingItem = if (isEdit) {
        if (viewModel.useSQL) sqlItems.find { it.id.toLong() == bookId }
        else noSqlItems.find { it.id == bookId }
    } else null

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var epubUri by remember { mutableStateOf<Uri?>(null) }
    var status by remember { mutableStateOf("Pendiente") }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = it
            } catch (e: Exception) {
                imageUri = it
            }
        }
    }

    val epubLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                epubUri = it
            } catch (e: Exception) {
                epubUri = it
            }
        }
    }

    LaunchedEffect(existingItem) {
        existingItem?.let {
            title = if (it is com.epn.mockcrud.db.ItemEntity) it.name else (it as com.epn.mockcrud.db.ItemNoSQL).name
            author = if (it is com.epn.mockcrud.db.ItemEntity) it.author else (it as com.epn.mockcrud.db.ItemNoSQL).author
            description = if (it is com.epn.mockcrud.db.ItemEntity) it.description else (it as com.epn.mockcrud.db.ItemNoSQL).description
            val uriStr = if (it is com.epn.mockcrud.db.ItemEntity) it.imageUrl else (it as com.epn.mockcrud.db.ItemNoSQL).imageUrl
            imageUri = uriStr?.let { Uri.parse(it) }
            val epubStr = if (it is com.epn.mockcrud.db.ItemEntity) it.epubUri else (it as com.epn.mockcrud.db.ItemNoSQL).epubUri
            epubUri = epubStr?.let { Uri.parse(it) }
            status = if (it is com.epn.mockcrud.db.ItemEntity) it.status else (it as com.epn.mockcrud.db.ItemNoSQL).status
            startDate = if (it is com.epn.mockcrud.db.ItemEntity) it.startDate else (it as com.epn.mockcrud.db.ItemNoSQL).startDate
            endDate = if (it is com.epn.mockcrud.db.ItemEntity) it.endDate else (it as com.epn.mockcrud.db.ItemNoSQL).endDate
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isEdit) "Editar Libro" else "Nuevo Libro", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Selector de Imagen
            Surface(
                modifier = Modifier
                    .size(160.dp, 220.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { imageLauncher.launch(arrayOf("image/*")) },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                            Text("Añadir Portada", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            Text("Detalles del Libro", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            CustomTextField(
                value = title,
                onValueChange = { title = it },
                label = "Título",
                icon = Icons.Default.Title
            )
            
            CustomTextField(
                value = author,
                onValueChange = { author = it },
                label = "Autor",
                icon = Icons.Default.Person
            )

            // Estado del Libro (Selector)
            StatusSelector(
                currentStatus = status,
                onStatusSelected = { status = it }
            )

            // Selector de ePub
            OutlinedCard(
                onClick = { epubLauncher.launch(arrayOf("application/epub+zip")) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = if (epubUri != null) "Archivo ePub Cargado" else "Cargar Archivo .epub",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.weight(1f))
                    if (epubUri != null) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
            }

            // Selectores de Fecha
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    label = "Inicio",
                    date = startDate,
                    onDateSelected = { startDate = it },
                    modifier = Modifier.weight(1f)
                )
                DatePickerField(
                    label = "Fin",
                    date = endDate,
                    onDateSelected = { endDate = it },
                    modifier = Modifier.weight(1f)
                )
            }

            CustomTextField(
                value = description,
                onValueChange = { description = it },
                label = "Descripción",
                icon = Icons.Default.Description,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (isEdit) {
                        viewModel.updateItem(
                            id = bookId!!,
                            name = title,
                            author = author,
                            description = description,
                            imageUrl = imageUri?.toString(),
                            startDate = startDate,
                            endDate = endDate,
                            status = status,
                            epubUri = epubUri?.toString()
                        )
                        Toast.makeText(context, "Libro actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addItem(
                            name = title,
                            author = author,
                            description = description,
                            imageUrl = imageUri?.toString(),
                            startDate = startDate,
                            endDate = endDate,
                            status = status,
                            epubUri = epubUri?.toString()
                        )
                        Toast.makeText(context, "Libro guardado", Toast.LENGTH_SHORT).show()
                    }
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(if (isEdit) "Actualizar" else "Guardar Libro", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatusSelector(currentStatus: String, onStatusSelected: (String) -> Unit) {
    val statuses = listOf("Pendiente", "Leyendo", "Completado", "Abandonado")
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Estado", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            statuses.forEach { status ->
                val isSelected = status == currentStatus
                FilterChip(
                    selected = isSelected,
                    onClick = { onStatusSelected(status) },
                    label = { Text(status, fontSize = 10.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(label: String, date: Long?, onDateSelected: (Long) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date ?: System.currentTimeMillis())

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDialog = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedCard(
        onClick = { showDialog = true },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Text(
                text = if (date != null) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(date)) else "Seleccionar",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}
