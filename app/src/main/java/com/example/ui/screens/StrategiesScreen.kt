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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Strategy
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StrategiesScreen(
    strategies: List<Strategy>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    val filters = listOf("All", "Breakout", "Intraday", "Swing", "Momentum", "Reversal")
    var expandedStrategyId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("strategies_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "TRADING STRATEGIES",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
            Text(
                text = "Backtested setups curated by MD Aamer for high probability trades.",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) BrandPurple else DarkSurface)
                            .border(1.dp, if (isSelected) BrandPurple else DarkCardBorder, RoundedCornerShape(20.dp))
                            .clickable { onFilterChange(filter) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) Color.White else TextPrimary
                        )
                    }
                }
            }
        }

        val filteredList = if (selectedFilter == "All") {
            strategies
        } else {
            strategies.filter { it.tag.contains(selectedFilter, ignoreCase = true) }
        }

        items(filteredList) { strategy ->
            val isExpanded = expandedStrategyId == strategy.id
            StrategyDetailCard(
                strategy = strategy,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedStrategyId = if (isExpanded) null else strategy.id
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun StrategyDetailCard(
    strategy: Strategy,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
            .clickable { onToggleExpand() },
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrandPurpleLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(strategy.tag, fontSize = 10.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                    }
                    if (strategy.isPopular) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Star, contentDescription = "Popular", tint = AmberGold)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Win Rate: ", fontSize = 11.sp, color = TextMuted)
                    Text(strategy.winRate, fontSize = 12.sp, fontWeight = FontWeight.Black, color = ProfitGreen)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = strategy.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strategy.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Risk/Reward: ${strategy.riskReward}", fontSize = 11.sp, color = AmberGold, fontWeight = FontWeight.Bold)
                Text("Timeframe: ${strategy.timeFrame}", fontSize = 11.sp, color = TextMuted)
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(DarkCardBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text("ENTRY RULES", fontSize = 11.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                strategy.entryCriteria.forEach { rule ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ProfitGreen, modifier = Modifier.padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(rule, fontSize = 12.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("EXIT & RISK MANAGEMENT", fontSize = 11.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                strategy.exitCriteria.forEach { rule ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AmberGold, modifier = Modifier.padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(rule, fontSize = 12.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("RECOMMENDED TICKERS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    strategy.recommendedStocks.forEach { stock ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(stock, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isExpanded) "Tap to collapse ▲" else "Tap for entry/exit rules ▼",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurple,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
