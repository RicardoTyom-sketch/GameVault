package com.gamevault.mobile.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gamevault.mobile.presentation.viewmodels.GamesUiState
import com.gamevault.mobile.presentation.viewmodels.GamesViewModel

@Composable
fun DetailScreen(id: Int, state: GamesUiState, userId: Int?, vm: GamesViewModel,
                 onBack: () -> Unit, onEdit: () -> Unit) {
    LaunchedEffect(id) { vm.detail(id) }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Detalle", style = MaterialTheme.typography.headlineMedium)
        if (state.loading) CircularProgressIndicator()
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        val game = state.selected?.takeIf { it.id == id }
        if (game != null) {
            Text("Título: ${game.title}")
            Text("Género: ${game.genre}")
            Text("Plataforma: ${game.platform}")
            Text("Desarrollador: ${game.developer}")
            Text("Calificación: ${game.rating} / 10")
            Text("Completado: ${if (game.completed) "Sí" else "No"}")
            Text("ID: ${game.id}")
            if (game.ownerId == userId) Button(onClick = onEdit) { Text("Editar") }
        }
        OutlinedButton(onClick = onBack) { Text("Volver") }
    }
}
