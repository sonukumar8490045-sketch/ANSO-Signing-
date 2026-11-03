package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
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
import com.example.model.ChatMessage
import com.example.model.Performer
import com.example.ui.PerformerHubViewModel
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ChatScreen(
    performer: Performer,
    viewModel: PerformerHubViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val chatMessages by viewModel.chatMessages.collectAsState()
    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("chat_screen")
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = StageSurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("chat_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                PerformerAvatar(
                    name = performer.name,
                    category = performer.category,
                    sizeDp = 40
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = performer.stageName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = StageNeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "● Active on PerformerHub • Fast response",
                        fontSize = 11.sp,
                        color = StageNeonGreen
                    )
                }
            }
        }

        // Quick Suggestion Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = listOf(
                "Are you free on my date?",
                "Can you share setlist?",
                "What's your travel fee?"
            )
            suggestions.forEach { prompt ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = StageSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.sendChatMessage(prompt, isFromPerformer = false)
                        }
                ) {
                    Text(
                        text = prompt,
                        fontSize = 11.sp,
                        color = StageTextSecondary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Chat Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatMessages) { msg ->
                ChatBubble(msg = msg)
            }
        }

        // Bottom Input Field
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = StageSurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    placeholder = {
                        Text(
                            "Type gig inquiry, event date, budget...",
                            color = StageTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = StageSurfaceDark,
                        unfocusedContainerColor = StageSurfaceDark,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder,
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary
                    )
                )

                IconButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            viewModel.sendChatMessage(inputMessage.trim(), isFromPerformer = false)
                            inputMessage = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(StageGold)
                        .testTag("chat_send_btn")
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

@Composable
fun ChatBubble(msg: ChatMessage) {
    val isUser = !msg.isFromPerformer
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) StageCoral else StageSurfaceVariant,
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = msg.senderName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) Color(0xFFFFD9E2) else StageGold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = msg.text,
                    fontSize = 13.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )

                if (msg.isQuoteCard && msg.quoteAmount != null) {
                    val formatted = NumberFormat.getNumberInstance(Locale("en", "IN")).format(msg.quoteAmount)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF281C06)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageGold)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Official Quotation Card 🎟️", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StageGold)
                            Text("Total Show Fee: ₹$formatted", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("Sound, engineer and acoustic gear included.", fontSize = 10.sp, color = StageTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.time,
                    fontSize = 10.sp,
                    color = if (isUser) Color(0xCCFFFFFF) else StageTextMuted,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
