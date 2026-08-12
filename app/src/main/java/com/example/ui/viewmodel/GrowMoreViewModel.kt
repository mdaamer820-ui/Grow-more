package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.TradeJournalEntity
import com.example.data.local.WatchlistEntity
import com.example.data.model.BrokerInfo
import com.example.data.model.StockTicker
import com.example.data.model.Strategy
import com.example.data.model.Testimonial
import com.example.data.repository.GrowMoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GrowMoreViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = GrowMoreRepository(db.tradeDao(), db.watchlistDao())

    // Real-time market state
    private val _indices = MutableStateFlow<List<StockTicker>>(repository.getInitialIndices())
    val indices: StateFlow<List<StockTicker>> = _indices.asStateFlow()

    private val _stocks = MutableStateFlow<List<StockTicker>>(repository.getInitialStocks())
    val stocks: StateFlow<List<StockTicker>> = _stocks.asStateFlow()

    // Navigation & Search state
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _strategyFilter = MutableStateFlow("All")
    val strategyFilter: StateFlow<String> = _strategyFilter.asStateFlow()

    private val _selectedStockForDetail = MutableStateFlow<StockTicker?>(null)
    val selectedStockForDetail: StateFlow<StockTicker?> = _selectedStockForDetail.asStateFlow()

    // Database state flows from Room
    val watchlist: StateFlow<List<WatchlistEntity>> = repository.watchlist.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val tradeJournal: StateFlow<List<TradeJournalEntity>> = repository.allTrades.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Static / Semi-static lists
    val strategies: List<Strategy> = repository.getStrategies()
    val brokers: List<BrokerInfo> = repository.getBrokers()
    val testimonials: List<Testimonial> = repository.getTestimonials()

    // Brokers connection status
    private val _connectedBrokers = MutableStateFlow(setOf("dhan"))
    val connectedBrokers: StateFlow<Set<String>> = _connectedBrokers.asStateFlow()

    // Filtered stocks based on search query
    val filteredStocks: StateFlow<List<StockTicker>> = combine(_stocks, _searchQuery) { stockList, query ->
        if (query.isBlank()) {
            stockList
        } else {
            stockList.filter {
                it.symbol.contains(query, ignoreCase = true) ||
                it.name.contains(query, ignoreCase = true) ||
                it.sector.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stocks.value)

    init {
        // Start real-time live ticker simulation loop
        startLiveMarketTicks()
    }

    private fun startLiveMarketTicks() {
        viewModelScope.launch {
            while (isActive) {
                delay(2500) // Update tick every 2.5s
                _stocks.value = _stocks.value.map { repository.simulatePriceTick(it) }
                _indices.value = _indices.value.map { repository.simulatePriceTick(it) }
            }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateStrategyFilter(filter: String) {
        _strategyFilter.value = filter
    }

    fun selectStockDetail(stock: StockTicker?) {
        _selectedStockForDetail.value = stock
    }

    // Room DB Operations
    fun toggleWatchlist(stock: StockTicker) {
        viewModelScope.launch {
            val currentWatchlist = watchlist.value
            val existing = currentWatchlist.find { it.symbol == stock.symbol }
            if (existing != null) {
                repository.removeFromWatchlist(stock.symbol)
            } else {
                repository.addToWatchlist(
                    WatchlistEntity(
                        symbol = stock.symbol,
                        companyName = stock.name,
                        addedPrice = stock.price,
                        targetPrice = Math.round(stock.price * 1.05 * 10.0) / 10.0,
                        stopLossPrice = Math.round(stock.price * 0.97 * 10.0) / 10.0
                    )
                )
            }
        }
    }

    fun addTradeLog(
        symbol: String,
        companyName: String,
        buyPrice: Double,
        sellPrice: Double,
        quantity: Int,
        tradeType: String,
        strategyUsed: String,
        notes: String
    ) {
        viewModelScope.launch {
            val pnl = if (sellPrice > 0) (sellPrice - buyPrice) * quantity else 0.0
            val isClosed = sellPrice > 0
            repository.addTrade(
                TradeJournalEntity(
                    stockSymbol = symbol.uppercase(),
                    companyName = companyName,
                    buyPrice = buyPrice,
                    sellPrice = sellPrice,
                    quantity = quantity,
                    tradeType = tradeType,
                    strategyUsed = strategyUsed,
                    notes = notes,
                    isClosed = isClosed,
                    pnlAmount = Math.round(pnl * 100.0) / 100.0
                )
            )
        }
    }

    fun deleteTradeLog(id: Int) {
        viewModelScope.launch {
            repository.deleteTrade(id)
        }
    }

    fun toggleBrokerConnection(brokerId: String) {
        val current = _connectedBrokers.value.toMutableSet()
        if (current.contains(brokerId)) {
            current.remove(brokerId)
        } else {
            current.add(brokerId)
        }
        _connectedBrokers.value = current
    }
}
