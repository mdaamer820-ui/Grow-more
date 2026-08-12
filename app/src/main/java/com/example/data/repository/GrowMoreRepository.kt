package com.example.data.repository

import com.example.data.local.TradeDao
import com.example.data.local.TradeJournalEntity
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.model.BrokerInfo
import com.example.data.model.StockTicker
import com.example.data.model.Strategy
import com.example.data.model.Testimonial
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class GrowMoreRepository(
    private val tradeDao: TradeDao,
    private val watchlistDao: WatchlistDao
) {
    val allTrades: Flow<List<TradeJournalEntity>> = tradeDao.getAllTrades()
    val watchlist: Flow<List<WatchlistEntity>> = watchlistDao.getWatchlist()

    suspend fun addTrade(trade: TradeJournalEntity) = tradeDao.insertTrade(trade)
    suspend fun updateTrade(trade: TradeJournalEntity) = tradeDao.updateTrade(trade)
    suspend fun deleteTrade(id: Int) = tradeDao.deleteTradeById(id)

    suspend fun addToWatchlist(item: WatchlistEntity) = watchlistDao.addToWatchlist(item)
    suspend fun removeFromWatchlist(symbol: String) = watchlistDao.removeFromWatchlist(symbol)
    fun isInWatchlist(symbol: String): Flow<Boolean> = watchlistDao.isInWatchlist(symbol)

    // Initial Market Indices
    fun getInitialIndices(): List<StockTicker> {
        return listOf(
            StockTicker("NIFTY 50", "NSE Benchmark Index", 24420.50, +185.30, +0.76, isIndex = true, sector = "Index"),
            StockTicker("BANK NIFTY", "Banking Sector Index", 51280.10, -120.45, -0.23, isIndex = true, sector = "Banking"),
            StockTicker("SENSEX", "BSE Benchmark Index", 80110.25, +540.80, +0.68, isIndex = true, sector = "Index"),
            StockTicker("NIFTY IT", "Tech Sector Index", 41250.75, +680.15, +1.68, isIndex = true, sector = "IT"),
            StockTicker("NIFTY METAL", "Metal & Commodities", 9850.40, +110.20, +1.13, isIndex = true, sector = "Metals")
        )
    }

    // Initial Popular Stocks
    fun getInitialStocks(): List<StockTicker> {
        return listOf(
            StockTicker("RELIANCE", "Reliance Industries Ltd", 2980.40, +35.20, +1.20, sector = "Energy", dayHigh = 2995.0, dayLow = 2940.0, volume = "4.8M", rsi = 64, macdSignal = "Strong Bullish"),
            StockTicker("TCS", "Tata Consultancy Services", 4210.80, +88.50, +2.15, sector = "IT", dayHigh = 4235.0, dayLow = 4120.0, volume = "2.1M", rsi = 72, macdSignal = "Bullish Crossover"),
            StockTicker("HDFCBANK", "HDFC Bank Ltd", 1640.25, -8.30, -0.50, sector = "Banking", dayHigh = 1658.0, dayLow = 1632.0, volume = "8.2M", rsi = 48, macdSignal = "Neutral"),
            StockTicker("ICICIBANK", "ICICI Bank Ltd", 1215.10, +14.60, +1.22, sector = "Banking", dayHigh = 1222.0, dayLow = 1198.0, volume = "5.4M", rsi = 61, macdSignal = "Bullish"),
            StockTicker("INFY", "Infosys Limited", 1820.60, +42.10, +2.37, sector = "IT", dayHigh = 1835.0, dayLow = 1780.0, volume = "6.1M", rsi = 69, macdSignal = "Strong Bullish"),
            StockTicker("TATAMOTORS", "Tata Motors Ltd", 1025.30, -12.40, -1.20, sector = "Auto", dayHigh = 1045.0, dayLow = 1018.0, volume = "3.9M", rsi = 52, macdSignal = "Consolidation"),
            StockTicker("SBIN", "State Bank of India", 845.75, +6.80, +0.81, sector = "Banking", dayHigh = 852.0, dayLow = 838.0, volume = "7.1M", rsi = 57, macdSignal = "Bullish"),
            StockTicker("BHARTIARTL", "Bharti Airtel Ltd", 1460.90, +18.40, +1.28, sector = "Telecom", dayHigh = 1472.0, dayLow = 1440.0, volume = "3.2M", rsi = 66, macdSignal = "Bullish"),
            StockTicker("ABB", "ABB India Ltd", 8420.00, +210.00, +2.56, sector = "Capital Goods", dayHigh = 8480.0, dayLow = 8200.0, volume = "850K", rsi = 74, macdSignal = "Breakout"),
            StockTicker("BSOFT", "Birlasoft Limited", 685.40, +18.20, +2.73, sector = "IT", dayHigh = 692.0, dayLow = 665.0, volume = "1.8M", rsi = 68, macdSignal = "Bullish Crossover"),
            StockTicker("MARICO", "Marico Limited", 642.15, -4.20, -0.65, sector = "FMCG", dayHigh = 650.0, dayLow = 638.0, volume = "1.1M", rsi = 45, macdSignal = "Neutral"),
            StockTicker("AMBUJACEM", "Ambuja Cements Ltd", 655.80, +8.90, +1.38, sector = "Cement", dayHigh = 662.0, dayLow = 645.0, volume = "2.4M", rsi = 59, macdSignal = "Bullish")
        )
    }

    // Helper to produce live randomized minor tick updates for interactive realism
    fun simulatePriceTick(stock: StockTicker): StockTicker {
        val delta = (Random.nextDouble() - 0.49) * (stock.price * 0.004)
        val newPrice = (stock.price + delta).coerceAtLeast(1.0)
        val newChange = stock.change + delta
        val newChangePercent = (newChange / (stock.price - stock.change)) * 100.0
        val history = stock.historyPrices.toMutableList()
        if (history.size >= 10) history.removeAt(0)
        history.add(newPrice.toFloat())

        return stock.copy(
            price = Math.round(newPrice * 100.0) / 100.0,
            change = Math.round(newChange * 100.0) / 100.0,
            changePercent = Math.round(newChangePercent * 100.0) / 100.0,
            historyPrices = history
        )
    }

    // Strategies created & curated by MD Aamer for Grow More
    fun getStrategies(): List<Strategy> {
        return listOf(
            Strategy(
                id = "channel_breakout",
                title = "Channel Breakout Master",
                tag = "Breakout",
                winRate = "82%",
                riskReward = "1:2.8",
                timeFrame = "15m / Daily",
                description = "Identifies tight horizontal price consolidation channels lasting 5+ days. Generates high-probability buy signals when 15-minute volume spikes 2.5x above 20-period SMA.",
                entryCriteria = listOf(
                    "Stock must consolidate inside a 3% price channel for at least 5 sessions.",
                    "Volume must exceed 2.5x the 20-period Simple Moving Average.",
                    "Closing candle on 15m chart must close cleanly above resistance level."
                ),
                exitCriteria = listOf(
                    "Target 1: 1.5x Channel Height.",
                    "Target 2: 2.8x Channel Height with trailing SL at 9 EMA.",
                    "Stop Loss: Placed strictly 0.5% below the lower channel boundary."
                ),
                recommendedStocks = listOf("ABB", "BSOFT", "TCS", "INFY"),
                isPopular = true
            ),
            Strategy(
                id = "nr7_contraction",
                title = "NR7 Volatility Contraction",
                tag = "Range Contraction",
                winRate = "78%",
                riskReward = "1:3.0",
                timeFrame = "Daily / Swing",
                description = "Narrowest Range of 7 days (NR7) strategy designed by MD Aamer to catch explosive multi-day breakout moves right before momentum spikes.",
                entryCriteria = listOf(
                    "Identify candle with the smallest high-low range among last 7 daily candles.",
                    "Wait for next morning's price to cross previous day's high by 0.2%.",
                    "RSI(14) must be resting between 45 and 60."
                ),
                exitCriteria = listOf(
                    "Exit 50% position at 1:2 Risk-Reward ratio.",
                    "Trail remaining 50% using 5-day EMA until daily close under 5 EMA.",
                    "Stop Loss: Low of the NR7 candle."
                ),
                recommendedStocks = listOf("RELIANCE", "TATAMOTORS", "AMBUJACEM"),
                isPopular = true
            ),
            Strategy(
                id = "vwap_reversal",
                title = "VWAP Reversal Scalper",
                tag = "Intraday",
                winRate = "75%",
                riskReward = "1:2.0",
                timeFrame = "5 min",
                description = "High-precision intraday scalp strategy capturing mean-reversion when institutional volume drives prices to extreme VWAP deviation bands.",
                entryCriteria = listOf(
                    "Price stretches 2 Standard Deviations below Daily VWAP.",
                    "Bullish engulfing or hammer candle appears on 5-minute timeframe.",
                    "Stochastic RSI(14,3,3) crosses up from oversold (<20)."
                ),
                exitCriteria = listOf(
                    "Primary Target: Main VWAP Line.",
                    "Secondary Target: +1 Standard Deviation VWAP Band.",
                    "Stop Loss: 0.3% below entry hammer candle low."
                ),
                recommendedStocks = listOf("HDFCBANK", "ICICIBANK", "SBIN"),
                isPopular = false
            ),
            Strategy(
                id = "option_wheel",
                title = "Option Wheel Income Engine",
                tag = "Options",
                winRate = "88%",
                riskReward = "1:1.5",
                timeFrame = "Monthly Options",
                description = "Systematic cash-secured put & covered call selling strategy curated by MD Aamer to generate consistent passive monthly rental yield from Nifty 50 bluechips.",
                entryCriteria = listOf(
                    "Sell 30-Delta Cash Secured Put on high-conviction Nifty 50 stock.",
                    "IV Rank must be above 35th percentile.",
                    "Days to Expiration (DTE) should be between 30 to 45 days."
                ),
                exitCriteria = listOf(
                    "Close option position when 50% of maximum profit is achieved.",
                    "If assigned, immediately sell 30-Delta Covered Call above cost basis.",
                    "Roll position if tested at 21 DTE."
                ),
                recommendedStocks = listOf("RELIANCE", "INFY", "TCS", "BHARTIARTL"),
                isPopular = true
            ),
            Strategy(
                id = "weekly_momentum",
                title = "Weekly Momentum Watch",
                tag = "Swing",
                winRate = "85%",
                riskReward = "1:3.5",
                timeFrame = "Weekly",
                description = "MD Aamer's signature positional strategy targeting 15% to 30% multi-week rallies in top sector leader stocks.",
                entryCriteria = listOf(
                    "Weekly MACD line crosses above Signal line above 0 axis.",
                    "Weekly RSI breaks above 60 with rising volume.",
                    "20 Weekly EMA is sloping upward at >30 degree angle."
                ),
                exitCriteria = listOf(
                    "Target: 25% ROI or 3.5x Risk-Reward.",
                    "Stop Loss: Trailing 20-period Weekly EMA."
                ),
                recommendedStocks = listOf("ABB", "BSOFT", "MARICO", "SBIN"),
                isPopular = true
            )
        )
    }

    // Supported / Integrated Brokers
    fun getBrokers(): List<BrokerInfo> {
        return listOf(
            BrokerInfo("dhan", "Dhan", "DHAN", "0 Brokerage Delivery, Direct TradingView Orders", "4.9 ★", isConnected = true),
            BrokerInfo("zerodha", "Zerodha Kite", "KITE", "India's #1 Discount Broker & Easy Charts", "4.8 ★", isConnected = false),
            BrokerInfo("angelone", "Angel One", "ANGEL", "SmartAPI Algo Trading & Instant Margin", "4.7 ★", isConnected = false),
            BrokerInfo("groww", "Groww", "GROWW", "1-Tap UPI Investing & Clean Interface", "4.7 ★", isConnected = false),
            BrokerInfo("upstox", "Upstox Pro", "UPSTOX", "Fast Execution & Option Chain Analytics", "4.6 ★", isConnected = false)
        )
    }

    // Community Testimonials
    fun getTestimonials(): List<Testimonial> {
        return listOf(
            Testimonial("Rajesh Kumar", "Pro Intraday Trader", "Grow More by MD Aamer completely eliminated my FOMO trades. The NR7 strategy scanner gave me 4 consecutive winning swing trades this month!", "₹1,45,000 Profit"),
            Testimonial("Priya Sharma", "Options Trader", "The Option Wheel strategy guide and entry/exit criteria defined by MD Aamer are super clear and easy to execute. Highly recommended!", "₹82,500 Profit"),
            Testimonial("Amitabh Roy", "Equity Investor", "The built-in Trade Journal saved in local DB helps me track my win rate and ROI effortlessly. Thank you MD Aamer for Grow More!", "₹2,10,000 Profit")
        )
    }
}
