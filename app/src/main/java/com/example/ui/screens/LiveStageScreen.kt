package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.LiveChatMessage
import com.example.model.LiveStageSession
import com.example.model.Performer
import com.example.ui.PerformerHubViewModel
import com.example.ui.components.AudioWaveformCanvas
import com.example.ui.components.PerformerAvatar
import com.example.ui.theme.StageBorder
import com.example.ui.theme.StageCoral
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageNeonCyan
import com.example.ui.theme.StageNeonGreen
import com.example.ui.theme.StageSurfaceCard
import com.example.ui.theme.StageSurfaceDark
import com.example.ui.theme.StageSurfaceVariant
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary
import com.example.ui.theme.StageTextSecondary
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LiveStageScreen(
    viewModel: PerformerHubViewModel,
    onBookPerformer: (Performer) -> Unit,
    modifier: Modifier = Modifier
) {
    val liveStreams by viewModel.liveStreams.collectAsState()
    val allPerformers by viewModel.repository.performers.collectAsState()
    val isPerformerMode by viewModel.isPerformerMode.collectAsState()

    var selectedStreamIndex by remember { mutableIntStateOf(0) }
    val currentStream = liveStreams.getOrNull(selectedStreamIndex) ?: liveStreams.firstOrNull()

    var chatInputText by remember { mutableStateOf("") }
    val chatListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll chat to bottom when new messages arrive
    LaunchedEffect(currentStream?.messages?.size) {
        if (currentStream != null && currentStream.messages.isNotEmpty()) {
            chatListState.animateScrollToItem(currentStream.messages.size - 1)
        }
    }

    // Pulse animation for LIVE indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("live_stage_screen")
    ) {
        if (currentStream != null) {
            // Live Stream Background (Concert / DJ Stage)
            Box(modifier = Modifier.fillMaxSize()) {
                val bgDrawable = if (currentStream.category == com.example.model.PerformerCategory.DJ) {
                    R.drawable.performer_live_dj_1793680875950
                } else {
                    R.drawable.performer_hero_stage_1793680862484
                }

                Image(
                    painter = painterResource(id = bgDrawable),
                    contentDescription = "Live Stage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Atmospheric dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xB30D0818),
                                    Color(0x330D0818),
                                    Color(0xD90D0818),
                                    Color(0xF50D0818)
                                )
                            )
                        )
                )
            }

            // Top Header: Performer details, Live badge, Viewers count, Stream Selector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Performer Pill
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xCC1A122B),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PerformerAvatar(
                                name = currentStream.performerName,
                                category = currentStream.category,
                                sizeDp = 32
                            )
                            Column {
                                Text(
                                    text = currentStream.performerStageName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${currentStream.category.displayName.split(" ").first()}",
                                    fontSize = 10.sp,
                                    color = StageGoldLight
                                )
                            }
                        }
                    }

                    // Live badge & viewers
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // LIVE pulsing badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StageCoral.copy(alpha = pulseAlpha)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Text(
                                    text = "LIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }

                        // Viewers count pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x99000000)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "Viewers",
                                    tint = StageNeonCyan,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "${currentStream.currentViewers}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stream Selector tabs (Kabir Live vs DJ Zenith)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    liveStreams.forEachIndexed { index, session ->
                        val isSelected = index == selectedStreamIndex
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedStreamIndex = index },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) StageGold else StageSurfaceVariant.copy(alpha = 0.8f),
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${session.category.iconEmoji} ${session.performerStageName}",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(0xFF1E1000) else StageTextPrimary
                                )
                            }
                        }
                    }
                }

                // Stream Title Card
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0x66000000),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = currentStream.streamTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🪙", fontSize = 12.sp)
                            Text(
                                text = "${currentStream.totalTipsGold} gold",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StageGold
                            )
                        }
                    }
                }
            }

            // Center Stage Audio Waveform
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.6f)
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                AudioWaveformCanvas(
                    modifier = Modifier.fillMaxSize(),
                    barCount = 18,
                    color = StageGold.copy(alpha = 0.8f),
                    isPlaying = true
                )
            }

            // Bottom Section: Live Chat Feed + Gift Bar + Comment Field
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 90.dp)
            ) {
                // Live Chat Messages (scrolling transparent cards)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        state = chatListState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(currentStream.messages) { msg ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (msg.isGift) Color(0xCC3D1F00) else Color(0x99180E29),
                                border = if (msg.isGift) androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.6f)) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (msg.isGift) {
                                        Text(text = msg.giftEmoji, fontSize = 14.sp)
                                    }
                                    Text(
                                        text = msg.user,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (msg.isGift) StageGold else StageNeonCyan
                                    )
                                    Text(
                                        text = msg.message,
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Virtual Gifting Bar: 🌹 Rose, 👏 Claps, 🎤 Mic, 🏆 Trophy, 💎 Diamond
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val gifts = listOf(
                        Triple("🌹", "Rose", 10),
                        Triple("👏", "Clap", 50),
                        Triple("🎤", "Mic", 100),
                        Triple("🏆", "Trophy", 250),
                        Triple("💎", "Diamond", 500)
                    )

                    gifts.forEach { (emoji, label, gold) ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.sendLiveComment(
                                        streamId = currentStream.id,
                                        user = "You",
                                        text = "Sent a $label ($gold gold)!",
                                        giftEmoji = emoji,
                                        isGift = true
                                    )
                                }
                                .testTag("gift_btn_$label"),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xCC201336),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = emoji, fontSize = 18.sp)
                                Text(text = "$gold g", fontSize = 9.sp, color = StageGoldLight, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Book Artist Quick Button
                    val matchingPerf = allPerformers.find { it.id == currentStream.performerId }
                    if (matchingPerf != null) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onBookPerformer(matchingPerf) }
                                .testTag("live_book_btn"),
                            shape = RoundedCornerShape(12.dp),
                            color = StageCoral
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Book", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chat Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = chatInputText,
                        onValueChange = { chatInputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("live_chat_input"),
                        placeholder = {
                            Text(
                                "Cheer or request a song/act...",
                                color = StageTextMuted,
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xCC180F2B),
                            unfocusedContainerColor = Color(0xCC180F2B),
                            focusedBorderColor = StageGold,
                            unfocusedBorderColor = StageBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    IconButton(
                        onClick = {
                            if (chatInputText.isNotBlank()) {
                                viewModel.sendLiveComment(
                                    streamId = currentStream.id,
                                    user = "You",
                                    text = chatInputText.trim()
                                )
                                chatInputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(StageGold)
                            .testTag("live_send_chat_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color(0xFF1E1000),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
