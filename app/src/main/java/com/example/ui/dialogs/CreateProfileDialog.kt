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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Stars
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
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageNeonCyan
import com.example.ui.theme.StageSurfaceCard
import com.example.ui.theme.StageSurfaceVariant
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileDialog(
    onDismiss: () -> Unit,
    onSubmitProfile: (
        name: String,
        stageName: String,
        category: PerformerCategory,
        tagline: String,
        bio: String,
        location: String,
        startingPrice: Long,
        hourlyRate: Long,
        skills: String,
        languages: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("Sonu Kumar") }
    var stageName by remember { mutableStateOf("Sonu Live") }
    var selectedCategory by remember { mutableStateOf(PerformerCategory.SINGER) }
    var tagline by remember { mutableStateOf("Versatile Bollywood, Sufi & Acoustic Live Singer 🎤") }
    var bio by remember { mutableStateOf("Passionate stage performer with rich experience in live weddings, corporate events, and acoustic concerts. High energy crowd interaction guaranteed!") }
    var location by remember { mutableStateOf("New Delhi / NCR") }
    var startingPriceText by remember { mutableStateOf("20000") }
    var hourlyRateText by remember { mutableStateOf("10000") }
    var skillsText by remember { mutableStateOf("Bollywood Hits, Sufi Unplugged, Acoustic Guitar, Stage Crowd Work") }
    var languagesText by remember { mutableStateOf("Hindi, English, Punjabi") }

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
                .testTag("create_profile_bottom_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Create Performer Profile 🌟",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = StageTextPrimary
                    )
                    Text(
                        text = "Build your stage identity, upload reels & get booked",
                        fontSize = 11.sp,
                        color = StageGoldLight
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = StageGold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Talent Category Chips
            Text(
                text = "Choose Your Primary Talent",
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
                PerformerCategory.MAGICIAN,
                PerformerCategory.MUSICIAN,
                PerformerCategory.ANCHOR
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.take(4).forEach { cat ->
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
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Name & Stage Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = stageName,
                    onValueChange = { stageName = it },
                    label = { Text("Stage / Band Name", fontSize = 11.sp) },
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

            // Tagline & Location
            OutlinedTextField(
                value = tagline,
                onValueChange = { tagline = it },
                label = { Text("Catchy Stage Tagline", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Primary City / Base", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bio
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Artist Bio & Background", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Rates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = startingPriceText,
                    onValueChange = { startingPriceText = it },
                    label = { Text("Base Show Fee (₹)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = hourlyRateText,
                    onValueChange = { hourlyRateText = it },
                    label = { Text("Hourly Rate (₹)", fontSize = 11.sp) },
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

            // Skills & Languages
            OutlinedTextField(
                value = skillsText,
                onValueChange = { skillsText = it },
                label = { Text("Skills (comma separated)", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = languagesText,
                onValueChange = { languagesText = it },
                label = { Text("Performance Languages", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
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
                    val startPrice = startingPriceText.toLongOrNull() ?: 20000L
                    val hourly = hourlyRateText.toLongOrNull() ?: 10000L
                    onSubmitProfile(
                        name,
                        stageName,
                        selectedCategory,
                        tagline,
                        bio,
                        location,
                        startPrice,
                        hourly,
                        skillsText,
                        languagesText
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = StageGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("publish_profile_btn")
            ) {
                Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFF1E1000))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Publish Profile & Launch on Stage",
                    color = Color(0xFF1E1000),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
