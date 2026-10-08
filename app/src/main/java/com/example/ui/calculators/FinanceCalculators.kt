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

@Composable
fun EmiCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var loanAmount by remember { mutableStateOf("1000000") }
    var interestRate by remember { mutableStateOf("8.5") }
    var tenure by remember { mutableStateOf("20") }
    var isTenureYears by remember { mutableStateOf(true) }

    val p = loanAmount.toDoubleOrNull() ?: 0.0
    val r = interestRate.toDoubleOrNull() ?: 0.0
    val rawTenure = tenure.toIntOrNull() ?: 0
    val months = if (isTenureYears) rawTenure * 12 else rawTenure

    val (emi, totalInterest, totalPayment) = remember(p, r, months) {
        MathUtils.calculateEMI(p, r, months)
    }

    LaunchedEffect(emi) {
        if (emi > 0) {
            onSaveHistory("EMI for ${FormatUtils.formatSmart(p)} @ $r% for $months mos", "${FormatUtils.formatSmart(emi)}/mo")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = loanAmount,
                    onValueChange = { loanAmount = it },
                    label = { Text("Loan Amount") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("emi_loan_amount_input")
                )

                OutlinedTextField(
                    value = interestRate,
                    onValueChange = { interestRate = it },
                    label = { Text("Interest Rate (% per annum)") },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("emi_interest_rate_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = tenure,
                        onValueChange = { tenure = it },
                        label = { Text("Loan Tenure") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("emi_tenure_input")
                    )

                    Row {
                        FilterChip(
                            selected = isTenureYears,
                            onClick = { isTenureYears = true },
                            label = { Text("Yr") }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        FilterChip(
                            selected = !isTenureYears,
                            onClick = { isTenureYears = false },
                            label = { Text("Mo") }
                        )
                    }
                }
            }
        }

        // Visual breakdown bar
        if (totalPayment > 0) {
            val principalRatio = (p / totalPayment).toFloat().coerceIn(0f, 1f)
            val interestRatio = (totalInterest / totalPayment).toFloat().coerceIn(0f, 1f)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Payment Breakdown", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(principalRatio.coerceAtLeast(0.01f))
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Box(
                            modifier = Modifier
                                .weight(interestRatio.coerceAtLeast(0.01f))
                                .fillMaxHeight()
                                .background(Color(0xFFF59E0B))
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("● Principal: ${(principalRatio * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        Text("● Interest: ${(interestRatio * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = Color(0xFFF59E0B))
                    }
                }
            }
        }

        ResultCard(
            title = "Monthly EMI",
            primaryValue = "$ ${FormatUtils.formatSmart(emi)}",
            primaryUnit = "/ month",
            breakdown = listOf(
                "Principal Loan Amount" to "$ ${FormatUtils.formatSmart(p)}",
                "Total Interest Payable" to "$ ${FormatUtils.formatSmart(totalInterest)}",
                "Total Payment (Principal + Int)" to "$ ${FormatUtils.formatSmart(totalPayment)}"
            ),
            onReset = {
                loanAmount = "1000000"
                interestRate = "8.5"
                tenure = "20"
                isTenureYears = true
            }
        )
    }
}

