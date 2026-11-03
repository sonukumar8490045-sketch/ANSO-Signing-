package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.model.PerformerCategory
import com.example.ui.theme.StageBorder
import com.example.ui.theme.StageCoral
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageSurfaceCard
import com.example.ui.theme.StageSurfaceVariant
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostGigDialog(
    onDismiss: () -> Unit,
    onSubmitGig: (
        title: String,
        organizer: String,
        category: PerformerCategory,
        eventType: String,
        city: String,
        venue: String,
        date: String,
        budget: String,
        desc: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var gigTitle by remember { mutableStateOf("Soulful Bollywood Singer for Destination Sangeet") }
    var organizerName by remember { mutableStateOf("Sharma Wedding Planners") }
    var selectedCategory by remember { mutableStateOf(PerformerCategory.SINGER) }
    var eventType by remember { mutableStateOf("Shaadi / Sangeet Night") }
    var city by remember { mutableStateOf("Jaipur, Rajasthan") }
    var venue by remember { mutableStateOf("Rambagh Palace Lawn") }
    var gigDate by remember { mutableStateOf("Dec 28, 2026") }
    var budgetRange by remember { mutableStateOf("₹50,000 - ₹80,000") }
    var description by remember { mutableStateOf("Looking for an energetic live singer with acoustic guitar and playback songs for 2.5 hours set. Sound system and engineer provided.") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StageSurfaceCard,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .testTag("post_gig_bottom_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Post an Event Gig 🎟️",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = StageTextPrimary
                    )
                    Text(
                        text = "Connect with verified singers, dancers, DJs & comics",
                        fontSize = 11.sp,
                        color = StageGoldLight
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = StageGold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Required Category
            Text(
                text = "Talent Category Needed",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = StageTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            val categories = listOf(
                PerformerCategory.SINGER,
                PerformerCategory.DANCER,
                PerformerCategory.COMEDIAN,
                PerformerCategory.DJ,
                PerformerCategory.MAGICIAN
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedCategory = cat },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) StageGold else StageSurfaceVariant
                    ) {
                        Text(
                            text = "${cat.iconEmoji} ${cat.displayName.split(" ").first()}",
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF1E1000) else StageTextPrimary,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = gigTitle,
                onValueChange = { gigTitle = it },
                label = { Text("Gig Title / Performance Role", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = organizerName,
                    onValueChange = { organizerName = it },
                    label = { Text("Organizer / Host Name", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = eventType,
                    onValueChange = { eventType = it },
                    label = { Text("Event Occasion", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City, State", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = gigDate,
                    onValueChange = { gigDate = it },
                    label = { Text("Event Date", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Venue", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = budgetRange,
                    onValueChange = { budgetRange = it },
                    label = { Text("Budget Range", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Gig Requirements & Schedule Details", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSubmitGig(
                        gigTitle,
                        organizerName,
                        selectedCategory,
                        eventType,
                        city,
                        venue,
                        gigDate,
                        budgetRange,
                        description
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = StageCoral),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("publish_gig_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Post Gig & Invite Pitches",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
