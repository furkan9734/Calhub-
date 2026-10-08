package com.example.ui.calculators

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.components.ResultCard
import com.example.utils.ConverterUtils
import com.example.utils.FormatUtils
import com.example.utils.MathUtils
import kotlin.random.Random

@Composable
fun PercentageCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var mode by remember { mutableStateOf(0) } // 0: X% of Y, 1: X is what % of Y, 2: % Change X to Y
    var valX by remember { mutableStateOf("15") }
    var valY by remember { mutableStateOf("250") }

    val x = valX.toDoubleOrNull() ?: 0.0
    val y = valY.toDoubleOrNull() ?: 0.0

    val (primaryResult, breakdown) = remember(mode, x, y) {
        when (mode) {
            0 -> {
                val res = (x / 100.0) * y
                FormatUtils.formatSmart(res) to listOf(
                    "Calculation" to "$x% × $y",
                    "Formula" to "($x / 100) × $y"
                )
            }
            1 -> {
                val res = if (y != 0.0) (x / y) * 100.0 else 0.0
                "${FormatUtils.formatSmart(res)} %" to listOf(
                    "Calculation" to "$x out of $y",
                    "Formula" to "($x / $y) × 100"
                )
            }
            else -> {
                val diff = y - x
                val change = if (x != 0.0) (diff / x) * 100.0 else 0.0
                val sign = if (change >= 0) "+" else ""
                "$sign${FormatUtils.formatSmart(change)} %" to listOf(
                    "Absolute Difference" to FormatUtils.formatSmart(diff),
                    "Type" to if (change >= 0) "Increase" else "Decrease",
                    "Formula" to "(($y - $x) / $x) × 100"
                )
            }
        }
    }

    LaunchedEffect(primaryResult) {
        if (x > 0 && y > 0) {
            val expr = when (mode) {
                0 -> "$x% of $y"
                1 -> "$x as % of $y"
                else -> "% change $x to $y"
            }
            onSaveHistory(expr, primaryResult)
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Select Percentage Mode:", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = mode == 0,
                        onClick = { mode = 0 },
                        label = { Text("What is X% of Y?") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = mode == 1,
                        onClick = { mode = 1 },
                        label = { Text("X is what % of Y?") },
                        modifier = Modifier.weight(1f)
                    )
                }
                FilterChip(
                    selected = mode == 2,
                    onClick = { mode = 2 },
                    label = { Text("% Increase or Decrease from X to Y") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = valX,
                    onValueChange = { valX = it },
                    label = {
                        Text(
                            when (mode) {
                                0 -> "Percentage (X %)"
                                1 -> "Value (X)"
                                else -> "Initial Value (X)"
                            }
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = valY,
                    onValueChange = { valY = it },
                    label = {
                        Text(
                            when (mode) {
                                0 -> "Total Value (Y)"
                                1 -> "Total (Y)"
                                else -> "Final Value (Y)"
                            }
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResultCard(
            title = "Result",
            primaryValue = primaryResult,
            breakdown = breakdown,
            onReset = {
                valX = "15"
                valY = "250"
            }
        )
    }
}

@Composable
fun TipCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var billAmount by remember { mutableStateOf("85.00") }
    var tipPercent by remember { mutableStateOf("18") }
    var peopleCount by remember { mutableStateOf("2") }

    val bill = billAmount.toDoubleOrNull() ?: 0.0
    val tipPct = tipPercent.toDoubleOrNull() ?: 0.0
    val people = (peopleCount.toIntOrNull() ?: 1).coerceAtLeast(1)

    val totalTip = bill * (tipPct / 100.0)
    val totalBill = bill + totalTip
    val perPersonTip = totalTip / people
    val perPersonTotal = totalBill / people

    LaunchedEffect(totalBill) {
        if (bill > 0) {
            onSaveHistory("Tip $tipPct% on $$bill ($people people)", "$ ${FormatUtils.formatSmart(perPersonTotal)}/person")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = billAmount,
                    onValueChange = { billAmount = it },
                    label = { Text("Bill Amount") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Tip Percentage Preset:", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("10", "15", "18", "20").forEach { preset ->
                        FilterChip(
                            selected = tipPercent == preset,
                            onClick = { tipPercent = preset },
                            label = { Text("$preset%") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = tipPercent,
                    onValueChange = { tipPercent = it },
                    label = { Text("Custom Tip %") },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = peopleCount,
                    onValueChange = { peopleCount = it },
                    label = { Text("Split Between (Number of People)") },
                    suffix = { Text("people") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ResultCard(
            title = "Total Per Person",
            primaryValue = "$ ${FormatUtils.formatSmart(perPersonTotal)}",
            breakdown = listOf(
                "Tip Amount Per Person" to "$ ${FormatUtils.formatSmart(perPersonTip)}",
                "Total Tip ($tipPct%)" to "$ ${FormatUtils.formatSmart(totalTip)}",
                "Total Bill (incl. Tip)" to "$ ${FormatUtils.formatSmart(totalBill)}"
            ),
            onReset = {
                billAmount = "85.00"
                tipPercent = "18"
                peopleCount = "2"
            }
        )
    }
}

@Composable
fun UniversalUnitConverterView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(ConverterUtils.UnitCategory.LENGTH) }
    val units = remember(selectedCategory) { ConverterUtils.getUnitsForCategory(selectedCategory) }

    var fromUnitId by remember(selectedCategory) { mutableStateOf(units.firstOrNull()?.id ?: "") }
    var toUnitId by remember(selectedCategory) { mutableStateOf(units.getOrNull(1)?.id ?: units.firstOrNull()?.id ?: "") }
    var inputValue by remember { mutableStateOf("10") }

    val inputNum = inputValue.toDoubleOrNull() ?: 0.0
    val convertedValue = remember(selectedCategory, inputNum, fromUnitId, toUnitId) {
        ConverterUtils.convert(selectedCategory, inputNum, fromUnitId, toUnitId)
    }

    LaunchedEffect(convertedValue) {
        if (inputNum > 0) {
            val fromDef = units.firstOrNull { it.id == fromUnitId }
            val toDef = units.firstOrNull { it.id == toUnitId }
            if (fromDef != null && toDef != null) {
                onSaveHistory("$inputNum ${fromDef.symbol} to ${toDef.symbol}", "${FormatUtils.formatSmart(convertedValue)} ${toDef.symbol}")
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Category Selector
        ScrollableTabRow(
            selectedTabIndex = selectedCategory.ordinal,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            ConverterUtils.UnitCategory.values().forEach { cat ->
                Tab(
                    selected = selectedCategory == cat,
                    onClick = {
                        selectedCategory = cat
                        val newUnits = ConverterUtils.getUnitsForCategory(cat)
                        fromUnitId = newUnits.firstOrNull()?.id ?: ""
                        toUnitId = newUnits.getOrNull(1)?.id ?: newUnits.firstOrNull()?.id ?: ""
                    },
                    text = { Text(cat.displayName) }
                )
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Value to Convert") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("From:", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        units.take(4).forEach { u ->
                            FilterChip(
                                selected = fromUnitId == u.id,
                                onClick = { fromUnitId = u.id },
                                label = { Text("${u.symbol} (${u.name})") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val temp = fromUnitId
                            fromUnitId = toUnitId
                            toUnitId = temp
                        }
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Swap Units")
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("To:", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        units.take(4).forEach { u ->
                            FilterChip(
                                selected = toUnitId == u.id,
                                onClick = { toUnitId = u.id },
                                label = { Text("${u.symbol} (${u.name})") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        val toDef = units.firstOrNull { it.id == toUnitId }
        val fromDef = units.firstOrNull { it.id == fromUnitId }

        ResultCard(
            title = "Converted Result",
            primaryValue = FormatUtils.formatSmart(convertedValue),
            primaryUnit = toDef?.symbol ?: "",
            breakdown = listOf(
                "Input Value" to "$inputNum ${fromDef?.symbol ?: ""}",
                "Category" to selectedCategory.displayName,
                "Formula Ratio" to "1 ${fromDef?.symbol ?: ""} = ${FormatUtils.formatSmart(ConverterUtils.convert(selectedCategory, 1.0, fromUnitId, toUnitId))} ${toDef?.symbol ?: ""}"
            ),
            onReset = {
                inputValue = "10"
            }
        )
    }
}

@Composable
fun NumberSystemConverterView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var decimalInput by remember { mutableStateOf("255") }

    val res = remember(decimalInput) {
        ConverterUtils.convertFromDecimal(decimalInput)
    }

    LaunchedEffect(decimalInput) {
        if (res != null) {
            onSaveHistory("Dec ${res.decimal}", "Bin ${res.binary}, Hex ${res.hex}")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = decimalInput,
                    onValueChange = { decimalInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Decimal (Base 10)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Binary (Base 2):", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(res?.binary ?: "Invalid", style = MaterialTheme.typography.bodyMedium, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Octal (Base 8):", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(res?.octal ?: "Invalid", style = MaterialTheme.typography.bodyMedium, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Hexadecimal (Base 16):", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(res?.hex ?: "Invalid", style = MaterialTheme.typography.bodyMedium, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        ResultCard(
            title = "Hexadecimal Representation",
            primaryValue = res?.hex ?: "N/A",
            primaryUnit = "HEX",
            breakdown = listOf(
                "Binary (Base 2)" to (res?.binary ?: "N/A"),
                "Octal (Base 8)" to (res?.octal ?: "N/A"),
                "Decimal (Base 10)" to (res?.decimal ?: "N/A")
            ),
            onReset = { decimalInput = "255" }
        )
    }
}

@Composable
fun RandomNumberGeneratorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var minVal by remember { mutableStateOf("1") }
    var maxVal by remember { mutableStateOf("100") }
    var countVal by remember { mutableStateOf("1") }
    var generatedList by remember { mutableStateOf(listOf(42)) }

    fun generate() {
        val min = minVal.toIntOrNull() ?: 1
        val max = maxVal.toIntOrNull() ?: 100
        val count = (countVal.toIntOrNull() ?: 1).coerceIn(1, 20)
        if (min <= max) {
            val list = List(count) { Random.nextInt(min, max + 1) }
            generatedList = list
            onSaveHistory("RNG [$min - $max]", list.joinToString(", "))
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = minVal,
                        onValueChange = { minVal = it },
                        label = { Text("Minimum") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = maxVal,
                        onValueChange = { maxVal = it },
                        label = { Text("Maximum") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = countVal,
                    onValueChange = { countVal = it },
                    label = { Text("Number of Random Values") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { generate() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Casino, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Roll / Generate")
                }
            }
        }

        ResultCard(
            title = "Generated Number",
            primaryValue = generatedList.firstOrNull()?.toString() ?: "0",
            breakdown = listOf(
                "All Generated Numbers" to generatedList.joinToString(", "),
                "Range Interval" to "[$minVal to $maxVal]"
            ),
            onReset = {
                minVal = "1"
                maxVal = "100"
                countVal = "1"
                generate()
            }
        )
    }
}
