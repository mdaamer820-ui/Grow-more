package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WatchlistEntity
import com.example.data.model.StockTicker
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.LossRed
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WatchlistScreen(
    watchlistItems: List<WatchlistEntity>,
    liveStocks: List<StockTicker>,
    onSelectStock: (StockTicker) -> Unit,
    onRemoveFromWatchlist: (StockTicker) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("watchlist_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "MY WATCHLIST",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
            Text(
                text = "Track target prices, stop loss levels, and live breakout updates.",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
        }

        if (watchlistItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Your Watchlist is Empty",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tap bookmark on any stock to monitor it here.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(watchlistItems) { item ->
                val matchingLiveStock = liveStocks.find { it.symbol == item.symbol }
                    ?: StockTicker(item.symbol, item.companyName, item.addedPrice, 0.0, 0.0)

                WatchlistCard(
                    item = item,
                    liveStock = matchingLiveStock,
                    onClick = { onSelectStock(matchingLiveStock) },
                    onRemove = { onRemoveFromWatchlist(matchingLiveStock) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun WatchlistCard(
    item: WatchlistEntity,
    liveStock: StockTicker,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val isPositive = liveStock.change >= 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, DarkCardBorder, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BrandPurpleLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.symbol.take(2),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = BrandPurple
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(item.symbol, fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(item.companyName, fontSize = 11.sp, color = TextMuted, maxLines = 1)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${liveStock.price}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(
                            text = "${if (isPositive) "+" else ""}${liveStock.changePercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) ProfitGreen else LossRed
                        )
                    }

                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Added: ₹${item.addedPrice}", fontSize = 11.sp, color = TextMuted)
                Text("Target: ₹${item.targetPrice}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ProfitGreen)
                Text("SL: ₹${item.stopLossPrice}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LossRed)
            }
        }
    }
}
