package com.example.ui.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utils.FormatUtils
import com.example.utils.MathUtils

@Composable
fun BasicCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var previousCalc by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    fun evaluateLive(expr: String) {
        if (expr.isEmpty()) {
            resultText = "0"
            return
        }
        val res = MathUtils.evaluateExpression(expr, MathUtils.AngleMode.DEG)
        res.onSuccess { value ->
            resultText = FormatUtils.formatSmart(value)
        }.onFailure {
            // Keep preview clean or show error if ending
        }
    }

    fun onKeyClick(key: String) {
        copied = false
        when (key) {
            "C" -> {
                expression = ""
                resultText = "0"
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    evaluateLive(expression)
                }
            }
            "=" -> {
                if (expression.isNotEmpty()) {
                    val eval = MathUtils.evaluateExpression(expression, MathUtils.AngleMode.DEG)
                    eval.onSuccess { value ->
                        val formatted = FormatUtils.formatSmart(value)
                        previousCalc = "$expression = $formatted"
                        onSaveHistory(expression, formatted)
                        expression = formatted
                        resultText = formatted
                    }.onFailure {
                        resultText = "Error"
                    }
                }
            }
            "±" -> {
                if (expression.isNotEmpty()) {
                    expression = if (expression.startsWith("-")) {
                        expression.removePrefix("-")
                    } else {
                        "-$expression"
                    }
                    evaluateLive(expression)
                }
            }
            else -> {
                expression += key
                evaluateLive(expression)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("basic_calculator_view")
    ) {
        // Display Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (previousCalc.isNotEmpty()) {
                    Text(
                        text = previousCalc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = if (expression.isEmpty()) "0" else expression,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.testTag("calculator_expression_display")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = resultText,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("calculator_result_display")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(resultText))
                            copied = true
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Result",
                            tint = if (copied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Keypad Grid
        val buttons = listOf(
            listOf("C", "(", ")", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("±", "0", ".", "=")
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { key ->
                        val isOp = key in listOf("÷", "×", "-", "+", "=")
                        val isSpecial = key in listOf("C", "±", "(", ")")
                        val isEquals = key == "="

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    when {
                                        isEquals -> MaterialTheme.colorScheme.primary
                                        isOp -> MaterialTheme.colorScheme.primaryContainer
                                        isSpecial -> MaterialTheme.colorScheme.surfaceVariant
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                )
                                .clickable { onKeyClick(key) }
                                .testTag("calc_key_$key"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = key,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = when {
                                    isEquals -> MaterialTheme.colorScheme.onPrimary
                                    isOp -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }

            // Quick backspace button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onKeyClick("⌫") },
                    modifier = Modifier.testTag("calc_backspace_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Backspace,
                        contentDescription = "Backspace",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete Last")
                }
            }
        }
    }
}

@Composable
fun ScientificCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var angleMode by remember { mutableStateOf(MathUtils.AngleMode.DEG) }
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    fun evaluateLive(expr: String) {
        if (expr.isEmpty()) {
            resultText = "0"
            return
        }
        val res = MathUtils.evaluateExpression(expr, angleMode)
        res.onSuccess { value ->
            resultText = FormatUtils.formatSmart(value)
        }
    }

    fun onKeyClick(key: String) {
        copied = false
        when (key) {
            "C" -> {
                expression = ""
                resultText = "0"
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    evaluateLive(expression)
                }
            }
            "=" -> {
                if (expression.isNotEmpty()) {
                    val eval = MathUtils.evaluateExpression(expression, angleMode)
                    eval.onSuccess { value ->
                        val formatted = FormatUtils.formatSmart(value)
                        onSaveHistory(expression, formatted)
                        expression = formatted
                        resultText = formatted
                    }.onFailure {
                        resultText = "Error"
                    }
                }
            }
            "sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "sqrt" -> {
                expression += "$key("
                evaluateLive(expression)
            }
            "x²" -> {
                expression += "^2"
                evaluateLive(expression)
            }
            "xʸ" -> {
                expression += "^"
                evaluateLive(expression)
            }
            "n!" -> {
                expression += "!"
                evaluateLive(expression)
            }
            "π" -> {
                expression += "π"
                evaluateLive(expression)
            }
            "e" -> {
                expression += "e"
                evaluateLive(expression)
            }
            else -> {
                expression += key
                evaluateLive(expression)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("scientific_calculator_view")
    ) {
        // Display card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = angleMode == MathUtils.AngleMode.DEG,
                        onClick = {
                            angleMode = if (angleMode == MathUtils.AngleMode.DEG) MathUtils.AngleMode.RAD else MathUtils.AngleMode.DEG
                            evaluateLive(expression)
                        },
                        label = { Text(angleMode.name) },
                        modifier = Modifier.testTag("angle_mode_chip")
                    )

                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(resultText))
                            copied = true
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Result",
                            tint = if (copied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (expression.isEmpty()) "0" else expression,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = resultText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scientific Keys in responsive layout
        val sciRows = listOf(
            listOf("sin", "cos", "tan", "sqrt", "C"),
            listOf("asin", "acos", "atan", "x²", "xʸ"),
            listOf("log", "ln", "n!", "π", "e"),
            listOf("7", "8", "9", "÷", "⌫"),
            listOf("4", "5", "6", "×", "("),
            listOf("1", "2", "3", "-", ")"),
            listOf("0", ".", "%", "+", "=")
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sciRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { key ->
                        val isEquals = key == "="
                        val isOp = key in listOf("÷", "×", "-", "+")
                        val isFunc = key in listOf("sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "sqrt", "x²", "xʸ", "n!", "π", "e")

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        isEquals -> MaterialTheme.colorScheme.primary
                                        isOp -> MaterialTheme.colorScheme.primaryContainer
                                        isFunc -> MaterialTheme.colorScheme.surfaceVariant
                                        key == "C" -> MaterialTheme.colorScheme.errorContainer
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                )
                                .clickable { onKeyClick(key) }
                                .testTag("sci_key_$key"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = key,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isEquals || isOp) FontWeight.Bold else FontWeight.Medium,
                                color = when {
                                    isEquals -> MaterialTheme.colorScheme.onPrimary
                                    isOp -> MaterialTheme.colorScheme.onPrimaryContainer
                                    key == "C" -> MaterialTheme.colorScheme.onErrorContainer
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
