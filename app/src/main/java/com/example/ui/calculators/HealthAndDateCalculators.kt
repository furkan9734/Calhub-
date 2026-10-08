package com.example.ui.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.components.ResultCard
import com.example.utils.FormatUtils
import com.example.utils.MathUtils
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

@Composable
fun BmiCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMetric by remember { mutableStateOf(true) }
    var heightCm by remember { mutableStateOf("175") }
    var weightKg by remember { mutableStateOf("70") }

    var heightFt by remember { mutableStateOf("5") }
    var heightIn by remember { mutableStateOf("9") }
    var weightLb by remember { mutableStateOf("154") }

    val effectiveCm = if (isMetric) {
        heightCm.toDoubleOrNull() ?: 0.0
    } else {
        val ft = heightFt.toDoubleOrNull() ?: 0.0
        val inches = heightIn.toDoubleOrNull() ?: 0.0
        (ft * 12.0 + inches) * 2.54
    }

    val effectiveKg = if (isMetric) {
        weightKg.toDoubleOrNull() ?: 0.0
    } else {
        (weightLb.toDoubleOrNull() ?: 0.0) * 0.45359237
    }

    val (bmi, category) = remember(effectiveCm, effectiveKg) {
        MathUtils.calculateBMI(effectiveCm, effectiveKg)
    }

    LaunchedEffect(bmi) {
        if (bmi > 0) {
            onSaveHistory("BMI ($effectiveCm cm, $effectiveKg kg)", "${FormatUtils.formatNumber(bmi, 1)} ($category)")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isMetric,
                        onClick = { isMetric = true },
                        label = { Text("Metric (cm / kg)") },
                        modifier = Modifier.weight(1f).testTag("bmi_metric_chip")
                    )
                    FilterChip(
                        selected = !isMetric,
                        onClick = { isMetric = false },
                        label = { Text("Imperial (ft-in / lb)") },
                        modifier = Modifier.weight(1f).testTag("bmi_imperial_chip")
                    )
                }

                if (isMetric) {
                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { heightCm = it },
                        label = { Text("Height (cm)") },
                        suffix = { Text("cm") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("bmi_height_cm_input")
                    )

                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { weightKg = it },
                        label = { Text("Weight (kg)") },
                        suffix = { Text("kg") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("bmi_weight_kg_input")
                    )
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = heightFt,
                            onValueChange = { heightFt = it },
                            label = { Text("Feet") },
                            suffix = { Text("ft") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("bmi_height_ft_input")
                        )
                        OutlinedTextField(
                            value = heightIn,
                            onValueChange = { heightIn = it },
                            label = { Text("Inches") },
                            suffix = { Text("in") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("bmi_height_in_input")
                        )
                    }

                    OutlinedTextField(
                        value = weightLb,
                        onValueChange = { weightLb = it },
                        label = { Text("Weight (lbs)") },
                        suffix = { Text("lb") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("bmi_weight_lb_input")
                    )
                }
            }
        }

        // Color health meter
        val categoryColor = when (category) {
            "Underweight" -> Color(0xFF38BDF8)
            "Normal weight" -> Color(0xFF10B981)
            "Overweight" -> Color(0xFFF59E0B)
            "Obese" -> Color(0xFFEF4444)
            else -> MaterialTheme.colorScheme.primary
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("BMI Category Spectrum", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    Box(modifier = Modifier.weight(18.5f).fillMaxHeight().background(Color(0xFF38BDF8)))
                    Box(modifier = Modifier.weight(6.5f).fillMaxHeight().background(Color(0xFF10B981)))
                    Box(modifier = Modifier.weight(5f).fillMaxHeight().background(Color(0xFFF59E0B)))
                    Box(modifier = Modifier.weight(10f).fillMaxHeight().background(Color(0xFFEF4444)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("<18.5 Under", style = MaterialTheme.typography.labelSmall)
                    Text("18.5-24.9 Normal", style = MaterialTheme.typography.labelSmall)
                    Text("25-29.9 Over", style = MaterialTheme.typography.labelSmall)
                    Text("30+ Obese", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        ResultCard(
            title = "Body Mass Index (BMI)",
            primaryValue = FormatUtils.formatNumber(bmi, 1),
            primaryUnit = "kg/m²",
            breakdown = listOf(
                "Health Classification" to category,
                "Normal Weight Range" to "${FormatUtils.formatNumber(18.5 * (effectiveCm/100)*(effectiveCm/100), 1)} - ${FormatUtils.formatNumber(24.9 * (effectiveCm/100)*(effectiveCm/100), 1)} kg",
                "Clinical Note" to "General health indicator, not a medical diagnosis."
            ),
            onReset = {
                heightCm = "175"
                weightKg = "70"
                heightFt = "5"
                heightIn = "9"
                weightLb = "154"
            }
        )
    }
}

@Composable
fun BmrAndCalorieView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMale by remember { mutableStateOf(true) }
    var age by remember { mutableStateOf("28") }
    var heightCm by remember { mutableStateOf("175") }
    var weightKg by remember { mutableStateOf("70") }
    var activityFactor by remember { mutableStateOf(1.375) } // 1.2 Sedentary, 1.375 Light, 1.55 Moderate, 1.725 Heavy

    val w = weightKg.toDoubleOrNull() ?: 70.0
    val h = heightCm.toDoubleOrNull() ?: 175.0
    val a = age.toIntOrNull() ?: 28

    val bmr = remember(w, h, a, isMale) {
        MathUtils.calculateBMR(w, h, a, isMale)
    }
    val tdee = bmr * activityFactor

    LaunchedEffect(tdee) {
        if (tdee > 0) {
            onSaveHistory("Calorie TDEE (${if (isMale) "M" else "F"}, $a yrs)", "${FormatUtils.formatSmart(tdee)} kcal")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isMale,
                        onClick = { isMale = true },
                        label = { Text("Male ♂") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !isMale,
                        onClick = { isMale = false },
                        label = { Text("Female ♀") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        suffix = { Text("yrs") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { heightCm = it },
                        label = { Text("Height") },
                        suffix = { Text("cm") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = weightKg,
                    onValueChange = { weightKg = it },
                    label = { Text("Weight (kg)") },
                    suffix = { Text("kg") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Activity Level:", style = MaterialTheme.typography.labelMedium)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        1.2 to "Sedentary (Little or no exercise)",
                        1.375 to "Light Exercise (1-3 days/week)",
                        1.55 to "Moderate Exercise (3-5 days/week)",
                        1.725 to "Very Active (6-7 days/week)"
                    ).forEach { (factor, desc) ->
                        FilterChip(
                            selected = activityFactor == factor,
                            onClick = { activityFactor = factor },
                            label = { Text(desc) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        ResultCard(
            title = "Daily Maintenance Calories (TDEE)",
            primaryValue = FormatUtils.formatSmart(tdee),
            primaryUnit = "kcal / day",
            breakdown = listOf(
                "Basal Metabolic Rate (BMR)" to "${FormatUtils.formatSmart(bmr)} kcal",
                "Weight Loss Target (-500 kcal)" to "${FormatUtils.formatSmart((tdee - 500).coerceAtLeast(1200.0))} kcal",
                "Weight Gain Target (+500 kcal)" to "${FormatUtils.formatSmart(tdee + 500)} kcal"
            ),
            onReset = {
                age = "28"
                heightCm = "175"
                weightKg = "70"
                activityFactor = 1.375
            }
        )
    }
}

@Composable
fun AgeCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var birthYear by remember { mutableStateOf("1998") }
    var birthMonth by remember { mutableStateOf("5") }
    var birthDay by remember { mutableStateOf("15") }

    val today = remember { LocalDate.now() }
    val birthDate = remember(birthYear, birthMonth, birthDay) {
        try {
            val y = birthYear.toIntOrNull() ?: 1998
            val m = birthMonth.toIntOrNull() ?: 1
            val d = birthDay.toIntOrNull() ?: 1
            LocalDate.of(y, m.coerceIn(1, 12), d.coerceIn(1, 31))
        } catch (_: Exception) {
            null
        }
    }

    val period = remember(birthDate, today) {
        if (birthDate != null && !birthDate.isAfter(today)) {
            Period.between(birthDate, today)
        } else null
    }

    val totalDays = remember(birthDate, today) {
        if (birthDate != null && !birthDate.isAfter(today)) {
            ChronoUnit.DAYS.between(birthDate, today)
        } else 0L
    }

    val totalWeeks = totalDays / 7
    val totalMonths = remember(birthDate, today) {
        if (birthDate != null && !birthDate.isAfter(today)) {
            ChronoUnit.MONTHS.between(birthDate, today)
        } else 0L
    }

    LaunchedEffect(period) {
        if (period != null) {
            onSaveHistory("Age born ${birthDate}", "${period.years}y ${period.months}m ${period.days}d")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Date of Birth:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = birthDay,
                        onValueChange = { birthDay = it },
                        label = { Text("Day") },
                        placeholder = { Text("15") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("age_day_input")
                    )
                    OutlinedTextField(
                        value = birthMonth,
                        onValueChange = { birthMonth = it },
                        label = { Text("Month") },
                        placeholder = { Text("5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("age_month_input")
                    )
                    OutlinedTextField(
                        value = birthYear,
                        onValueChange = { birthYear = it },
                        label = { Text("Year") },
                        placeholder = { Text("1998") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.2f).testTag("age_year_input")
                    )
                }
            }
        }

        ResultCard(
            title = "Chronological Age",
            primaryValue = if (period != null) "${period.years} Years" else "Invalid Date",
            primaryUnit = if (period != null) "${period.months}m ${period.days}d" else "",
            breakdown = listOf(
                "Total Months" to FormatUtils.formatSmart(totalMonths.toDouble()),
                "Total Weeks" to FormatUtils.formatSmart(totalWeeks.toDouble()),
                "Total Days Elapsed" to FormatUtils.formatSmart(totalDays.toDouble()),
                "Calculated As Of" to today.toString()
            ),
            onReset = {
                birthYear = "1998"
                birthMonth = "5"
                birthDay = "15"
            }
        )
    }
}

@Composable
fun DateDifferenceView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var startYear by remember { mutableStateOf("2024") }
    var startMonth by remember { mutableStateOf("1") }
    var startDay by remember { mutableStateOf("1") }

    var endYear by remember { mutableStateOf("2026") }
    var endMonth by remember { mutableStateOf("10") }
    var endDay by remember { mutableStateOf("7") }

    val startDate = remember(startYear, startMonth, startDay) {
        try {
            LocalDate.of(startYear.toInt(), startMonth.toInt(), startDay.toInt())
        } catch (_: Exception) { null }
    }

    val endDate = remember(endYear, endMonth, endDay) {
        try {
            LocalDate.of(endYear.toInt(), endMonth.toInt(), endDay.toInt())
        } catch (_: Exception) { null }
    }

    val diffPeriod = remember(startDate, endDate) {
        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            Period.between(startDate, endDate)
        } else null
    }

    val totalDays = remember(startDate, endDate) {
        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            ChronoUnit.DAYS.between(startDate, endDate)
        } else 0L
    }

    LaunchedEffect(diffPeriod) {
        if (diffPeriod != null) {
            onSaveHistory("$startDate to $endDate", "$totalDays days")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Start Date (D/M/Y):", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = startDay, onValueChange = { startDay = it }, label = { Text("Day") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = startMonth, onValueChange = { startMonth = it }, label = { Text("Month") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = startYear, onValueChange = { startYear = it }, label = { Text("Year") }, modifier = Modifier.weight(1.2f))
                }

                Text("End Date (D/M/Y):", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = endDay, onValueChange = { endDay = it }, label = { Text("Day") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = endMonth, onValueChange = { endMonth = it }, label = { Text("Month") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = endYear, onValueChange = { endYear = it }, label = { Text("Year") }, modifier = Modifier.weight(1.2f))
                }
            }
        }

        ResultCard(
            title = "Date Difference",
            primaryValue = if (diffPeriod != null) "$totalDays Days" else "Invalid Dates",
            breakdown = listOf(
                "Calendar Interval" to if (diffPeriod != null) "${diffPeriod.years}y ${diffPeriod.months}m ${diffPeriod.days}d" else "N/A",
                "Total Weeks" to FormatUtils.formatSmart((totalDays / 7).toDouble()),
                "Total Hours" to FormatUtils.formatSmart((totalDays * 24).toDouble())
            ),
            onReset = {
                startYear = "2024"
                startMonth = "1"
                startDay = "1"
                endYear = "2026"
                endMonth = "10"
                endDay = "7"
            }
        )
    }
}
