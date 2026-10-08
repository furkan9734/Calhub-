package com.example.model

object CalculatorRegistry {
    val allCalculators: List<CalculatorItem> = listOf(
        // Quick & Basic
        CalculatorItem(
            id = "basic",
            name = "Basic Calculator",
            category = CalculatorCategory.MATH,
            description = "Standard arithmetic operations, parentheses, percentages and history.",
            keywords = listOf("add", "subtract", "multiply", "divide", "math", "simple", "standard", "basic"),
            iconName = "Calculate",
            formula = "Expression Evaluation with standard operator precedence PEMDAS/BODMAS",
            howItWorks = "Evaluates numerical expressions safely respecting parentheses, multiplication/division before addition/subtraction, and percentage transformations."
        ),
        CalculatorItem(
            id = "scientific",
            name = "Scientific Calculator",
            category = CalculatorCategory.MATH,
            description = "Trigonometry (sin, cos, tan), log, ln, square root, powers, factorials and constants.",
            keywords = listOf("sin", "cos", "tan", "trig", "log", "ln", "sqrt", "power", "pi", "factorial", "algebra"),
            iconName = "Functions",
            formula = "f(x) using radians or degrees: sin(θ), cos(θ), log₁₀(x), ln(x), xʸ, n!",
            howItWorks = "Supports mathematical and trigonometric functions with toggleable angle measurement in Degrees or Radians, exact π and e values, and factorial calculations."
        ),

        // Finance
        CalculatorItem(
            id = "emi",
            name = "EMI Calculator",
            category = CalculatorCategory.FINANCE,
            description = "Calculate Equated Monthly Installment (EMI), total interest and visual payment split.",
            keywords = listOf("loan", "emi", "mortgage", "car", "home", "interest", "finance", "bank", "installment"),
            iconName = "AccountBalance",
            formula = "EMI = P × r × (1+r)ⁿ / ((1+r)ⁿ - 1)\nwhere P=Principal, r=Monthly rate (R/12/100), n=Number of months",
            howItWorks = "Calculates your monthly debt obligation based on loan principal, annual interest rate, and tenure in months or years."
        ),
        CalculatorItem(
            id = "loan",
            name = "Loan Calculator",
            category = CalculatorCategory.FINANCE,
            description = "Compute monthly repayment, amortization breakdown and total cost of borrowing.",
            keywords = listOf("loan", "borrow", "debt", "interest", "finance", "mortgage", "car loan", "personal"),
            iconName = "CreditCard",
            formula = "Monthly Payment = P × (r(1+r)ⁿ) / ((1+r)ⁿ - 1)",
            howItWorks = "Determines total payback and interest charges for auto, personal, or mortgage loans with flexible tenure settings."
        ),
        CalculatorItem(
            id = "sip",
            name = "SIP Calculator",
            category = CalculatorCategory.FINANCE,
            description = "Systematic Investment Plan returns, compounding wealth growth and maturity value.",
            keywords = listOf("sip", "mutual fund", "investment", "wealth", "compounding", "returns", "stocks", "growth"),
            iconName = "TrendingUp",
            formula = "M = P × ({[1 + i]ⁿ - 1} / i) × (1 + i)\nwhere P=Monthly investment, i=Periodic rate, n=Number of payments",
            howItWorks = "Estimates future portfolio wealth by regularly investing fixed amounts into mutual funds or index funds over time."
        ),
        CalculatorItem(
            id = "simple_interest",
            name = "Simple Interest",
            category = CalculatorCategory.FINANCE,
            description = "Calculate flat interest accrued over time without periodic compounding.",
            keywords = listOf("simple", "interest", "principal", "rate", "time", "si", "finance", "deposit"),
            iconName = "Paid",
            formula = "SI = (P × R × T) / 100\nTotal Amount = P + SI",
            howItWorks = "Computes interest on the original principal amount for short-term loans, bonds, or simple savings."
        ),
        CalculatorItem(
            id = "compound_interest",
            name = "Compound Interest",
            category = CalculatorCategory.FINANCE,
            description = "Compound interest with yearly, half-yearly, quarterly, or monthly compounding intervals.",
            keywords = listOf("compound", "interest", "ci", "compounding", "savings", "apy", "fixed deposit", "growth"),
            iconName = "ShowChart",
            formula = "A = P × (1 + r/n)^(n×t)\nwhere P=Principal, r=Annual rate, n=Compounding frequency, t=Years",
            howItWorks = "Shows how reinvesting earned interest accelerates savings growth over time, comparing various compounding frequencies."
        ),
        CalculatorItem(
            id = "gst",
            name = "GST Calculator",
            category = CalculatorCategory.FINANCE,
            description = "Add or remove Goods and Services Tax (GST) with preset slabs (5%, 12%, 18%, 28%).",
            keywords = listOf("gst", "tax", "vat", "sales tax", "add gst", "remove gst", "invoice", "price"),
            iconName = "ReceiptLong",
            formula = "Add GST: Total = Base + (Base × Rate%)\nRemove GST: Base = Total / (1 + Rate%)",
            howItWorks = "Quickly adds GST on net prices or extracts the base value and GST component from gross retail prices."
        ),
        CalculatorItem(
            id = "discount",
            name = "Discount Calculator",
            category = CalculatorCategory.FINANCE,
            description = "Calculate savings, final sale price after discount, and optional sales tax.",
            keywords = listOf("discount", "sale", "offer", "coupon", "savings", "percent off", "shopping", "deal"),
            iconName = "LocalOffer",
            formula = "Savings = Original Price × (Discount% / 100)\nFinal Price = Original Price - Savings",
            howItWorks = "Finds exact amount saved on promotional discounts and calculates final check-out price with optional sales tax."
        ),
        CalculatorItem(
            id = "profit_loss",
            name = "Profit & Loss",
            category = CalculatorCategory.FINANCE,
            description = "Determine profit or loss margin, cost vs selling price differences and percentage gain.",
            keywords = listOf("profit", "loss", "cost price", "selling price", "margin", "gain", "business", "commerce"),
            iconName = "PointOfSale",
            formula = "Profit = Selling Price - Cost Price\nProfit % = (Profit / Cost Price) × 100",
            howItWorks = "Evaluates transaction profitability by comparing cost price and selling price for merchants and traders."
        ),
        CalculatorItem(
            id = "salary",
            name = "Salary Calculator",
            category = CalculatorCategory.FINANCE,
            description = "Estimate monthly take-home salary, gross pay, allowances and deduction breakdown.",
            keywords = listOf("salary", "ctc", "take home", "in hand", "income", "payroll", "deductions", "gross", "net"),
            iconName = "Payments",
            formula = "Monthly Take-Home = (Gross Annual - Deductions) / 12",
            howItWorks = "Provides a realistic estimate of monthly take-home salary based on annual CTC, standard allowances, and expected deductions."
        ),
        CalculatorItem(
            id = "tax",
            name = "Tax & Income Bracket",
            category = CalculatorCategory.FINANCE,
            description = "Calculate tax obligation based on progressive income brackets and effective tax rate.",
            keywords = listOf("tax", "income tax", "bracket", "revenue", "effective rate", "deduction"),
            iconName = "RequestQuote",
            formula = "Tax = Σ (Bracket Rate × Taxable Income in Bracket)",
            howItWorks = "Shows progressive taxation tiers and effective overall tax rate for smart personal tax planning."
        ),

        // Health & Fitness
        CalculatorItem(
            id = "bmi",
            name = "BMI Calculator",
            category = CalculatorCategory.HEALTH,
            description = "Calculate Body Mass Index (BMI) in metric (cm/kg) or imperial (ft-in/lb) with health categories.",
            keywords = listOf("bmi", "body mass", "weight", "height", "health", "fitness", "obesity", "overweight", "underweight"),
            iconName = "MonitorWeight",
            formula = "Metric: BMI = weight(kg) / height(m)²\nImperial: BMI = 703 × weight(lb) / height(in)²",
            howItWorks = "Evaluates adult weight category (underweight, normal, overweight, obese) as a general screening tool, not a medical diagnosis."
        ),
        CalculatorItem(
            id = "bmr",
            name = "BMR Calculator",
            category = CalculatorCategory.HEALTH,
            description = "Basal Metabolic Rate: calories burned at rest using the Mifflin-St Jeor equation.",
            keywords = listOf("bmr", "metabolism", "calories", "metabolic rate", "fitness", "diet", "nutrition"),
            iconName = "LocalFireDepartment",
            formula = "Men: 10×W(kg) + 6.25×H(cm) - 5×Age + 5\nWomen: 10×W(kg) + 6.25×H(cm) - 5×Age - 161",
            howItWorks = "Measures the baseline calories your body expends just to keep vital organs functioning at complete rest."
        ),
        CalculatorItem(
            id = "calorie",
            name = "Calorie Calculator",
            category = CalculatorCategory.HEALTH,
            description = "Daily calorie requirements for weight maintenance, healthy weight loss, or muscle gain.",
            keywords = listOf("calorie", "tdee", "diet", "macros", "weight loss", "maintenance", "bulking", "cutting"),
            iconName = "FitnessCenter",
            formula = "TDEE = BMR × Activity Multiplier (1.2 to 1.9)",
            howItWorks = "Combines your basal metabolic rate with physical activity level to calculate exact maintenance and deficit targets."
        ),
        CalculatorItem(
            id = "ideal_weight",
            name = "Ideal Weight Calculator",
            category = CalculatorCategory.HEALTH,
            description = "Estimate healthy body weight ranges based on gender, height and clinical formulas (Devine, Robinson).",
            keywords = listOf("ideal weight", "healthy weight", "target weight", "devine", "body", "scale"),
            iconName = "Scale",
            formula = "Devine Formula:\nMen: 50 kg + 2.3 kg per inch over 5 ft\nWomen: 45.5 kg + 2.3 kg per inch over 5 ft",
            howItWorks = "Calculates standardized healthy weight benchmarks widely used in clinical pharmacology and general wellness."
        ),
        CalculatorItem(
            id = "body_fat",
            name = "Body Fat Calculator",
            category = CalculatorCategory.HEALTH,
            description = "Estimate body fat percentage using gender, waist, neck, hip and height measurements.",
            keywords = listOf("body fat", "lean mass", "fat percentage", "navy", "composition", "physique"),
            iconName = "AccessibilityNew",
            formula = "U.S. Navy Circumference Method based on waist, neck, height and hip logs",
            howItWorks = "Uses tape measurements to estimate body composition, distinguishing fat tissue from lean muscle mass."
        ),

        // Date & Time
        CalculatorItem(
            id = "age",
            name = "Age Calculator",
            category = CalculatorCategory.DATE_TIME,
            description = "Exact age in years, months, days, total weeks, hours, and next birthday countdown.",
            keywords = listOf("age", "birthday", "birth date", "how old", "years", "months", "days", "calendar"),
            iconName = "Cake",
            formula = "Exact calendar calculation accounting for leap years and month day counts",
            howItWorks = "Calculates chronological age from birthdate to current date, breaking down total elapsed months, weeks, days and hours."
        ),
        CalculatorItem(
            id = "date_diff",
            name = "Date Difference",
            category = CalculatorCategory.DATE_TIME,
            description = "Exact duration between two calendar dates in years, months, days, weeks and hours.",
            keywords = listOf("date", "difference", "between dates", "days between", "countdown", "interval", "duration"),
            iconName = "DateRange",
            formula = "Δt = Date₂ - Date₁ (Calendar accurate with leap years)",
            howItWorks = "Computes duration between any two historical or future calendar dates for event planning, contracts, or milestones."
        ),
        CalculatorItem(
            id = "add_date",
            name = "Add / Subtract Date",
            category = CalculatorCategory.DATE_TIME,
            description = "Add or subtract days, weeks, months or years from a reference date.",
            keywords = listOf("add days", "subtract days", "date math", "target date", "future date", "past date"),
            iconName = "EventNote",
            formula = "Result Date = Target Date ± (Years, Months, Days)",
            howItWorks = "Finds exact future or past dates when adding or subtracting time units like 45 business days or 6 months."
        ),
        CalculatorItem(
            id = "time_diff",
            name = "Time Difference",
            category = CalculatorCategory.DATE_TIME,
            description = "Compute hours, minutes and seconds elapsed between two time stamps.",
            keywords = listOf("time", "time difference", "hours", "minutes", "shift", "work hours", "duration"),
            iconName = "Schedule",
            formula = "Duration = End Time - Start Time (handles overnight crossings)",
            howItWorks = "Calculates elapsed working hours or clock intervals, automatically adjusting if the interval crosses midnight."
        ),

        // Math
        CalculatorItem(
            id = "percentage",
            name = "Percentage Calculator",
            category = CalculatorCategory.MATH,
            description = "Calculate percentage of value, what % is X of Y, percentage increase/decrease, and differences.",
            keywords = listOf("percent", "percentage", "increase", "decrease", "ratio", "change", "math"),
            iconName = "Percent",
            formula = "Mode A: (X/100) × Y\nMode B: (X/Y) × 100\nMode C: ((Y - X) / X) × 100",
            howItWorks = "Features 4 intuitive modes for all common percentage tasks: part-to-whole, percentage changes, and value shifts."
        ),
        CalculatorItem(
            id = "fraction",
            name = "Fraction Calculator",
            category = CalculatorCategory.MATH,
            description = "Add, subtract, multiply, and divide fractions with step-by-step reduction to simplest form.",
            keywords = listOf("fraction", "numerator", "denominator", "simplify", "mixed fraction", "math"),
            iconName = "SquareFoot",
            formula = "a/b ± c/d = (ad ± bc) / bd\n(a/b) × (c/d) = ac / bd\n(a/b) ÷ (c/d) = ad / bc",
            howItWorks = "Computes arithmetic on two fractions, finds Greatest Common Divisor to reduce to lowest terms, and gives decimal equivalents."
        ),
        CalculatorItem(
            id = "ratio",
            name = "Ratio Calculator",
            category = CalculatorCategory.MATH,
            description = "Solve proportions (A : B = C : D), scale dimensions, and simplify ratio proportions.",
            keywords = listOf("ratio", "proportion", "scale", "aspect ratio", "solve ratio", "math"),
            iconName = "CompareArrows",
            formula = "A : B = C : D ⇒ A × D = B × C",
            howItWorks = "Finds missing variable in proportional equalities or simplifies two large numbers into minimal integer ratios."
        ),
        CalculatorItem(
            id = "average",
            name = "Average & Statistics",
            category = CalculatorCategory.MATH,
            description = "Calculate Mean, Median, Mode, Range, Min, Max, and Sum for any list of numbers.",
            keywords = listOf("average", "mean", "median", "mode", "statistics", "data", "sum", "range", "min", "max"),
            iconName = "Analytics",
            formula = "Mean = Σx / N\nMedian = Middle sorted value\nMode = Most frequent value",
            howItWorks = "Analyzes any series of values to provide complete central tendency metrics and range spread."
        ),
        CalculatorItem(
            id = "lcm_gcd",
            name = "LCM & GCD / HCF",
            category = CalculatorCategory.MATH,
            description = "Find Least Common Multiple (LCM) and Greatest Common Divisor (GCD/HCF) with prime factors.",
            keywords = listOf("lcm", "gcd", "hcf", "greatest common divisor", "least common multiple", "factors", "primes"),
            iconName = "Hub",
            formula = "GCD using Euclidean algorithm: gcd(a, b) = gcd(b, a mod b)\nLCM(a, b) = |a × b| / GCD(a, b)",
            howItWorks = "Computes Euclidean algorithm factors and multiples for pairs or lists of integers."
        ),
        CalculatorItem(
            id = "power_root",
            name = "Power & Root Calculator",
            category = CalculatorCategory.MATH,
            description = "Calculate exponents (xʸ), square root (√x), cube root (∛x), and custom nth roots.",
            keywords = listOf("power", "root", "exponent", "square root", "cube root", "nth root", "algebra"),
            iconName = "Exposure",
            formula = "y = xⁿ\nRoot = ⁿ√x = x^(1/n)",
            howItWorks = "Calculates integer and floating-point powers, square roots, and arbitrary radical roots."
        ),

        // Converters
        CalculatorItem(
            id = "converter",
            name = "Unit Converter",
            category = CalculatorCategory.CONVERTERS,
            description = "Universal converter for Length, Weight, Temperature, Area, Volume, Speed, Time and Data.",
            keywords = listOf("converter", "unit", "length", "weight", "temp", "celsius", "fahrenheit", "kg", "lbs", "km", "miles", "storage", "bytes"),
            iconName = "SwapHoriz",
            formula = "Value_target = (Value_base × Factor_from) / Factor_to",
            howItWorks = "Normalizes any input unit to SI standard base units, then accurately converts into the desired target unit."
        ),
        CalculatorItem(
            id = "number_system",
            name = "Number System Converter",
            category = CalculatorCategory.CONVERTERS,
            description = "Simultaneous real-time conversion between Decimal, Binary, Octal, and Hexadecimal.",
            keywords = listOf("binary", "decimal", "hex", "octal", "base 2", "base 16", "bits", "bytes", "computer science"),
            iconName = "Pin",
            formula = "Radix Conversion: Base 10 ↔ Base 2, 8, 16 with signed & unsigned integer support",
            howItWorks = "Parses characters according to valid base alphabet (0-1 for binary, 0-F for hex) and updates all systems concurrently."
        ),

        // Business
        CalculatorItem(
            id = "markup_margin",
            name = "Markup & Margin",
            category = CalculatorCategory.BUSINESS,
            description = "Calculate profit margin %, markup %, selling price and gross profit based on cost.",
            keywords = listOf("markup", "margin", "gross margin", "pricing", "retail", "cost", "revenue", "business"),
            iconName = "Storefront",
            formula = "Margin % = (Profit / Revenue) × 100\nMarkup % = (Profit / Cost) × 100",
            howItWorks = "Prevents common business pricing errors by clearly separating margin (percentage of selling price) from markup (percentage of cost)."
        ),
        CalculatorItem(
            id = "break_even",
            name = "Break-Even Calculator",
            category = CalculatorCategory.BUSINESS,
            description = "Find sales volume and revenue required to cover fixed overheads and variable costs.",
            keywords = listOf("break even", "fixed cost", "variable cost", "units", "revenue", "startup", "breakeven"),
            iconName = "Balance",
            formula = "Break-Even Units = Fixed Costs / (Price per Unit - Variable Cost per Unit)\nBreak-Even Revenue = Units × Price",
            howItWorks = "Calculates exact production or sales quantity where net profit equals zero, showing safety margins."
        ),
        CalculatorItem(
            id = "commission",
            name = "Commission Calculator",
            category = CalculatorCategory.BUSINESS,
            description = "Calculate sales agent commission, broker fees and net payout from deal size.",
            keywords = listOf("commission", "broker", "sales", "agent fee", "cut", "royalty", "percentage"),
            iconName = "Handshake",
            formula = "Commission = Total Sales × (Rate% / 100)\nNet Amount = Total Sales - Commission",
            howItWorks = "Computes commissions for real estate, affiliate marketing, or commercial sales reps with net receipts."
        ),
        CalculatorItem(
            id = "cagr",
            name = "CAGR Calculator",
            category = CalculatorCategory.BUSINESS,
            description = "Compound Annual Growth Rate over multi-year investment or business performance.",
            keywords = listOf("cagr", "annual growth", "compounded", "investment", "portfolio", "growth rate", "revenue growth"),
            iconName = "Timeline",
            formula = "CAGR = (Ending Value / Beginning Value)^(1 / Years) - 1",
            howItWorks = "Measures the smoothed annualized return of an asset or business revenue over multiple periods."
        ),

        // Other & Daily Life
        CalculatorItem(
            id = "tip",
            name = "Tip & Bill Split",
            category = CalculatorCategory.OTHER,
            description = "Calculate dining tip with presets (10%, 15%, 18%, 20%) and evenly split bills among friends.",
            keywords = listOf("tip", "bill split", "restaurant", "dining", "gratuity", "split check", "dinner"),
            iconName = "VolunteerActivism",
            formula = "Total Tip = Bill × (Tip% / 100)\nPer Person = (Bill + Tip) / Number of People",
            howItWorks = "Calculates exact tips on restaurant bills and splits total cost evenly among dining party members."
        ),
        CalculatorItem(
            id = "random",
            name = "Random Number Generator",
            category = CalculatorCategory.OTHER,
            description = "Generate single or multiple random integers within custom min/max bounds with unique options.",
            keywords = listOf("random", "rng", "dice", "picker", "draw", "lottery", "number generator"),
            iconName = "Casino",
            formula = "Uniform pseudo-random generation within [Min, Max] using secure random seeds",
            howItWorks = "Produces random numbers for raffles, board games, lucky draws, or statistical sampling."
        ),
        CalculatorItem(
            id = "age_days",
            name = "Age in Days & Seconds",
            category = CalculatorCategory.OTHER,
            description = "Fun chronological breakdown: your exact age in days, hours, minutes, and breaths taken.",
            keywords = listOf("days old", "age in days", "hours", "seconds", "birthday", "fun stats"),
            iconName = "HourglassBottom",
            formula = "Elapsed Days = Today - Birthday\nHours = Days × 24, Minutes = Hours × 60",
            howItWorks = "Provides fascinating perspective on time lived, total sunrises witnessed, and milestones reached."
        ),
        CalculatorItem(
            id = "discount_gst",
            name = "Discount + GST Calculator",
            category = CalculatorCategory.OTHER,
            description = "Combined billing calculator applying percentage discount followed by statutory GST.",
            keywords = listOf("discount and gst", "tax on discount", "retail bill", "checkout", "shopping tax"),
            iconName = "ShoppingBag",
            formula = "Discounted Price = Original - (Original × Discount%)\nFinal Price = Discounted Price + (Discounted Price × GST%)",
            howItWorks = "Simulates realistic retail checkout where discount is applied first to gross item price and GST is assessed on the discounted base."
        )
    )

    fun getById(id: String): CalculatorItem? {
        return allCalculators.firstOrNull { it.id == id }
    }

    fun getByCategory(category: CalculatorCategory): List<CalculatorItem> {
        if (category == CalculatorCategory.ALL) return allCalculators
        return allCalculators.filter { it.category == category }
    }

    fun search(query: String): List<CalculatorItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allCalculators
        return allCalculators.filter { calc ->
            calc.name.lowercase().contains(q) ||
            calc.description.lowercase().contains(q) ||
            calc.category.title.lowercase().contains(q) ||
            calc.keywords.any { it.lowercase().contains(q) }
        }
    }

    fun getRelated(currentId: String, limit: Int = 4): List<CalculatorItem> {
        val current = getById(currentId) ?: return emptyList()
        return allCalculators
            .filter { it.id != currentId && it.category == current.category }
            .take(limit)
            .ifEmpty {
                allCalculators.filter { it.id != currentId }.take(limit)
            }
    }
}
