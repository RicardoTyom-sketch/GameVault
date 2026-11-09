package com.gamevault.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gamevault.mobile.presentation.navigation.GameVaultNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFFA9B8FF),
                secondary = Color(0xFF6BD8BD),
                background = Color(0xFF121726),
                surface = Color(0xFF202638)
            )) {
                Surface(modifier = Modifier.fillMaxSize()) { GameVaultNavigation() }
            }
        }
    }
}
