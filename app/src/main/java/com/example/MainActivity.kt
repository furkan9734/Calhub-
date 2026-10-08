package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CalcHubBottomNavigation
import com.example.ui.components.CalcHubHeader
import com.example.ui.screens.*
import com.example.ui.theme.CalcHubTheme
import com.example.ui.theme.ThemeMode
import com.example.viewmodel.CalcHubViewModel
import com.example.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CalcHubViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(uiState.toastMessage) {
                uiState.toastMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearToast()
                }
            }

            CalcHubTheme(themeMode = uiState.themeMode) {
                val isDetailView = uiState.activeCalculatorId != null

                BackHandler(enabled = isDetailView || uiState.currentTab != NavigationTab.HOME) {
                    viewModel.navigateBack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        if (!isDetailView) {
                            CalcHubHeader(
                                currentTheme = uiState.themeMode,
                                onThemeToggle = {
                                    val nextTheme = when (uiState.themeMode) {
                                        ThemeMode.SYSTEM -> ThemeMode.LIGHT
                                        ThemeMode.LIGHT -> ThemeMode.DARK
                                        ThemeMode.DARK -> ThemeMode.SYSTEM
                                    }
                                    viewModel.setThemeMode(nextTheme)
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (!isDetailView) {
                            CalcHubBottomNavigation(
                                selectedTab = uiState.currentTab,
                                onTabSelected = { viewModel.selectTab(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = isDetailView to uiState.currentTab, label = "screen_transition") { (onDetail, tab) ->
                            if (onDetail && uiState.activeCalculatorId != null) {
                                CalculatorDetailScreen(
                                    calculatorId = uiState.activeCalculatorId!!,
                                    onBack = { viewModel.navigateBack() },
                                    isFavorite = viewModel.isFavorite(uiState.activeCalculatorId!!),
                                    onToggleFavorite = { viewModel.toggleFavorite(uiState.activeCalculatorId!!) },
                                    onOpenCalculator = { viewModel.openCalculator(it) },
                                    onSaveHistory = { calcId, name, expr, res ->
                                        viewModel.addHistory(calcId, name, expr, res)
                                    }
                                )
                            } else {
                                when (tab) {
                                    NavigationTab.HOME -> {
                                        HomeScreen(
                                            searchQuery = uiState.searchQuery,
                                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                            onOpenCalculator = { viewModel.openCalculator(it) },
                                            onNavigateToCategory = { cat ->
                                                viewModel.setSelectedCategory(cat)
                                                viewModel.selectTab(NavigationTab.TOOLS)
                                            },
                                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                                            isFavorite = { viewModel.isFavorite(it) },
                                            onSaveHistory = { calcId, name, expr, res ->
                                                viewModel.addHistory(calcId, name, expr, res)
                                            }
                                        )
                                    }
                                    NavigationTab.TOOLS -> {
                                        ToolsScreen(
                                            selectedCategory = uiState.selectedCategory,
                                            onCategorySelected = { viewModel.setSelectedCategory(it) },
                                            onOpenCalculator = { viewModel.openCalculator(it) },
                                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                                            isFavorite = { viewModel.isFavorite(it) }
                                        )
                                    }
                                    NavigationTab.FAVORITES -> {
                                        FavoritesScreen(
                                            favorites = uiState.favorites,
                                            onOpenCalculator = { viewModel.openCalculator(it) },
                                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                                        )
                                    }
                                    NavigationTab.HISTORY -> {
                                        HistoryScreen(
                                            historyList = uiState.history,
                                            onOpenCalculator = { viewModel.openCalculator(it) },
                                            onDeleteItem = { viewModel.deleteHistoryItem(it) },
                                            onClearAll = { viewModel.clearAllHistory() }
                                        )
                                    }
                                    NavigationTab.SETTINGS -> {
                                        SettingsScreen(
                                            themeMode = uiState.themeMode,
                                            onThemeModeChange = { viewModel.setThemeMode(it) },
                                            decimalPrecision = uiState.decimalPrecision,
                                            onDecimalPrecisionChange = { viewModel.setDecimalPrecision(it) },
                                            autoCopyResult = uiState.autoCopyResult,
                                            onAutoCopyResultChange = { viewModel.setAutoCopyResult(it) },
                                            hapticFeedback = uiState.hapticFeedback,
                                            onHapticFeedbackChange = { viewModel.setHapticFeedback(it) },
                                            onClearHistory = { viewModel.clearAllHistory() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
