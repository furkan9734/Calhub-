package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.model.CalculatorCategory

object CalcHubIcons {
    fun getIconForName(name: String): ImageVector {
        return when (name) {
            "Calculate" -> Icons.Default.Calculate
            "Functions" -> Icons.Default.Functions
            "AccountBalance" -> Icons.Default.AccountBalance
            "CreditCard" -> Icons.Default.CreditCard
            "TrendingUp" -> Icons.Default.TrendingUp
            "Paid" -> Icons.Default.Paid
            "ShowChart" -> Icons.Default.ShowChart
            "ReceiptLong" -> Icons.Default.ReceiptLong
            "LocalOffer" -> Icons.Default.LocalOffer
            "PointOfSale" -> Icons.Default.PointOfSale
            "Payments" -> Icons.Default.Payments
            "RequestQuote" -> Icons.Default.RequestQuote
            "MonitorWeight" -> Icons.Default.MonitorWeight
            "LocalFireDepartment" -> Icons.Default.LocalFireDepartment
            "FitnessCenter" -> Icons.Default.FitnessCenter
            "Scale" -> Icons.Default.Scale
            "AccessibilityNew" -> Icons.Default.AccessibilityNew
            "Cake" -> Icons.Default.Cake
            "DateRange" -> Icons.Default.DateRange
            "EventNote" -> Icons.Default.EventNote
            "Schedule" -> Icons.Default.Schedule
            "Percent" -> Icons.Default.Percent
            "SquareFoot" -> Icons.Default.SquareFoot
            "CompareArrows" -> Icons.Default.CompareArrows
            "Analytics" -> Icons.Default.Analytics
            "Hub" -> Icons.Default.Hub
            "Exposure" -> Icons.Default.Exposure
            "SwapHoriz" -> Icons.Default.SwapHoriz
            "Pin" -> Icons.Default.Pin
            "Storefront" -> Icons.Default.Storefront
            "Balance" -> Icons.Default.Balance
            "Handshake" -> Icons.Default.Handshake
            "Timeline" -> Icons.Default.Timeline
            "VolunteerActivism" -> Icons.Default.VolunteerActivism
            "Casino" -> Icons.Default.Casino
            "HourglassBottom" -> Icons.Default.HourglassBottom
            "ShoppingBag" -> Icons.Default.ShoppingBag
            else -> Icons.Default.Calculate
        }
    }

    fun getCategoryIcon(category: CalculatorCategory): ImageVector {
        return when (category) {
            CalculatorCategory.ALL -> Icons.Default.Apps
            CalculatorCategory.FINANCE -> Icons.Default.AccountBalance
            CalculatorCategory.HEALTH -> Icons.Default.Favorite
            CalculatorCategory.DATE_TIME -> Icons.Default.DateRange
            CalculatorCategory.MATH -> Icons.Default.Calculate
            CalculatorCategory.CONVERTERS -> Icons.Default.SwapHoriz
            CalculatorCategory.BUSINESS -> Icons.Default.TrendingUp
            CalculatorCategory.OTHER -> Icons.Default.Lightbulb
        }
    }
}
