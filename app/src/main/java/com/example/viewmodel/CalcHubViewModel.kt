package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CalculationHistoryItem
import com.example.model.CalculatorCategory
import com.example.model.CalculatorItem
import com.example.model.CalculatorRegistry
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

enum class NavigationTab {
    HOME,
    TOOLS,
    FAVORITES,
    HISTORY,
    SETTINGS
}

data class CalcHubUiState(
    val currentTab: NavigationTab = NavigationTab.HOME,
    val activeCalculatorId: String? = null,
    val navigationStack: List<Pair<NavigationTab, String?>> = listOf(Pair(NavigationTab.HOME, null)),
    val searchQuery: String = "",
    val selectedCategory: CalculatorCategory = CalculatorCategory.ALL,
    val favorites: Set<String> = emptySet(),
    val history: List<CalculationHistoryItem> = emptyList(),
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val decimalPrecision: Int = 2,
    val autoCopyResult: Boolean = false,
    val hapticFeedback: Boolean = true,
    val toastMessage: String? = null
)

class CalcHubViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs: SharedPreferences =
        application.getSharedPreferences("calchub_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(CalcHubUiState())
    val uiState: StateFlow<CalcHubUiState> = _uiState.asStateFlow()

    init {
        loadPersistedState()
    }

    private fun loadPersistedState() {
        val favs = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
        val themeStr = prefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        val theme = try { ThemeMode.valueOf(themeStr) } catch (_: Exception) { ThemeMode.SYSTEM }
        val precision = prefs.getInt("decimal_precision", 2)
        val autoCopy = prefs.getBoolean("auto_copy", false)
        val haptic = prefs.getBoolean("haptic", true)
        val historyJson = prefs.getString("history_json", "[]") ?: "[]"
        val historyList = parseHistoryJson(historyJson)

        _uiState.update {
            it.copy(
                favorites = favs,
                themeMode = theme,
                decimalPrecision = precision,
                autoCopyResult = autoCopy,
                hapticFeedback = haptic,
                history = historyList
            )
        }
    }

    private fun parseHistoryJson(json: String): List<CalculationHistoryItem> {
        val list = mutableListOf<CalculationHistoryItem>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    CalculationHistoryItem(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        calculatorId = obj.optString("calcId", ""),
                        calculatorName = obj.optString("calcName", ""),
                        expression = obj.optString("expr", ""),
                        result = obj.optString("result", ""),
                        timestamp = obj.optLong("time", System.currentTimeMillis()),
                        details = obj.optString("details", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    private fun persistHistory(history: List<CalculationHistoryItem>) {
        val array = JSONArray()
        history.take(100).forEach { item ->
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("calcId", item.calculatorId)
            obj.put("calcName", item.calculatorName)
            obj.put("expr", item.expression)
            obj.put("result", item.result)
            obj.put("time", item.timestamp)
            obj.put("details", item.details)
            array.put(obj)
        }
        prefs.edit().putString("history_json", array.toString()).apply()
    }

    fun selectTab(tab: NavigationTab) {
        _uiState.update { current ->
            current.copy(
                currentTab = tab,
                activeCalculatorId = null,
                navigationStack = listOf(Pair(tab, null))
            )
        }
    }

    fun openCalculator(calcId: String) {
        _uiState.update { current ->
            val nextStack = current.navigationStack + Pair(current.currentTab, calcId)
            current.copy(
                activeCalculatorId = calcId,
                navigationStack = nextStack
            )
        }
    }

    fun navigateBack(): Boolean {
        val stack = _uiState.value.navigationStack
        if (_uiState.value.activeCalculatorId != null) {
            _uiState.update { it.copy(activeCalculatorId = null) }
            return true
        }
        if (stack.size > 1) {
            val popped = stack.dropLast(1)
            val top = popped.last()
            _uiState.update {
                it.copy(
                    currentTab = top.first,
                    activeCalculatorId = top.second,
                    navigationStack = popped
                )
            }
            return true
        } else if (_uiState.value.currentTab != NavigationTab.HOME) {
            selectTab(NavigationTab.HOME)
            return true
        }
        return false
    }

    fun toggleFavorite(calcId: String) {
        val currentFavs = _uiState.value.favorites.toMutableSet()
        if (currentFavs.contains(calcId)) {
            currentFavs.remove(calcId)
            showToast("Removed from favorites")
        } else {
            currentFavs.add(calcId)
            showToast("Saved to favorites")
        }
        prefs.edit().putStringSet("favorites", currentFavs).apply()
        _uiState.update { it.copy(favorites = currentFavs) }
    }

    fun isFavorite(calcId: String): Boolean {
        return _uiState.value.favorites.contains(calcId)
    }

    fun addHistory(calcId: String, calcName: String, expression: String, result: String, details: String = "") {
        val newItem = CalculationHistoryItem(
            id = UUID.randomUUID().toString(),
            calculatorId = calcId,
            calculatorName = calcName,
            expression = expression,
            result = result,
            timestamp = System.currentTimeMillis(),
            details = details
        )
        val updatedHistory = listOf(newItem) + _uiState.value.history
        persistHistory(updatedHistory)
        _uiState.update { it.copy(history = updatedHistory) }
    }

    fun deleteHistoryItem(id: String) {
        val updated = _uiState.value.history.filter { it.id != id }
        persistHistory(updated)
        _uiState.update { it.copy(history = updated) }
        showToast("Calculation removed")
    }

    fun clearAllHistory() {
        persistHistory(emptyList())
        _uiState.update { it.copy(history = emptyList()) }
        showToast("History cleared")
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedCategory(category: CalculatorCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun setDecimalPrecision(precision: Int) {
        prefs.edit().putInt("decimal_precision", precision).apply()
        _uiState.update { it.copy(decimalPrecision = precision) }
    }

    fun setAutoCopyResult(enabled: Boolean) {
        prefs.edit().putBoolean("auto_copy", enabled).apply()
        _uiState.update { it.copy(autoCopyResult = enabled) }
    }

    fun setHapticFeedback(enabled: Boolean) {
        prefs.edit().putBoolean("haptic", enabled).apply()
        _uiState.update { it.copy(hapticFeedback = enabled) }
    }

    fun showToast(message: String) {
        _uiState.update { it.copy(toastMessage = message) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
