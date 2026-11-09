package com.gamevault.mobile.presentation.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.mobile.data.model.User
import com.gamevault.mobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val booting: Boolean = true,
    val loading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val user = try { repository.currentUser() } catch (_: Exception) { null }
            _ui.update { it.copy(booting = false, user = user) }
        }
    }

    fun name(value: String) = _ui.update { it.copy(name = value, error = null) }
    fun email(value: String) = _ui.update { it.copy(email = value, error = null) }
    fun password(value: String) = _ui.update { it.copy(password = value, error = null) }
    fun confirm(value: String) = _ui.update { it.copy(confirm = value, error = null) }
    fun logout() {
        repository.logout()
        _ui.value = AuthUiState(booting = false)
    }

    fun submit(isRegister: Boolean) {
        val current = _ui.value
        if (current.loading) return
        val error = when {
            isRegister && current.name.isBlank() -> "Escribe tu nombre."
            !Patterns.EMAIL_ADDRESS.matcher(current.email.trim()).matches() -> "Correo no válido."
            current.password.length < 8 -> "La contraseña necesita 8 caracteres como mínimo."
            isRegister && current.password != current.confirm -> "Las contraseñas no coinciden."
            else -> null
        }
        if (error != null) { _ui.update { it.copy(error = error) }; return }
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }
            try {
                val user = if (isRegister) repository.registerAndLogin(
                    current.name.trim(), current.email.trim(), current.password
                ) else repository.login(current.email.trim(), current.password)
                _ui.update { it.copy(loading = false, user = user, password = "", confirm = "") }
            } catch (error: Exception) {
                _ui.update { it.copy(loading = false, error = error.message ?: "Error de conexión") }
            }
        }
    }
}
