package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.BottomNavBar
import com.example.ui.components.HeaderBar
import com.example.ui.components.StockDetailDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JournalScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.StrategiesScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.GrowMoreTheme
import com.example.ui.theme.DarkBackground
import com.example.ui.viewmodel.GrowMoreViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: GrowMoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrowMoreTheme {
                GrowMoreApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GrowMoreApp(viewModel: GrowMoreViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredStocks by viewModel.filteredStocks.collectAsState()
    val indices by viewModel.indices.collectAsState()
    val selectedStockForDetail by viewModel.selectedStockForDetail.collectAsState()
    val watchlist by viewModel.watchlist.collectAsState()
    val tradeJournal by viewModel.tradeJournal.collectAsState()
    val connectedBrokers by viewModel.connectedBrokers.collectAsState()
    val strategyFilter by viewModel.strategyFilter.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            HeaderBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                indices = indices
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    indices = indices,
                    stocks = filteredStocks,
                    strategies = viewModel.strategies,
                    brokers = viewModel.brokers,
                    testimonials = viewModel.testimonials,
                    connectedBrokers = connectedBrokers,
                    onSelectStock = { viewModel.selectStockDetail(it) },
                    onNavigateToStrategies = { viewModel.selectTab(1) },
                    onNavigateToJournal = { viewModel.selectTab(3) },
                    onToggleBroker = { viewModel.toggleBrokerConnection(it) }
                )
                1 -> StrategiesScreen(
                    strategies = viewModel.strategies,
                    selectedFilter = strategyFilter,
                    onFilterChange = { viewModel.updateStrategyFilter(it) }
                )
                2 -> WatchlistScreen(
                    watchlistItems = watchlist,
                    liveStocks = filteredStocks,
                    onSelectStock = { viewModel.selectStockDetail(it) },
                    onRemoveFromWatchlist = { viewModel.toggleWatchlist(it) }
                )
                3 -> JournalScreen(
                    trades = tradeJournal,
                    onAddTrade = { symbol, company, buy, sell, qty, type, strat, notes ->
                        viewModel.addTradeLog(symbol, company, buy, sell, qty, type, strat, notes)
                    },
                    onDeleteTrade = { viewModel.deleteTradeLog(it) }
                )
                4 -> ProfileScreen(
                    connectedBrokersCount = connectedBrokers.size
                )
            }

            // Stock Detail Dialog Modal
            selectedStockForDetail?.let { stock ->
                val isInWatchlist = watchlist.any { it.symbol == stock.symbol }
                StockDetailDialog(
                    stock = stock,
                    isInWatchlist = isInWatchlist,
                    onToggleWatchlist = { viewModel.toggleWatchlist(stock) },
                    onLogTrade = {
                        viewModel.selectTab(3)
                        viewModel.selectStockDetail(null)
                    },
                    onDismiss = { viewModel.selectStockDetail(null) }
                )
            }
        }
    }
}
