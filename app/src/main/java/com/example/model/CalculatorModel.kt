package com.example.model

enum class CalculatorCategory(val title: String, val subtitle: String) {
    ALL("All", "Browse all calculators"),
    FINANCE("Finance", "Loans, investments, interest & taxes"),
    HEALTH("Health & Fitness", "BMI, calories, BMR & wellness"),
    DATE_TIME("Date & Time", "Age, intervals & time calculations"),
    MATH("Math", "Percentages, fractions, powers & algebra"),
    CONVERTERS("Converters", "Length, weight, currency & units"),
    BUSINESS("Business", "Margins, markup, break-even & CAGR"),
    OTHER("Other", "Everyday utilities & generators")
}

data class CalculatorItem(
    val id: String,
    val name: String,
    val category: CalculatorCategory,
    val description: String,
    val keywords: List<String>,
    val iconName: String,
    val formula: String,
    val howItWorks: String
)

data class CalculationHistoryItem(
    val id: String,
    val calculatorId: String,
    val calculatorName: String,
    val expression: String,
    val result: String,
    val timestamp: Long,
    val details: String = ""
)