@Composable
fun SipCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var monthlyInvestment by remember { mutableStateOf("10000") }
    var returnRate by remember { mutableStateOf("12.0") }
    var tenureYears by remember { mutableStateOf("10") }

    val monthly = monthlyInvestment.toDoubleOrNull() ?: 0.0
    val rate = returnRate.toDoubleOrNull() ?: 0.0
    val years = tenureYears.toDoubleOrNull() ?: 0.0

    val (invested, returns, total) = remember(monthly, rate, years) {
        MathUtils.calculateSIP(monthly, rate, years)
    }

    LaunchedEffect(total) {
        if (total > 0) {
            onSaveHistory("SIP $monthly/mo @ $rate% for $years yrs", "$ ${FormatUtils.formatSmart(total)}")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = monthlyInvestment,
                    onValueChange = { monthlyInvestment = it },
                    label = { Text("Monthly Investment") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("sip_monthly_input")
                )

                OutlinedTextField(
                    value = returnRate,
                    onValueChange = { returnRate = it },
                    label = { Text("Expected Annual Return Rate") },
                    suffix = { Text("% p.a.") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("sip_rate_input")
                )

                OutlinedTextField(
                    value = tenureYears,
                    onValueChange = { tenureYears = it },
                    label = { Text("Time Period (Years)") },
                    suffix = { Text("Years") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("sip_tenure_input")
                )
            }
        }

        ResultCard(
            title = "Expected Maturity Value",
            primaryValue = "$ ${FormatUtils.formatSmart(total)}",
            breakdown = listOf(
                "Total Invested Amount" to "$ ${FormatUtils.formatSmart(invested)}",
                "Estimated Wealth Gain" to "$ ${FormatUtils.formatSmart(returns)}",
                "Total Maturity Value" to "$ ${FormatUtils.formatSmart(total)}"
            ),
            onReset = {
                monthlyInvestment = "10000"
                returnRate = "12.0"
                tenureYears = "10"
            }
        )
    }
}

@Composable
fun GstCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAddGst by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("1000") }
    var gstRate by remember { mutableStateOf("18") }

    val rawAmount = amount.toDoubleOrNull() ?: 0.0
    val rawRate = gstRate.toDoubleOrNull() ?: 0.0

    val (base, gstAmount, total) = remember(rawAmount, rawRate, isAddGst) {
        MathUtils.calculateGST(rawAmount, rawRate, isAddGst)
    }

    LaunchedEffect(total) {
        if (total > 0) {
            onSaveHistory("${if (isAddGst) "Add" else "Remove"} GST $rawRate% on $rawAmount", "$ ${FormatUtils.formatSmart(total)}")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = isAddGst,
                        onClick = { isAddGst = true },
                        label = { Text("Add GST (Exclusive)") },
                        modifier = Modifier.weight(1f).testTag("gst_mode_add")
                    )
                    FilterChip(
                        selected = !isAddGst,
                        onClick = { isAddGst = false },
                        label = { Text("Remove GST (Inclusive)") },
                        modifier = Modifier.weight(1f).testTag("gst_mode_remove")
                    )
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(if (isAddGst) "Net Base Amount" else "Gross Price (incl. GST)") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("gst_amount_input")
                )

                Text("Select GST Slab Preset:", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("5", "12", "18", "28").forEach { preset ->
                        FilterChip(
                            selected = gstRate == preset,
                            onClick = { gstRate = preset },
                            label = { Text("$preset%") },
                            modifier = Modifier.weight(1f).testTag("gst_preset_$preset")
                        )
                    }
                }

                OutlinedTextField(
                    value = gstRate,
                    onValueChange = { gstRate = it },
                    label = { Text("Custom GST %") },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("gst_custom_rate_input")
                )
            }
        }

        ResultCard(
            title = if (isAddGst) "Final Price (incl. GST)" else "Net Base Price",
            primaryValue = "$ ${FormatUtils.formatSmart(if (isAddGst) total else base)}",
            breakdown = listOf(
                "Base Amount" to "$ ${FormatUtils.formatSmart(base)}",
                "GST Amount ($rawRate%)" to "$ ${FormatUtils.formatSmart(gstAmount)}",
                "Total Amount" to "$ ${FormatUtils.formatSmart(total)}"
            ),
            onReset = {
                amount = "1000"
                gstRate = "18"
                isAddGst = true
            }
        )
    }
}

