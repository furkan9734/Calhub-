package com.example.utils

object ConverterUtils {

    enum class UnitCategory(val displayName: String) {
        LENGTH("Length"),
        WEIGHT("Weight"),
        TEMPERATURE("Temperature"),
        AREA("Area"),
        VOLUME("Volume"),
        SPEED("Speed"),
        DATA("Data Storage"),
        TIME("Time")
    }

    data class UnitDef(val id: String, val name: String, val symbol: String, val toBaseFactor: Double)

    val lengthUnits = listOf(
        UnitDef("mm", "Millimeter", "mm", 0.001),
        UnitDef("cm", "Centimeter", "cm", 0.01),
        UnitDef("m", "Meter", "m", 1.0),
        UnitDef("km", "Kilometer", "km", 1000.0),
        UnitDef("in", "Inch", "in", 0.0254),
        UnitDef("ft", "Foot", "ft", 0.3048),
        UnitDef("yd", "Yard", "yd", 0.9144),
        UnitDef("mi", "Mile", "mi", 1609.344)
    )

    val weightUnits = listOf(
        UnitDef("mg", "Milligram", "mg", 0.000001),
        UnitDef("g", "Gram", "g", 0.001),
        UnitDef("kg", "Kilogram", "kg", 1.0),
        UnitDef("oz", "Ounce", "oz", 0.028349523125),
        UnitDef("lb", "Pound", "lb", 0.45359237)
    )

    val areaUnits = listOf(
        UnitDef("sq_m", "Square Meter", "m²", 1.0),
        UnitDef("sq_km", "Square Kilometer", "km²", 1000000.0),
        UnitDef("sq_ft", "Square Foot", "ft²", 0.092903),
        UnitDef("acre", "Acre", "ac", 4046.8564224),
        UnitDef("hectare", "Hectare", "ha", 10000.0)
    )

    val volumeUnits = listOf(
        UnitDef("ml", "Milliliter", "mL", 0.001),
        UnitDef("l", "Liter", "L", 1.0),
        UnitDef("gal", "US Gallon", "gal", 3.785411784),
        UnitDef("cup", "US Cup", "cup", 0.2365882365)
    )

    val speedUnits = listOf(
        UnitDef("mps", "Meter per second", "m/s", 1.0),
        UnitDef("kmh", "Kilometer per hour", "km/h", 0.277777778),
        UnitDef("mph", "Miles per hour", "mph", 0.44704)
    )

    val dataUnits = listOf(
        UnitDef("bit", "Bit", "b", 0.125),
        UnitDef("byte", "Byte", "B", 1.0),
        UnitDef("kb", "Kilobyte", "KB", 1024.0),
        UnitDef("mb", "Megabyte", "MB", 1024.0 * 1024.0),
        UnitDef("gb", "Gigabyte", "GB", 1024.0 * 1024.0 * 1024.0),
        UnitDef("tb", "Terabyte", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0)
    )

    val timeUnits = listOf(
        UnitDef("sec", "Second", "s", 1.0),
        UnitDef("min", "Minute", "min", 60.0),
        UnitDef("hr", "Hour", "hr", 3600.0),
        UnitDef("day", "Day", "d", 86400.0),
        UnitDef("week", "Week", "wk", 604800.0)
    )

    fun getUnitsForCategory(category: UnitCategory): List<UnitDef> {
        return when (category) {
            UnitCategory.LENGTH -> lengthUnits
            UnitCategory.WEIGHT -> weightUnits
            UnitCategory.AREA -> areaUnits
            UnitCategory.VOLUME -> volumeUnits
            UnitCategory.SPEED -> speedUnits
            UnitCategory.DATA -> dataUnits
            UnitCategory.TIME -> timeUnits
            UnitCategory.TEMPERATURE -> listOf(
                UnitDef("c", "Celsius", "°C", 1.0),
                UnitDef("f", "Fahrenheit", "°F", 1.0),
                UnitDef("k", "Kelvin", "K", 1.0)
            )
        }
    }

    fun convert(category: UnitCategory, value: Double, fromUnitId: String, toUnitId: String): Double {
        if (category == UnitCategory.TEMPERATURE) {
            return convertTemperature(value, fromUnitId, toUnitId)
        }
        val units = getUnitsForCategory(category)
        val from = units.firstOrNull { it.id == fromUnitId } ?: return value
        val to = units.firstOrNull { it.id == toUnitId } ?: return value
        val baseValue = value * from.toBaseFactor
        return baseValue / to.toBaseFactor
    }

    private fun convertTemperature(value: Double, from: String, to: String): Double {
        if (from == to) return value
        // Convert to Celsius first
        val celsius = when (from) {
            "c" -> value
            "f" -> (value - 32.0) * (5.0 / 9.0)
            "k" -> value - 273.15
            else -> value
        }
        // Convert Celsius to target
        return when (to) {
            "c" -> celsius
            "f" -> (celsius * (9.0 / 5.0)) + 32.0
            "k" -> celsius + 273.15
            else -> celsius
        }
    }

    // Number System: Decimal, Binary, Octal, Hex
    data class NumberSystemResult(
        val decimal: String,
        val binary: String,
        val octal: String,
        val hex: String
    )

    fun convertFromDecimal(decStr: String): NumberSystemResult? {
        val clean = decStr.trim()
        val num = clean.toLongOrNull() ?: return null
        return NumberSystemResult(
            decimal = num.toString(),
            binary = java.lang.Long.toBinaryString(num),
            octal = java.lang.Long.toOctalString(num),
            hex = java.lang.Long.toHexString(num).uppercase()
        )
    }

    fun convertFromBinary(binStr: String): NumberSystemResult? {
        val clean = binStr.trim()
        val num = try { java.lang.Long.parseLong(clean, 2) } catch (_: Exception) { return null }
        return convertFromDecimal(num.toString())
    }

    fun convertFromOctal(octStr: String): NumberSystemResult? {
        val clean = octStr.trim()
        val num = try { java.lang.Long.parseLong(clean, 8) } catch (_: Exception) { return null }
        return convertFromDecimal(num.toString())
    }

    fun convertFromHex(hexStr: String): NumberSystemResult? {
        val clean = hexStr.trim().removePrefix("0x").removePrefix("0X")
        val num = try { java.lang.Long.parseLong(clean, 16) } catch (_: Exception) { return null }
        return convertFromDecimal(num.toString())
    }
}
