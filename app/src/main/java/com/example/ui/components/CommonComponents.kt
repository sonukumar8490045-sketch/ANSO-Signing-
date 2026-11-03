package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Performer
import com.example.model.PerformerCategory
import com.example.ui.theme.StageBorder
import com.example.ui.theme.StageCoral
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageNeonCyan
import com.example.ui.theme.StageNeonGreen
import com.example.ui.theme.StageNeonPurple
import com.example.ui.theme.StageSurfaceCard
import com.example.ui.theme.StageSurfaceDark
import com.example.ui.theme.StageSurfaceVariant
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary
import com.example.ui.theme.StageTextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StageCategoryChipsRow(
    selectedCategory: PerformerCategory,
    onCategorySelected: (PerformerCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PerformerCategory.values().forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onCategorySelected(category) }
                    .testTag("category_chip_${category.name.lowercase()}"),
                shape = RoundedCornerShape(24.dp),
                color = if (isSelected) StageGold else StageSurfaceVariant,
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = category.iconEmoji,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (category == PerformerCategory.ALL) "All Talents" else category.displayName.split(" ").first(),
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF1E1000) else StageTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun PerformerAvatar(
    name: String,
    category: PerformerCategory,
    sizeDp: Int = 54,
    modifier: Modifier = Modifier
) {
    val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
    val gradientBrush = remember(category) {
        when (category) {
            PerformerCategory.SINGER -> Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFFEC4899)))
            PerformerCategory.DANCER -> Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF06B6D4)))
            PerformerCategory.COMEDIAN -> Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFEF4444)))
            PerformerCategory.DJ -> Brush.linearGradient(listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6)))
            PerformerCategory.MAGICIAN -> Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFFA855F7)))
            PerformerCategory.MUSICIAN -> Brush.linearGradient(listOf(Color(0xFFEC4899), Color(0xFFF97316)))
            else -> Brush.linearGradient(listOf(StageGold, StageCoral))
        }
    }

    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(gradientBrush)
            .border(2.dp, StageGold.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = initials.ifBlank { "PH" },
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = (sizeDp * 0.35).sp
            )
        }
    }
}

@Composable
fun AudioWaveformCanvas(
    modifier: Modifier = Modifier,
    barCount: Int = 18,
    color: Color = StageGold,
    isPlaying: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val barWidth = size.width / (barCount * 1.6f)
        val gap = barWidth * 0.6f
        val centerY = size.height / 2f

        for (i in 0 until barCount) {
            val factor = if (isPlaying) {
                val wave = Math.sin((phase + (i * 0.4f)).toDouble()).toFloat()
                (0.3f + (0.7f * Math.abs(wave))).coerceIn(0.2f, 1f)
            } else 0.3f

            val barHeight = size.height * factor
            val x = i * (barWidth + gap)
            val y = centerY - (barHeight / 2f)

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f)
            )
        }
    }
}

@Composable
fun PerformerCard(
    performer: Performer,
    onViewDetail: () -> Unit,
    onBookNow: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedPrice = remember(performer.startingPrice) {
        NumberFormat.getNumberInstance(Locale("en", "IN")).format(performer.startingPrice)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onViewDetail() }
            .testTag("performer_card_${performer.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Avatar, Name, Category, Verified, Heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PerformerAvatar(name = performer.name, category = performer.category, sizeDp = 50)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = performer.stageName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (performer.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Artist",
                                tint = StageNeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${performer.category.iconEmoji} ${performer.category.displayName}",
                        fontSize = 12.sp,
                        color = StageGoldLight,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📍 ${performer.location}",
                        fontSize = 11.sp,
                        color = StageTextMuted
                    )
                }

                // Rating Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2A1C05),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = StageGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${performer.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = StageGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tagline / Bio excerpt
            Text(
                text = performer.tagline,
                fontSize = 13.sp,
                color = StageTextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Skill Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                performer.skills.take(3).forEach { skill ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StageSurfaceVariant
                    ) {
                        Text(
                            text = skill,
                            fontSize = 11.sp,
                            color = StageTextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom action row: Price & Book button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Starts from",
                        fontSize = 11.sp,
                        color = StageTextMuted
                    )
                    Text(
                        text = "₹$formattedPrice",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StageGold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onViewDetail,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StageSurfaceVariant,
                            contentColor = StageTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("view_profile_btn_${performer.id}")
                    ) {
                        Text("Profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onBookNow,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StageCoral,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("book_performer_btn_${performer.id}")
                    ) {
                        Text("Book Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
