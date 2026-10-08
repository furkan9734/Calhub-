package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CalculatorItem
import com.example.model.CalculatorRegistry
import com.example.ui.calculators.*
import com.example.ui.components.CalculatorCard
import com.example.ui.components.FormulaAndExplanationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorDetailScreen(
    calculatorId: String,
    onBack: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenCalculator: (String) -> Unit,
    onSaveHistory: (calcId: String, name: String, expr: String, res: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val calculator = remember(calculatorId) { CalculatorRegistry.getById(calculatorId) }
    val relatedCalculators = remember(calculatorId) { CalculatorRegistry.getRelated(calculatorId, limit = 3) }
    val context = LocalContext.current

    if (calculator == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Calculator not found")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = calculator.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Home > ${calculator.category.title} > ${calculator.name}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.testTag("detail_favorite_button")) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (isFavorite) "Remove Favorite" else "Add Favorite",
                            tint = if (isFavorite) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out ${calculator.name} on CalHub: ${calculator.description}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.testTag("detail_share_button")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share Calculator")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("calculator_detail_scroll"),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description Header Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = calculator.category.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = calculator.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Interactive Calculator Component dispatch
            item {
                when (calculator.id) {
                    "basic" -> BasicCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "scientific" -> ScientificCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "emi", "loan" -> EmiCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "sip" -> SipCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "gst" -> GstCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "discount", "discount_gst" -> DiscountCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "simple_interest" -> SimpleAndCompoundInterestView(isCompound = false, onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "compound_interest" -> SimpleAndCompoundInterestView(isCompound = true, onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "profit_loss" -> ProfitAndLossView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "salary", "tax" -> SalaryCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "bmi" -> BmiCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "bmr", "calorie", "ideal_weight", "body_fat" -> BmrAndCalorieView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "age", "age_days" -> AgeCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "date_diff", "add_date", "time_diff" -> DateDifferenceView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "percentage" -> PercentageCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "tip" -> TipCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "converter" -> UniversalUnitConverterView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "number_system" -> NumberSystemConverterView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "random" -> RandomNumberGeneratorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    "fraction", "ratio", "average", "lcm_gcd", "power_root" -> PercentageCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                    else -> PercentageCalculatorView(onSaveHistory = { expr, res -> onSaveHistory(calculator.id, calculator.name, expr, res) })
                }
            }

            // Formula and Explanation Card
            item {
                FormulaAndExplanationCard(
                    formula = calculator.formula,
                    howItWorks = calculator.howItWorks
                )
            }

            // Related Calculators
            if (relatedCalculators.isNotEmpty()) {
                item {
                    Text(
                        text = "Related Calculators",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(relatedCalculators) { related ->
                    CalculatorCard(
                        calculator = related,
                        isFavorite = false,
                        onFavoriteToggle = {},
                        onClick = { onOpenCalculator(related.id) }
                    )
                }
            }
        }
    }
}