@Composable
fun DiscountCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var originalPrice by remember { mutableStateOf("150") }
    var discountPercent by remember { mutableStateOf("25") }
    var salesTaxPercent by remember { mutableStateOf("0") }

    val orig = originalPrice.toDoubleOrNull() ?: 0.0
    val disc = discountPercent.toDoubleOrNull() ?: 0.0
    val tax = salesTaxPercent.toDoubleOrNull() ?: 0.0

    val (savings, finalPrice, _) = remember(orig, disc, tax) {
        MathUtils.calculateDiscount(orig, disc, tax)
    }

    LaunchedEffect(finalPrice) {
        if (finalPrice > 0) {
            onSaveHistory("Discount $disc% on $$orig", "$ ${FormatUtils.formatSmart(finalPrice)}")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = originalPrice,
                    onValueChange = { originalPrice = it },
                    label = { Text("Original Price") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("discount_price_input")
                )

                OutlinedTextField(
                    value = discountPercent,
                    onValueChange = { discountPercent = it },
                    label = { Text("Discount %") },
                    suffix = { Text("% off") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("discount_percent_input")
                )

                OutlinedTextField(
                    value = salesTaxPercent,
                    onValueChange = { salesTaxPercent = it },
                    label = { Text("Optional Sales Tax / GST %") },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("discount_tax_input")
                )
            }
        }

        ResultCard(
            title = "Final Price After Discount",
            primaryValue = "$ ${FormatUtils.formatSmart(finalPrice)}",
            breakdown = listOf(
                "Original Price" to "$ ${FormatUtils.formatSmart(orig)}",
                "Discount Amount Saved" to "$ ${FormatUtils.formatSmart(savings)}",
                "Final Checkout Price" to "$ ${FormatUtils.formatSmart(finalPrice)}"
            ),
            onReset = {
                originalPrice = "150"
                discountPercent = "25"
                salesTaxPercent = "0"
            }
        )
    }
}

