package com.epn.mockcrud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.epn.mockcrud.ui.AestheticTheme
import com.epn.mockcrud.ui.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AestheticTheme {
                NavGraph()
            }
        }
    }
}