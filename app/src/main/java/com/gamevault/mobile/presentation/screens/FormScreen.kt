package com.gamevault.mobile.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gamevault.mobile.presentation.viewmodels.GamesUiState
import com.gamevault.mobile.presentation.viewmodels.GamesViewModel

@Composable
fun FormScreen(id: Int?, state: GamesUiState, vm: GamesViewModel,
               onBack: () -> Unit, onSaved: () -> Unit) {
    LaunchedEffect(id) { vm.form(id) }
    LaunchedEffect(state.saved) {
        if (state.saved) { vm.clearSaved(); onSaved() }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (id == null) "Agregar videojuego" else "Editar videojuego", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(state.title, vm::title, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.genre, vm::genre, label = { Text("Género") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.platform, vm::platform, label = { Text("Plataforma") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.developer, vm::developer, label = { Text("Desarrollador") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.rating, vm::rating, label = { Text("Calificación (0 a 10)") }, modifier = Modifier.fillMaxWidth())
        Row { Checkbox(checked = state.completed, onCheckedChange = vm::completed); Text("Ya lo terminé") }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (state.saving) CircularProgressIndicator()
        Button(onClick = vm::save, enabled = !state.saving, modifier = Modifier.fillMaxWidth()) { Text("Guardar") }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
    }
}
