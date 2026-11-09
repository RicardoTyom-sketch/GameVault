package com.gamevault.mobile.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.gamevault.mobile.presentation.viewmodels.AuthUiState
import com.gamevault.mobile.presentation.viewmodels.AuthViewModel

@Composable
fun AuthScreen(isRegister: Boolean, state: AuthUiState, vm: AuthViewModel, onSwitch: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text("GAMEVAULT", style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.secondary)
        Text(if (isRegister) "Crear cuenta" else "Iniciar sesión",
            style = MaterialTheme.typography.headlineSmall)
        if (isRegister) OutlinedTextField(state.name, vm::name,
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(state.email, vm::email, label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(state.password, vm::password, label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        if (isRegister) OutlinedTextField(state.confirm, vm::confirm,
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Button(onClick = { vm.submit(isRegister) }, enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()) {
            Text(if (isRegister) "Registrarme" else "Entrar")
        }
        TextButton(onClick = onSwitch) {
            Text(if (isRegister) "Ya tengo una cuenta" else "Crear una cuenta")
        }
    }
}
