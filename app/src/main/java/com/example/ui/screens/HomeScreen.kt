package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrokerInfo
import com.example.data.model.StockTicker
import com.example.data.model.Strategy
import com.example.data.model.Testimonial
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    indices: List<StockTicker>,
    stocks: List<StockTicker>,
    strategies: List<Strategy>,
    brokers: List<BrokerInfo>,
    testimonials: List<Testimonial>,
    connectedBrokers: Set<String>,
    onSelectStock: (StockTicker) -> Unit,
    onNavigateToStrategies: () -> Unit,
    onNavigateToJournal: () -> Unit,
    onToggleBroker: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Market Overview",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(indices) { index ->
                    IndexCard(index = index)
                }
                // Add India VIX dummy card
                item {
                    IndexCard(StockTicker("INDIA VIX", "Index", 13.42, -0.32, -2.33, true))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left: AI Market Sentiment
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(180.dp)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("AI Market Sentiment", color = TextSecondary, fontSize = 12.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AIMarketSentimentGauge()
                    }
                }

                // Right: Market Breadth
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(180.dp)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Market Breadth", color = TextSecondary, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("NSE", color = TextSecondary, fontSize = 12.sp)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Advance", color = TextSecondary, fontSize = 11.sp)
                                Text("1698", color = ProfitGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Decline", color = TextSecondary, fontSize = 11.sp)
                                Text("812", color = LossRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Unchanged", color = TextSecondary, fontSize = 11.sp)
                                Text("120", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        ) {
                            Box(modifier = Modifier.weight(1698f).fillMaxHeight().background(ProfitGreen))
                            Box(modifier = Modifier.weight(812f).fillMaxHeight().background(LossRed))
                            Box(modifier = Modifier.weight(120f).fillMaxHeight().background(DarkCardBorder))
                        }
                    }
                }
            }
        }

        // AI Buy / Sell Signals (Live)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AI Buy / Sell Signals (Live)", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("View All >", color = BrandPurple, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Dummy Signals
                    AISignalRow("RELIANCE", true, "2 min ago", "2,950", "2,780", "+1.24%")
                    AISignalRow("TCS", true, "5 min ago", "4,120", "3,890", "+0.92%")
                    AISignalRow("HDFCBANK", false, "7 min ago", "1,610", "1,690", "-0.68%")
                    AISignalRow("INFY", true, "10 min ago", "1,780", "1,640", "+1.05%")
                }
            }
        }
        
        // Top Gainers / Losers
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Top Gainers", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Text("Top Losers", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = BrandPurple, modifier = Modifier.fillMaxWidth(0.5f).height(2.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("BEL", color = TextPrimary, fontSize = 12.sp)
                        Text("273.45", color = TextPrimary, fontSize = 12.sp)
                        Text("+5.21%", color = ProfitGreen, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ADANIENT", color = TextPrimary, fontSize = 12.sp)
                        Text("2,915.80", color = TextPrimary, fontSize = 12.sp)
                        Text("+4.32%", color = ProfitGreen, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("TATAMOTORS", color = TextPrimary, fontSize = 12.sp)
                        Text("985.60", color = TextPrimary, fontSize = 12.sp)
                        Text("+3.45%", color = ProfitGreen, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun IndexCard(index: StockTicker) {
    val isPositive = index.change >= 0
    Card(
        modifier = Modifier
            .width(150.dp)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(index.symbol, color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(String.format("%,.2f", index.price), color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${if(isPositive) "+" else ""}${index.change} (${if(isPositive) "+" else ""}${index.changePercent}%)",
                color = if (isPositive) ProfitGreen else LossRed,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Dummy Sparkline
            Canvas(modifier = Modifier.fillMaxWidth().height(24.dp)) {
                // Just a line
                drawLine(
                    color = if (isPositive) ProfitGreen else LossRed,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
fun AIMarketSentimentGauge() {
    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = LossRed,
                startAngle = 180f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = AmberGold,
                startAngle = 240f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = ProfitGreen,
                startAngle = 300f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
            // Draw needle
            drawLine(
                color = Color.White,
                start = center,
                end = androidx.compose.ui.geometry.Offset(size.width * 0.8f, size.height * 0.2f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("BULLISH", color = ProfitGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("AI Confidence", color = TextSecondary, fontSize = 9.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("78%", color = ProfitGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AISignalRow(symbol: String, isBuy: Boolean, time: String, target: String, sl: String, change: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(symbol, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(time, color = TextSecondary, fontSize = 10.sp)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isBuy) ProfitGreenBg else LossRedBg)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                if (isBuy) "BUY" else "SELL",
                color = if (isBuy) ProfitGreen else LossRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1.5f)) {
            Text("Target: $target", color = TextSecondary, fontSize = 11.sp)
            Text("SL: $sl", color = TextSecondary, fontSize = 11.sp)
        }
        Text(change, color = if (change.startsWith("+")) ProfitGreen else LossRed, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
