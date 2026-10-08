package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CalculatorRegistry
import com.example.ui.components.CalculatorCard
import com.example.ui.components.EmptyStateView

@Composable
fun FavoritesScreen(
    favorites: Set<String>,
    onOpenCalculator: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteCalculators = favorites.mapNotNull { CalculatorRegistry.getById(it) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Saved Favorites",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Quickly access your most frequent and starred calculators.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (favoriteCalculators.isEmpty()) {
            item {
                EmptyStateView(
                    icon = Icons.Default.Star,
                    title = "No favorites yet",
                    subtitle = "Tap the star icon on any calculator to pin it here for instant access."
                )
            }
        } else {
            items(favoriteCalculators) { calc ->
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                    CalculatorCard(
                        calculator = calc,
                        isFavorite = true,
                        onFavoriteToggle = { onToggleFavorite(calc.id) },
                        onClick = { onOpenCalculator(calc.id) }
                    )
                }
            }
        }
    }
}
