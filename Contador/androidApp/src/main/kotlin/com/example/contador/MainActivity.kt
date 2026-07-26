package com.example.contador

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    private val TAG = "CicloDeVida"
    private val logs = mutableStateListOf<String>()

    private fun addLog(message: String) {
        val timestamp = android.text.format.DateFormat.format("HH:mm:ss", java.util.Calendar.getInstance()).toString()
        val fullMessage = "[$timestamp] $message"
        Log.d(TAG, fullMessage)
        logs.add(fullMessage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        addLog("onCreate: Creada")
        
        if (savedInstanceState != null) {
            addLog("onCreate: Restaurada")
        }

        setContent {
            App(lifecycleLogs = logs)
        }
    }

    override fun onStart() {
        super.onStart()
        addLog("onStart: Visible")
    }

    override fun onResume() {
        super.onResume()
        addLog("onResume: Interactuable")
    }

    override fun onPause() {
        super.onPause()
        addLog("onPause: Perdiendo foco")
    }

    override fun onStop() {
        super.onStop()
        addLog("onStop: Invisible")
    }

    override fun onDestroy() {
        super.onDestroy()
        addLog("onDestroy: Destruida")
    }

    override fun onRestart() {
        super.onRestart()
        addLog("onRestart: Reiniciando")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        addLog("onSaveInstanceState: Guardando")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        addLog("onRestoreInstanceState: Restaurando")
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
