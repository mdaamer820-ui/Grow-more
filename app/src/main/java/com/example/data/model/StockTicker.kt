package com.example.data.model

data class StockTicker(
    val symbol: String,
    val name: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val isIndex: Boolean = false,
    val sector: String = "Equity",
    val dayHigh: Double = price * 1.02,
    val dayLow: Double = price * 0.98,
    val volume: String = "1.2M",
    val rsi: Int = 58,
    val macdSignal: String = "Bullish Crossover",
    val historyPrices: List<Float> = listOf(price.toFloat() * 0.97f, price.toFloat() * 0.98f, price.toFloat() * 0.99f, price.toFloat() * 1.01f, price.toFloat())
)

data class Strategy(
    val id: String,
    val title: String,
    val tag: String, // e.g. "Channel Breakout", "Intraday", "Swing", "Options"
    val winRate: String, // e.g. "78%"
    val riskReward: String, // e.g. "1:2.5"
    val timeFrame: String, // e.g. "15 Min / Daily"
    val description: String,
    val entryCriteria: List<String>,
    val exitCriteria: List<String>,
    val recommendedStocks: List<String>,
    val isPopular: Boolean = false
)

data class BrokerInfo(
    val id: String,
    val name: String,
    val logoTag: String,
    val features: String,
    val rating: String,
    val isConnected: Boolean = false
)

data class Testimonial(
    val name: String,
    val role: String,
    val comment: String,
    val profitEarned: String
)
