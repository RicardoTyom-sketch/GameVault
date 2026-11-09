package com.gamevault.mobile.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gamevault.mobile.presentation.viewmodels.GamesUiState
import com.gamevault.mobile.presentation.viewmodels.GamesViewModel

@Composable
fun HomeScreen(
    state: GamesUiState, userId: Int?, vm: GamesViewModel,
    onAdd: () -> Unit, onOpen: (Int) -> Unit, onEdit: (Int) -> Unit, onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { vm.load() }
    state.confirmDeleteId?.let { id ->
        AlertDialog(
            onDismissRequest = vm::cancelDelete,
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que quieres eliminar este videojuego?") },
            confirmButton = { TextButton(onClick = vm::deleteConfirmed) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = vm::cancelDelete) { Text("Cancelar") } }
        )
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("GameVault", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onAdd) { Text("+ Nuevo") }
            OutlinedButton(onClick = vm::load) { Text("Recargar") }
            TextButton(onClick = onLogout) { Text("Salir") }
        }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.secondary) }
        state.error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
            TextButton(onClick = vm::load) { Text("Reintentar") }
        }
        if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        if (!state.loading && state.items.isEmpty()) Text("Lista vacía: agrega tu primer videojuego.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.items, key = { it.id }) { game ->
                Card(Modifier.fillMaxWidth().clickable { onOpen(game.id) }) {
                    Column(Modifier.padding(14.dp)) {
                        Text(game.title, style = MaterialTheme.typography.titleMedium)
                        Text("${game.genre} · ${game.platform} · ${game.rating}/10")
                        Text("Desarrollador: ${game.developer} · ${if (game.completed) "Terminado" else "Pendiente"}")
                        if (game.ownerId == userId) {
                            Row {
                                TextButton(onClick = { onEdit(game.id) }) { Text("Editar") }
                                TextButton(onClick = { vm.askDelete(game.id) }) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
