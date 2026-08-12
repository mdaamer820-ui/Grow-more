package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TradeJournalEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.LossRed
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun JournalScreen(
    trades: List<TradeJournalEntity>,
    onAddTrade: (symbol: String, company: String, buyPrice: Double, sellPrice: Double, qty: Int, type: String, strategy: String, notes: String) -> Unit,
    onDeleteTrade: (Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    var symbol by remember { mutableStateOf("") }
    var buyPrice by remember { mutableStateOf("") }
    var sellPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var tradeType by remember { mutableStateOf("BUY") }
    var strategyUsed by remember { mutableStateOf("Channel Breakout") }
    var notes by remember { mutableStateOf("") }

    val totalPnL = trades.sumOf { it.pnlAmount }
    val winTrades = trades.count { it.pnlAmount > 0 }
    val winRate = if (trades.isNotEmpty()) (winTrades.toDouble() / trades.size * 100).toInt() else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("journal_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TRADE JOURNAL",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                    Text(
                        text = "Systematic trading logs powered by MD Aamer's methodology.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = { showAddDialog = !showAddDialog },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Log Trade", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats summary card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL P&L", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TextMuted)
                        Text(
                            text = "₹${String.format("%.2f", totalPnL)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = if (totalPnL >= 0) ProfitGreen else LossRed
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL TRADES", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TextMuted)
                        Text("${trades.size}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("WIN RATE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TextMuted)
                        Text("$winRate%", fontSize = 18.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Trade Inline Section
            if (showAddDialog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BrandPurple, RoundedCornerShape(20.dp))
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("LOG NEW TRADE", fontSize = 14.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = symbol,
                            onValueChange = { symbol = it },
                            label = { Text("Stock Symbol (e.g. RELIANCE)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = DarkCardBorder
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = buyPrice,
                                onValueChange = { buyPrice = it },
                                label = { Text("Buy Price (₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPurple,
                                    unfocusedBorderColor = DarkCardBorder
                                ),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = sellPrice,
                                onValueChange = { sellPrice = it },
                                label = { Text("Sell Price (₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPurple,
                                    unfocusedBorderColor = DarkCardBorder
                                ),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantity") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = DarkCardBorder
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Trade Rationale / Notes") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = DarkCardBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val bPrice = buyPrice.toDoubleOrNull() ?: 0.0
                                val sPrice = sellPrice.toDoubleOrNull() ?: 0.0
                                val qty = quantity.toIntOrNull() ?: 1
                                if (symbol.isNotBlank() && bPrice > 0) {
                                    onAddTrade(symbol, symbol, bPrice, sPrice, qty, tradeType, strategyUsed, notes)
                                    symbol = ""
                                    buyPrice = ""
                                    sellPrice = ""
                                    quantity = ""
                                    notes = ""
                                    showAddDialog = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Trade Log", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        if (trades.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Book, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Journal Entries Yet", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text("Log your first trade to track your P&L performance.", fontSize = 12.sp, color = TextMuted)
                    }
                }
            }
        } else {
            items(trades) { trade ->
                TradeCard(trade = trade, onDelete = { onDeleteTrade(trade.id) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun TradeCard(trade: TradeJournalEntity, onDelete: () -> Unit) {
    val isProfit = trade.pnlAmount >= 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isProfit) ProfitGreen.copy(alpha = 0.15f) else LossRed.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = trade.stockSymbol,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isProfit) ProfitGreen else LossRed
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(trade.tradeType, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${if (isProfit) "+" else ""}₹${trade.pnlAmount}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isProfit) ProfitGreen else LossRed
                    )
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Buy: ₹${trade.buyPrice}", fontSize = 12.sp, color = TextPrimary)
                Text("Sell: ₹${trade.sellPrice}", fontSize = 12.sp, color = TextPrimary)
                Text("Qty: ${trade.quantity}", fontSize = 12.sp, color = TextMuted)
            }

            if (trade.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"${trade.notes}\"",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
