package com.example.contador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Colores del Olimpo
val MarbleWhite = Color(0xFFFDFDFD)
val AncientGold = Color(0xFFD4AF37)
val BronzeText = Color(0xFF3E2723)
val HeraPurple = Color(0xFF4A148C)

@Composable
fun App(lifecycleLogs: List<String> = emptyList()) {
    var count by rememberSaveable { mutableStateOf(0) }
    var showHeraMessage by rememberSaveable { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = AncientGold,
            onPrimary = MarbleWhite,
            background = MarbleWhite,
            surface = MarbleWhite,
            onSurface = BronzeText
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MarbleWhite
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isLandscape = maxWidth > maxHeight

                if (isLandscape) {
                    LandscapeLayout(
                        count = count,
                        onIncrement = { 
                            count++
                            showHeraMessage = false
                        },
                        onReset = {
                            count = 0
                            showHeraMessage = true
                        },
                        showHeraMessage = showHeraMessage,
                        lifecycleLogs = lifecycleLogs
                    )
                } else {
                    PortraitLayout(
                        count = count,
                        onIncrement = { 
                            count++
                            showHeraMessage = false
                        },
                        onReset = {
                            count = 0
                            showHeraMessage = true
                        },
                        showHeraMessage = showHeraMessage,
                        lifecycleLogs = lifecycleLogs
                    )
                }
            }
        }
    }
}

@Composable
fun PortraitLayout(
    count: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    showHeraMessage: Boolean,
    lifecycleLogs: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(24.dp))
        CounterSection(count, showHeraMessage)
        Spacer(modifier = Modifier.height(24.dp))
        ActionButtons(onIncrement, onReset)
        Spacer(modifier = Modifier.weight(1f))
        LogsConsole(lifecycleLogs, modifier = Modifier.height(150.dp))
    }
}

@Composable
fun LandscapeLayout(
    count: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    showHeraMessage: Boolean,
    lifecycleLogs: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            ActionButtons(onIncrement, onReset)
        }
        
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CounterSection(count, showHeraMessage)
            Spacer(modifier = Modifier.height(8.dp))
            LogsConsole(lifecycleLogs, modifier = Modifier.fillMaxHeight(0.8f))
        }
    }
}

@Composable
fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "EL REGISTRO DEL OLIMPO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = AncientGold,
            letterSpacing = 2.sp
        )
        Text(
            text = "¿Cuántos hijos tiene Zeus?",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BronzeText,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CounterSection(count: Int, showHeraMessage: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .border(3.dp, AncientGold, RoundedCornerShape(70.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = count.toString(),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = AncientGold
                )
                Text(
                    text = if (count == 1) "SEMIDIÓS" else "SEMIDIOSES",
                    fontSize = 10.sp,
                    color = BronzeText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        if (showHeraMessage) {
            Text(
                text = "Hera los mató a todos 💀",
                color = HeraPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ActionButtons(onIncrement: () -> Unit, onReset: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onIncrement,
            colors = ButtonDefaults.buttonColors(containerColor = AncientGold),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("+1 HIJO", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = onReset,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = HeraPurple),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("IRA DE HERA (RESET)", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LogsConsole(logs: List<String>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "CONSOLA DE EVENTOS (LOGS)",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BronzeText.copy(alpha = 0.5f)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(logs) { log ->
                Text(
                    text = log,
                    fontSize = 10.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = Color.DarkGray
                )
            }
        }
    }
}
