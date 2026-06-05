package com.epn.mockcrud.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.epn.mockcrud.db.ItemEntity
import com.epn.mockcrud.db.ItemNoSQL
import java.util.*
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToPosts: () -> Unit,
    onNavigateToSecrets: () -> Unit,
    viewModel: com.epn.mockcrud.viewmodel.ItemViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val sqlItems by viewModel.sqlItems.collectAsState()
    val noSqlItems = viewModel.noSqlItems
    val globalLogs by viewModel.globalReadingLogs.collectAsState()
    
    val currentItems = if (viewModel.useSQL) sqlItems else noSqlItems
    
    var itemToDeleteSql by remember { mutableStateOf<ItemEntity?>(null) }
    var itemToDeleteNoSql by remember { mutableStateOf<ItemNoSQL?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("Mi Biblioteca", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                },
                navigationIcon = {
                    Row {
                        IconButton(onClick = onNavigateToSecrets) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Secretos",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = onNavigateToPosts) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Red",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = if (viewModel.useSQL) Icons.Default.Storage else Icons.Default.Cloud,
                            contentDescription = null,
                            tint = if (viewModel.useSQL) MaterialTheme.colorScheme.primary else Color(0xFFE91E63),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = !viewModel.useSQL,
                            onCheckedChange = { viewModel.useSQL = !it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFFE91E63),
                                checkedTrackColor = Color(0xFFE91E63).copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo")
            }
        },
        bottomBar = {
            ReadingCalendar(
                selectedDays = globalLogs,
                onToggleDate = { viewModel.toggleReadingDate(it) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Surface(
                color = if (viewModel.useSQL) MaterialTheme.colorScheme.primaryContainer 
                        else Color(0xFFFCE4EC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (viewModel.useSQL) "MODO: SQLite (Relacional)" 
                               else "MODO: ObjectBox (No-Relacional)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (viewModel.useSQL) MaterialTheme.colorScheme.primary 
                                else Color(0xFFC2185B)
                    )
                }
            }

            if (currentItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay libros en ${if (viewModel.useSQL) "SQLite" else "NoSQL"}", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(currentItems) { item ->
                        val id = if (item is ItemEntity) item.id.toLong() else (item as ItemNoSQL).id
                        val name = if (item is ItemEntity) item.name else (item as ItemNoSQL).name
                        val author = if (item is ItemEntity) item.author else (item as ItemNoSQL).author
                        val status = if (item is ItemEntity) item.status else (item as ItemNoSQL).status
                        val imageUrl = if (item is ItemEntity) item.imageUrl else (item as ItemNoSQL).imageUrl
                        val hasEpub = if (item is ItemEntity) item.epubUri != null else (item as ItemNoSQL).epubUri != null
                        
                        ItemCard(
                            name = name,
                            author = author,
                            status = status,
                            imageUrl = imageUrl,
                            hasEpub = hasEpub,
                            source = if (viewModel.useSQL) "SQL" else "NoSQL",
                            onClick = { onNavigateToEdit(id) },
                            onDelete = {
                                if (viewModel.useSQL) itemToDeleteSql = item as ItemEntity
                                else itemToDeleteNoSql = item as ItemNoSQL
                            }
                        )
                    }
                }
            }
        }
    }

    if (itemToDeleteSql != null || itemToDeleteNoSql != null) {
        AlertDialog(
            onDismissRequest = { itemToDeleteSql = null; itemToDeleteNoSql = null },
            title = { Text("¿Eliminar libro?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteItem(itemToDeleteSql, itemToDeleteNoSql)
                    itemToDeleteSql = null; itemToDeleteNoSql = null
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { itemToDeleteSql = null; itemToDeleteNoSql = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ReadingCalendar(selectedDays: Set<String>, onToggleDate: (String) -> Unit) {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val days = remember {
        val list = mutableListOf<Pair<String, String>>()
        val tempCal = Calendar.getInstance()
        tempCal.add(Calendar.DAY_OF_YEAR, -6)
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        for (i in 0..6) {
            list.add(dayFormat.format(tempCal.time) to dateFormat.format(tempCal.time))
            tempCal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Tu Racha de Lectura", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                days.forEach { (dayName, dateStr) ->
                    val isSelected = selectedDays.contains(dateStr)
                    val isToday = dateStr == today
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onToggleDate(dateStr) }
                    ) {
                        Text(dayName.take(1).uppercase(), style = MaterialTheme.typography.labelSmall, color = if(isToday) MaterialTheme.colorScheme.primary else Color.Gray)
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary 
                                    else if (isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                    else Color.Transparent
                                )
                                .border(1.dp, if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp), tint = Color.White)
                            else Text(dateStr.split("-").last(), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    name: String, 
    author: String,
    status: String,
    imageUrl: String?, 
    hasEpub: Boolean,
    source: String,
    onClick: () -> Unit, 
    onDelete: () -> Unit
) {
    val statusColor = when(status) {
        "Leyendo" -> Color(0xFF4CAF50)
        "Completado" -> Color(0xFF2196F3)
        "Abandonado" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(70.dp, 100.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray)) {
                val painter = rememberAsyncImagePainter(imageUrl)
                Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    Surface(
                        color = if (source == "SQL") MaterialTheme.colorScheme.primary else Color(0xFFE91E63),
                        shape = CircleShape
                    ) {
                        Text(
                            text = source,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(author, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                        Text(status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    if (hasEpub) {
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.3f))
            }
        }
    }
}
