package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trade_journal")
data class TradeJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stockSymbol: String,
    val companyName: String,
    val buyPrice: Double,
    val sellPrice: Double = 0.0,
    val quantity: Int,
    val tradeType: String, // "BUY" or "SELL" or "OPTION"
    val strategyUsed: String,
    val tradeDate: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isClosed: Boolean = false,
    val pnlAmount: Double = 0.0
)

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbol: String,
    val companyName: String,
    val addedPrice: Double,
    val targetPrice: Double = 0.0,
    val stopLossPrice: Double = 0.0,
    val addedDate: Long = System.currentTimeMillis()
)
