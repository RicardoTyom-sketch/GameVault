package com.gamevault.mobile.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gamevault.mobile.GameVaultApplication
import com.gamevault.mobile.presentation.screens.AuthScreen
import com.gamevault.mobile.presentation.screens.DetailScreen
import com.gamevault.mobile.presentation.screens.FormScreen
import com.gamevault.mobile.presentation.screens.HomeScreen
import com.gamevault.mobile.presentation.viewmodels.AuthViewModel
import com.gamevault.mobile.presentation.viewmodels.GamesViewModel
import com.gamevault.mobile.presentation.viewmodels.authViewModelFactory
import com.gamevault.mobile.presentation.viewmodels.gamesViewModelFactory

@Composable
fun GameVaultNavigation() {
    val app = LocalContext.current.applicationContext as GameVaultApplication
    val container = app.container
    val authFactory = remember(container) { authViewModelFactory(container) }
    val gamesFactory = remember(container) { gamesViewModelFactory(container) }
    val nav = rememberNavController()
    val auth: AuthViewModel = viewModel(factory = authFactory)
    val games: GamesViewModel = viewModel(factory = gamesFactory)
    val authState by auth.ui.collectAsStateWithLifecycle()
    val gameState by games.ui.collectAsStateWithLifecycle()

    // A login/logout clears the previous stack, including the Back button history.
    LaunchedEffect(authState.booting, authState.user?.id) {
        if (!authState.booting) {
            val destination = if (authState.user == null) "login" else "home"
            nav.navigate(destination) {
                popUpTo(nav.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = nav, startDestination = "gate") {
        composable("gate") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        composable("login") {
            AuthScreen(false, authState, auth, onSwitch = { nav.navigate("register") })
        }
        composable("register") {
            AuthScreen(true, authState, auth, onSwitch = { nav.popBackStack() })
        }
        composable("home") {
            HomeScreen(
                state = gameState,
                userId = authState.user?.id,
                vm = games,
                onAdd = { nav.navigate("form/new") },
                onOpen = { nav.navigate("detail/$it") },
                onEdit = { nav.navigate("form/$it") },
                onLogout = auth::logout
            )
        }
        composable("detail/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            DetailScreen(
                id = id,
                state = gameState,
                userId = authState.user?.id,
                vm = games,
                onBack = { nav.popBackStack() },
                onEdit = { nav.navigate("form/$id") }
            )
        }
        composable("form/{id}") { entry ->
            val id = entry.arguments?.getString("id")?.toIntOrNull()
            FormScreen(
                id = id,
                state = gameState,
                vm = games,
                onBack = { nav.popBackStack() },
                onSaved = {
                    nav.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
