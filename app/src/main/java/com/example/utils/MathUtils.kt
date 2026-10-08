package com.example.utils

import java.util.Stack
import kotlin.math.*

object MathUtils {

    enum class AngleMode {
        DEG,
        RAD
    }

    /**
     * Evaluates a mathematical expression safely using Dijkstra's Shunting-yard algorithm.
     * Supports: +, -, *, /, %, ^, unary minus, sin, cos, tan, asin, acos, atan, log, ln, sqrt, ! (factorial), pi, e
     */
    fun evaluateExpression(rawExpr: String, angleMode: AngleMode = AngleMode.DEG): Result<Double> {
        return try {
            val expr = rawExpr
                .replace("×", "*")
                .replace("÷", "/")
                .replace("π", Math.PI.toString())
                .replace("e", Math.E.toString())
                .trim()

            if (expr.isEmpty()) return Result.success(0.0)

            val tokens = tokenize(expr)
            val rpn = shuntingYard(tokens)
            val result = evaluateRpn(rpn, angleMode)
            if (result.isNaN() || result.isInfinite()) {
                Result.failure(ArithmeticException("Math error"))
            } else {
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        val len = expr.length

        while (i < len) {
            val c = expr[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < len && (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }
                c.isLetter() -> {
                    val sb = StringBuilder()
                    while (i < len && expr[i].isLetter()) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString().lowercase())
                }
                c == '-' -> {
                    // Check for unary minus: at start or preceded by operator or open paren
                    val isUnary = tokens.isEmpty() || tokens.last() in listOf("+", "-", "*", "/", "%", "^", "(")
                    if (isUnary) {
                        tokens.add("neg")
                    } else {
                        tokens.add("-")
                    }
                    i++
                }
                c in "+*/%^()!" -> {
                    tokens.add(c.toString())
                    i++
                }
                else -> i++
            }
        }
        return tokens
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "+", "-" -> 1
            "*", "/", "%" -> 2
            "^" -> 3
            "neg" -> 4
            "!" -> 5
            "sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "sqrt" -> 6
            else -> 0
        }
    }

