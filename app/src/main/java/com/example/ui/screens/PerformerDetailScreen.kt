package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Performer
import com.example.model.TalentReel
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
fun PerformerDetailScreen(
    performer: Performer,
    viewModel: PerformerHubViewModel,
    onBack: () -> Unit,
    onBookNow: () -> Unit,
    onOpenChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val reviews by viewModel.reviews.collectAsState()
    val performerReviews = remember(reviews, performer.id) {
        reviews.filter { it.performerId == performer.id }.ifEmpty { reviews }
    }

    val formattedPrice = remember(performer.startingPrice) {
        NumberFormat.getNumberInstance(Locale("en", "IN")).format(performer.startingPrice)
    }
    val formattedHourly = remember(performer.hourlyRate) {
        NumberFormat.getNumberInstance(Locale("en", "IN")).format(performer.hourlyRate)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("performer_detail_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            // Top Navigation & Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4C1D95),
                                    Color(0xFF28104E),
                                    StageSurfaceDark
                                )
                            )
                        )
                ) {
                    // Back & Favorite buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x80000000))
                                .testTag("detail_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { viewModel.toggleSavePerformer(performer.id) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x80000000))
                                    .testTag("detail_fav_btn")
                            ) {
                                Icon(
                                    imageVector = if (performer.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Save",
                                    tint = if (performer.isSaved) StageCoral else Color.White
                                )
                            }
                        }
                    }

                    // Large Avatar positioned at bottom center
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                    ) {
                        PerformerAvatar(
                            name = performer.name,
                            category = performer.category,
                            sizeDp = 84
                        )
                    }
                }
            }

            // Performer Info Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = performer.stageName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = StageTextPrimary
                        )
                        if (performer.isVerified) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Artist",
                                tint = StageNeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = "${performer.category.iconEmoji} ${performer.category.displayName} • ${performer.location}",
                        fontSize = 13.sp,
                        color = StageGoldLight,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = performer.tagline,
                        fontSize = 13.sp,
                        color = StageTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badges row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        performer.badges.forEach { badge ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF2E174D),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StageNeonCyan.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StageNeonCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Stats Row: Rating, Shows, Starting price
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = StageSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = StageGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "${performer.rating}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = StageGold)
                            }
                            Text(text = "${performer.reviewCount} Reviews", fontSize = 11.sp, color = StageTextMuted)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = StageSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "${performer.showsCompleted}+", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = StageNeonCyan)
                            Text(text = "Shows Done", fontSize = 11.sp, color = StageTextMuted)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = StageSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "₹$formattedPrice", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StageGold)
                            Text(text = "Per Show", fontSize = 11.sp, color = StageTextMuted)
                        }
                    }
                }
            }

            // Bio Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "About The Artist",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StageTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = performer.bio,
                        fontSize = 13.sp,
                        color = StageTextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }

            // Skills & Languages
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Specialties & Performance Skills",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = StageTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        performer.skills.forEach { skill ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = StageSurfaceVariant
                            ) {
                                Text(
                                    text = skill,
                                    fontSize = 11.sp,
                                    color = StageTextPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Languages: " + performer.languages.joinToString(", "),
                        fontSize = 12.sp,
                        color = StageGoldLight
                    )
                }
            }

            // Reels Showcase Section
            if (performer.reels.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Performance Reels (${performer.reels.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                items(performer.reels) { reel ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(StageCoral),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = reel.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = StageTextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${reel.viewsCount} views • ${reel.likesCount} likes",
                                    fontSize = 11.sp,
                                    color = StageGoldLight
                                )
                            }
                        }
                    }
                }
            }

            // Client Testimonials / Reviews Section
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Client Reviews & Ratings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                        Text(
                            text = "Verified Hosts",
                            fontSize = 11.sp,
                            color = StageNeonGreen
                        )
                    }
                }
            }

            items(performerReviews) { rev ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    ReviewCardItem(review = rev)
                }
            }
        }

        // Bottom Persistent Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color(0xF2160E27),
            border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Starts at", fontSize = 10.sp, color = StageTextMuted)
                    Text(
                        text = "₹$formattedPrice",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StageGold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onOpenChat,
                        colors = ButtonDefaults.buttonColors(containerColor = StageSurfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("detail_chat_btn")
                    ) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = StageGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chat", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StageGold)
                    }

                    Button(
                        onClick = onBookNow,
                        colors = ButtonDefaults.buttonColors(containerColor = StageCoral),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("detail_book_btn")
                    ) {
                        Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book Artist", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
