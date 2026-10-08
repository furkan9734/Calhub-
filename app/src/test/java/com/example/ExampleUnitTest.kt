package com.example

import com.example.utils.ConverterUtils
import com.example.utils.FormatUtils
import com.example.utils.MathUtils
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testMathEvaluatorPrecedenceAndParentheses() {
        val res1 = MathUtils.evaluateExpression("2 + 3 * 4").getOrThrow()
        assertEquals(14.0, res1, 0.001)

        val res2 = MathUtils.evaluateExpression("(2 + 3) * 4").getOrThrow()
        assertEquals(20.0, res2, 0.001)

        val res3 = MathUtils.evaluateExpression("10 / 2 - 3").getOrThrow()
        assertEquals(2.0, res3, 0.001)
    }

    @Test
    fun testScientificFunctions() {
        val sqrtRes = MathUtils.evaluateExpression("sqrt(25)").getOrThrow()
        assertEquals(5.0, sqrtRes, 0.001)

        val sinRes = MathUtils.evaluateExpression("sin(30)", MathUtils.AngleMode.DEG).getOrThrow()
        assertEquals(0.5, sinRes, 0.001)

        val factRes = MathUtils.evaluateExpression("5!").getOrThrow()
        assertEquals(120.0, factRes, 0.001)
    }

    @Test
    fun testFinancialFormulas() {
        // EMI: 100,000 at 12% p.a. for 12 months
        val (emi, totalInterest, totalPayment) = MathUtils.calculateEMI(100000.0, 12.0, 12)
        assertTrue(emi > 8800 && emi < 8900)
        assertEquals(totalPayment, 100000.0 + totalInterest, 1.0)

        // GST: 1000 + 18% = 1180
        val (base, gstAmount, total) = MathUtils.calculateGST(1000.0, 18.0, isAdd = true)
        assertEquals(1000.0, base, 0.01)
        assertEquals(180.0, gstAmount, 0.01)
        assertEquals(1180.0, total, 0.01)

        // Remove GST: 1180 with 18% should recover base 1000
        val (remBase, remGst, remTotal) = MathUtils.calculateGST(1180.0, 18.0, isAdd = false)
        assertEquals(1000.0, remBase, 0.01)
        assertEquals(180.0, remGst, 0.01)
        assertEquals(1180.0, remTotal, 0.01)
    }

    @Test
    fun testBmiCalculation() {
        val (bmi, category) = MathUtils.calculateBMI(175.0, 70.0)
        assertEquals(22.86, bmi, 0.1)
        assertEquals("Normal weight", category)
    }

    @Test
    fun testUnitConversions() {
        val metersToCm = ConverterUtils.convert(
            ConverterUtils.UnitCategory.LENGTH,
            1.0,
            "m",
            "cm"
        )
        assertEquals(100.0, metersToCm, 0.001)

        val kgToG = ConverterUtils.convert(
            ConverterUtils.UnitCategory.WEIGHT,
            1.0,
            "kg",
            "g"
        )
        assertEquals(1000.0, kgToG, 0.001)

        val cToF = ConverterUtils.convert(
            ConverterUtils.UnitCategory.TEMPERATURE,
            0.0,
            "c",
            "f"
        )
        assertEquals(32.0, cToF, 0.001)
    }

    @Test
    fun testNumberSystemConversions() {
        val res = ConverterUtils.convertFromDecimal("255")
        assertNotNull(res)
        assertEquals("11111111", res?.binary)
        assertEquals("377", res?.octal)
        assertEquals("FF", res?.hex)
    }
}
