package com.gamevault.mobile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.mobile.data.model.Game
import com.gamevault.mobile.data.model.GameInput
import com.gamevault.mobile.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GamesUiState(
    val items: List<Game> = emptyList(),
    val selected: Game? = null,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val confirmDeleteId: Int? = null,
    val editingId: Int? = null,
    val title: String = "",
    val genre: String = "",
    val platform: String = "",
    val developer: String = "",
    val rating: String = "",
    val completed: Boolean = false
)

class GamesViewModel(private val repository: GameRepository) : ViewModel() {
    private val _ui = MutableStateFlow(GamesUiState())
    val ui: StateFlow<GamesUiState> = _ui.asStateFlow()
    fun load() {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }
            try {
                val games = repository.getGames()
                _ui.update { it.copy(items = games, loading = false) }
            } catch (e: Exception) {
                _ui.update { it.copy(loading = false, error = e.message ?: "Error de red") }
            }
        }
    }
    fun detail(id: Int) {
        viewModelScope.launch {
            _ui.update { it.copy(selected = null, loading = true, error = null) }
            try {
                val game = repository.getGame(id)
                _ui.update { it.copy(selected = game, loading = false) }
            } catch (e: Exception) {
                _ui.update { it.copy(loading = false, error = e.message ?: "No se encontró el juego") }
            }
        }
    }
    fun form(id: Int?) {
        val g = id?.let { key -> _ui.value.items.firstOrNull { it.id == key } ?: _ui.value.selected?.takeIf { it.id == key } }
        _ui.update {
            it.copy(editingId = id, title = g?.title ?: "", genre = g?.genre ?: "",
                platform = g?.platform ?: "", developer = g?.developer ?: "",
                rating = g?.rating?.toString() ?: "", completed = g?.completed ?: false,
                error = null, saved = false)
        }
    }
    fun title(v: String) = _ui.update { it.copy(title = v, error = null) }
    fun genre(v: String) = _ui.update { it.copy(genre = v, error = null) }
    fun platform(v: String) = _ui.update { it.copy(platform = v, error = null) }
    fun developer(v: String) = _ui.update { it.copy(developer = v, error = null) }
    fun rating(v: String) = _ui.update { it.copy(rating = v, error = null) }
    fun completed(v: Boolean) = _ui.update { it.copy(completed = v) }
    fun clearSaved() = _ui.update { it.copy(saved = false) }
    fun askDelete(id: Int) = _ui.update { it.copy(confirmDeleteId = id) }
    fun cancelDelete() = _ui.update { it.copy(confirmDeleteId = null) }
    fun deleteConfirmed() {
        val id = _ui.value.confirmDeleteId ?: return
        viewModelScope.launch {
            _ui.update { it.copy(confirmDeleteId = null, loading = true, error = null) }
            try {
                repository.deleteGame(id)
                val list = repository.getGames()
                _ui.update { it.copy(items = list, loading = false, message = "Videojuego eliminado") }
            } catch (e: Exception) {
                _ui.update { it.copy(loading = false, error = e.message ?: "No se pudo eliminar") }
            }
        }
    }
    fun save() {
        val s = _ui.value
        if (s.saving) return
        val r = s.rating.toDoubleOrNull()
        val error = when {
            s.title.isBlank() -> "El título es obligatorio."
            s.genre.isBlank() -> "El género es obligatorio."
            s.platform.isBlank() -> "La plataforma es obligatoria."
            s.developer.isBlank() -> "El desarrollador es obligatorio."
            r == null || r !in 0.0..10.0 -> "La calificación debe estar entre 0 y 10."
            else -> null
        }
        if (error != null) { _ui.update { it.copy(error = error) }; return }
        viewModelScope.launch {
            _ui.update { it.copy(saving = true, error = null) }
            try {
                val form = GameInput(s.title.trim(), s.genre.trim(), s.platform.trim(), s.developer.trim(), r!!, s.completed)
                if (s.editingId == null) repository.createGame(form) else repository.updateGame(s.editingId, form)
                val list = repository.getGames()
                _ui.update { it.copy(items = list, saving = false, saved = true, message = "Guardado correctamente") }
            } catch (e: Exception) {
                _ui.update { it.copy(saving = false, error = e.message ?: "Error al guardar") }
            }
        }
    }
}