    private fun isFunction(token: String): Boolean {
        return token in listOf("sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "sqrt")
    }

    private fun shuntingYard(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = Stack<String>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                isFunction(token) -> stack.push(token)
                token == "(" -> stack.push(token)
                token == ")" -> {
                    while (stack.isNotEmpty() && stack.peek() != "(") {
                        output.add(stack.pop())
                    }
                    if (stack.isNotEmpty() && stack.peek() == "(") {
                        stack.pop()
                    }
                    if (stack.isNotEmpty() && isFunction(stack.peek())) {
                        output.add(stack.pop())
                    }
                }
                else -> { // Operator
                    while (stack.isNotEmpty() && stack.peek() != "(" &&
                        (precedence(stack.peek()) > precedence(token) ||
                                (precedence(stack.peek()) == precedence(token) && token != "^" && token != "neg"))
                    ) {
                        output.add(stack.pop())
                    }
                    stack.push(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            output.add(stack.pop())
        }
        return output
    }

    private fun evaluateRpn(rpn: List<String>, angleMode: AngleMode): Double {
        val stack = Stack<Double>()
        for (token in rpn) {
            val num = token.toDoubleOrNull()
            if (num != null) {
                stack.push(num)
                continue
            }

            when (token) {
                "+" -> {
                    if (stack.size < 2) return 0.0
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a + b)
                }
                "-" -> {
                    if (stack.size < 2) return 0.0
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a - b)
                }
                "*" -> {
                    if (stack.size < 2) return 0.0
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a * b)
                }
                "/" -> {
                    if (stack.size < 2) return 0.0
                    val b = stack.pop()
                    val a = stack.pop()
                    if (b == 0.0) throw ArithmeticException("Division by zero")
                    stack.push(a / b)
                }
                "%" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    stack.push(a / 100.0)
                }
                "^" -> {
                    if (stack.size < 2) return 0.0
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a.pow(b))
                }
                "neg" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    stack.push(-a)
                }
                "!" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    stack.push(factorial(a))
                }
                "sin" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    val rad = if (angleMode == AngleMode.DEG) Math.toRadians(a) else a
                    stack.push(sin(rad))
                }
                "cos" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    val rad = if (angleMode == AngleMode.DEG) Math.toRadians(a) else a
                    stack.push(cos(rad))
                }
                "tan" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    val rad = if (angleMode == AngleMode.DEG) Math.toRadians(a) else a
                    stack.push(tan(rad))
                }
                "asin" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    val rad = asin(a)
                    stack.push(if (angleMode == AngleMode.DEG) Math.toDegrees(rad) else rad)
                }
                "acos" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    val rad = acos(a)
                    stack.push(if (angleMode == AngleMode.DEG) Math.toDegrees(rad) else rad)
                }
                "atan" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    val rad = atan(a)
                    stack.push(if (angleMode == AngleMode.DEG) Math.toDegrees(rad) else rad)
                }
                "log" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    if (a <= 0) throw ArithmeticException("Log domain error")
                    stack.push(log10(a))
                }
                "ln" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    if (a <= 0) throw ArithmeticException("Ln domain error")
                    stack.push(ln(a))
                }
                "sqrt" -> {
                    if (stack.isEmpty()) return 0.0
                    val a = stack.pop()
                    if (a < 0) throw ArithmeticException("Sqrt domain error")
                    stack.push(sqrt(a))
                }
            }
        }
        return if (stack.isNotEmpty()) stack.pop() else 0.0
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n) || n > 170) throw ArithmeticException("Invalid factorial input")
        var res = 1.0
        val intN = n.toInt()
        for (i in 2..intN) {
            res *= i
        }
        return res
    }

    // --- Specialized Calculator Functions ---

    /**
     * EMI = P × r × (1+r)^n / ((1+r)^n - 1)
     * returns Triple(monthlyEMI, totalInterest, totalPayment)
     */
    fun calculateEMI(principal: Double, annualRatePercent: Double, tenureMonths: Int): Triple<Double, Double, Double> {
        if (principal <= 0 || tenureMonths <= 0) return Triple(0.0, 0.0, 0.0)
        if (annualRatePercent <= 0) {
            val emi = principal / tenureMonths
            return Triple(emi, 0.0, principal)
        }
        val r = (annualRatePercent / 12.0) / 100.0
        val factor = (1.0 + r).pow(tenureMonths.toDouble())
        val emi = principal * r * factor / (factor - 1.0)
        val totalPayment = emi * tenureMonths
        val totalInterest = totalPayment - principal
        return Triple(emi, totalInterest, totalPayment)
    }

    /**
     * SIP: M = P × ({[1 + i]^n - 1} / i) × (1 + i)
     * returns Triple(investedAmount, estimatedReturns, totalValue)
     */
    fun calculateSIP(monthlyInvestment: Double, expectedAnnualReturnPercent: Double, tenureYears: Double): Triple<Double, Double, Double> {
        if (monthlyInvestment <= 0 || tenureYears <= 0) return Triple(0.0, 0.0, 0.0)
        val n = (tenureYears * 12).toInt()
        val i = (expectedAnnualReturnPercent / 12.0) / 100.0
        val totalInvested = monthlyInvestment * n

        val totalValue = if (i > 0) {
            monthlyInvestment * (((1.0 + i).pow(n.toDouble()) - 1.0) / i) * (1.0 + i)
        } else {
            totalInvested
        }
        val returns = totalValue - totalInvested
        return Triple(totalInvested, returns, totalValue)
    }

    fun calculateSimpleInterest(principal: Double, annualRatePercent: Double, timeYears: Double): Pair<Double, Double> {
        val interest = (principal * annualRatePercent * timeYears) / 100.0
        return Pair(interest, principal + interest)
    }

    fun calculateCompoundInterest(principal: Double, annualRatePercent: Double, timeYears: Double, compoundingFrequencyPerYear: Int): Pair<Double, Double> {
        val r = (annualRatePercent / 100.0) / compoundingFrequencyPerYear
        val nt = compoundingFrequencyPerYear * timeYears
        val total = principal * (1.0 + r).pow(nt)
        val interest = total - principal
        return Pair(interest, total)
    }

    fun calculateGST(amount: Double, gstPercent: Double, isAdd: Boolean): Triple<Double, Double, Double> {
        if (isAdd) {
            val gstAmount = (amount * gstPercent) / 100.0
            val total = amount + gstAmount
            return Triple(amount, gstAmount, total)
        } else {
            // Remove GST
            val base = amount / (1.0 + (gstPercent / 100.0))
            val gstAmount = amount - base
            return Triple(base, gstAmount, amount)
        }
    }

    fun calculateDiscount(originalPrice: Double, discountPercent: Double, taxPercent: Double = 0.0): Triple<Double, Double, Double> {
        val discountAmount = originalPrice * (discountPercent / 100.0)
        val discountedPrice = (originalPrice - discountAmount).coerceAtLeast(0.0)
        val taxAmount = if (taxPercent > 0) discountedPrice * (taxPercent / 100.0) else 0.0
        val finalPrice = discountedPrice + taxAmount
        val totalSaved = originalPrice - finalPrice.coerceAtMost(originalPrice)
        return Triple(discountAmount, finalPrice, discountAmount)
    }

    fun calculateBMI(heightCm: Double, weightKg: Double): Pair<Double, String> {
        if (heightCm <= 0 || weightKg <= 0) return Pair(0.0, "Invalid")
        val heightM = heightCm / 100.0
        val bmi = weightKg / (heightM * heightM)
        val category = when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.99 -> "Normal weight"
            bmi in 25.0..29.99 -> "Overweight"
            else -> "Obese"
        }
        return Pair(bmi, category)
    }

    fun calculateBMR(weightKg: Double, heightCm: Double, ageYears: Int, isMale: Boolean): Double {
        return if (isMale) {
            10.0 * weightKg + 6.25 * heightCm - 5.0 * ageYears + 5.0
        } else {
            10.0 * weightKg + 6.25 * heightCm - 5.0 * ageYears - 161.0
        }
    }

    fun gcd(a: Long, b: Long): Long {
        var x = abs(a)
        var y = abs(b)
        while (y != 0L) {
            val t = y
            y = x % y
            x = t
        }
        return x
    }

    fun lcm(a: Long, b: Long): Long {
        if (a == 0L || b == 0L) return 0L
        return abs(a * b) / gcd(a, b)
    }
}