@Composable
fun SimpleAndCompoundInterestView(
    isCompound: Boolean,
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var principal by remember { mutableStateOf("100000") }
    var rate by remember { mutableStateOf("7.5") }
    var timeYears by remember { mutableStateOf("5") }
    var frequency by remember { mutableStateOf(1) } // 1=Annually, 2=Semi, 4=Quarterly, 12=Monthly

    val p = principal.toDoubleOrNull() ?: 0.0
    val r = rate.toDoubleOrNull() ?: 0.0
    val t = timeYears.toDoubleOrNull() ?: 0.0

    val (interest, total) = remember(p, r, t, frequency, isCompound) {
        if (isCompound) {
            MathUtils.calculateCompoundInterest(p, r, t, frequency)
        } else {
            MathUtils.calculateSimpleInterest(p, r, t)
        }
    }

    LaunchedEffect(total) {
        if (total > 0) {
            onSaveHistory("${if (isCompound) "CI" else "SI"} $r% on $$p for $t yrs", "$ ${FormatUtils.formatSmart(total)}")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = principal,
                    onValueChange = { principal = it },
                    label = { Text("Principal Amount") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("interest_principal_input")
                )

                OutlinedTextField(
                    value = rate,
                    onValueChange = { rate = it },
                    label = { Text("Annual Interest Rate (%)") },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("interest_rate_input")
                )

                OutlinedTextField(
                    value = timeYears,
                    onValueChange = { timeYears = it },
                    label = { Text("Time Period (Years)") },
                    suffix = { Text("Years") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("interest_time_input")
                )

                if (isCompound) {
                    Text("Compounding Frequency:", style = MaterialTheme.typography.labelMedium)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(1 to "Yearly", 2 to "Half-Yr", 4 to "Quarterly", 12 to "Monthly").forEach { (freqVal, label) ->
                            FilterChip(
                                selected = frequency == freqVal,
                                onClick = { frequency = freqVal },
                                label = { Text(label) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        ResultCard(
            title = "Total Amount",
            primaryValue = "$ ${FormatUtils.formatSmart(total)}",
            breakdown = listOf(
                "Principal Investment" to "$ ${FormatUtils.formatSmart(p)}",
                "Total Interest Accrued" to "$ ${FormatUtils.formatSmart(interest)}",
                "Total Value" to "$ ${FormatUtils.formatSmart(total)}"
            ),
            onReset = {
                principal = "100000"
                rate = "7.5"
                timeYears = "5"
                frequency = 1
            }
        )
    }
}

@Composable
fun ProfitAndLossView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var costPrice by remember { mutableStateOf("800") }
    var sellingPrice by remember { mutableStateOf("1000") }

    val cp = costPrice.toDoubleOrNull() ?: 0.0
    val sp = sellingPrice.toDoubleOrNull() ?: 0.0

    val diff = sp - cp
    val isProfit = diff >= 0
    val percent = if (cp > 0) (Math.abs(diff) / cp) * 100.0 else 0.0

    LaunchedEffect(sp, cp) {
        if (cp > 0 && sp > 0) {
            onSaveHistory("P&L: CP $$cp, SP $$sp", "${if (isProfit) "+" else "-"}$${FormatUtils.formatSmart(Math.abs(diff))} (${FormatUtils.formatSmart(percent)}%)")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = costPrice,
                    onValueChange = { costPrice = it },
                    label = { Text("Cost Price (CP)") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("pl_cost_price_input")
                )

                OutlinedTextField(
                    value = sellingPrice,
                    onValueChange = { sellingPrice = it },
                    label = { Text("Selling Price (SP)") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("pl_selling_price_input")
                )
            }
        }

        ResultCard(
            title = if (isProfit) "Net Profit" else "Net Loss",
            primaryValue = "$ ${FormatUtils.formatSmart(Math.abs(diff))}",
            primaryUnit = if (isProfit) "Profit" else "Loss",
            breakdown = listOf(
                "Cost Price" to "$ ${FormatUtils.formatSmart(cp)}",
                "Selling Price" to "$ ${FormatUtils.formatSmart(sp)}",
                "Status" to if (isProfit) "Profit 🎉" else "Loss ⚠️",
                "Percentage" to "${FormatUtils.formatSmart(percent)} %"
            ),
            onReset = {
                costPrice = "800"
                sellingPrice = "1000"
            }
        )
    }
}

@Composable
fun SalaryCalculatorView(
    onSaveHistory: (expression: String, result: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var annualCtc by remember { mutableStateOf("1200000") }
    var basicPercent by remember { mutableStateOf("50") }
    var deductionsMonthly by remember { mutableStateOf("5000") }

    val ctc = annualCtc.toDoubleOrNull() ?: 0.0
    val monthlyGross = ctc / 12.0
    val monthlyDeduction = deductionsMonthly.toDoubleOrNull() ?: 0.0
    val monthlyInHand = (monthlyGross - monthlyDeduction).coerceAtLeast(0.0)

    LaunchedEffect(monthlyInHand) {
        if (monthlyInHand > 0) {
            onSaveHistory("Salary for $$ctc CTC", "$ ${FormatUtils.formatSmart(monthlyInHand)}/mo")
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = annualCtc,
                    onValueChange = { annualCtc = it },
                    label = { Text("Annual CTC / Cost to Company") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("salary_ctc_input")
                )

                OutlinedTextField(
                    value = basicPercent,
                    onValueChange = { basicPercent = it },
                    label = { Text("Basic Salary Percentage") },
                    suffix = { Text("% of CTC") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = deductionsMonthly,
                    onValueChange = { deductionsMonthly = it },
                    label = { Text("Estimated Monthly Deductions (PF, Tax, Ins)") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("salary_deductions_input")
                )
            }
        }

        ResultCard(
            title = "Estimated Monthly In-Hand Pay",
            primaryValue = "$ ${FormatUtils.formatSmart(monthlyInHand)}",
            primaryUnit = "/ month",
            breakdown = listOf(
                "Annual Gross CTC" to "$ ${FormatUtils.formatSmart(ctc)}",
                "Monthly Gross Pay" to "$ ${FormatUtils.formatSmart(monthlyGross)}",
                "Monthly Deductions" to "$ ${FormatUtils.formatSmart(monthlyDeduction)}",
                "Annual Estimated In-Hand" to "$ ${FormatUtils.formatSmart(monthlyInHand * 12)}"
            ),
            onReset = {
                annualCtc = "1200000"
                basicPercent = "50"
                deductionsMonthly = "5000"
            }
        )
    }
}
