package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.ui.components.AudioWaveformCanvas
import com.example.ui.components.PerformerAvatar
import com.example.ui.components.StageCategoryChipsRow
import com.example.ui.theme.StageCoral
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageNeonCyan
import com.example.ui.theme.StageSurfaceDark
import com.example.ui.theme.StageSurfaceVariant
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary
import com.example.ui.theme.StageTextSecondary

@Composable
fun ReelsScreen(
    viewModel: PerformerHubViewModel,
    onBookPerformer: (Performer) -> Unit,
    modifier: Modifier = Modifier
) {
    val reels by viewModel.allReels.collectAsState()
    val allPerformers by viewModel.repository.performers.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val pagerState = rememberPagerState(pageCount = { reels.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("reels_screen")
    ) {
        if (reels.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎬", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Talent Reels in this category yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = StageTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.selectCategory(com.example.model.PerformerCategory.ALL) },
                        colors = ButtonDefaults.buttonColors(containerColor = StageGold)
                    ) {
                        Text("Show All Reels", color = Color(0xFF1E1000), fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val reel = reels[page]
                val matchingPerformer = allPerformers.find { it.id == reel.performerId }

                SingleReelPlayerItem(
                    reel = reel,
                    matchingPerformer = matchingPerformer,
                    onToggleLike = { viewModel.toggleLikeReel(reel.id) },
                    onBookPerformer = {
                        matchingPerformer?.let { onBookPerformer(it) }
                            ?: viewModel.showMessage("Artist details ready for booking")
                    },
                    onTipArtist = {
                        viewModel.showMessage("Sent 🌹 Rose tip to ${reel.performerName}!")
                    },
                    onShareReel = {
                        viewModel.showMessage("Reel link copied! Share with event clients. 🔗")
                    }
                )
            }
        }

        // Top Overlay: Category Filter Chips
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xE60D0818), Color(0x990D0818), Color.Transparent)
                    )
                )
                .padding(top = 10.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Talent Showcase",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF28183E),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "Show Your Talent 🎬",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StageGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            StageCategoryChipsRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )
        }
    }
}

@Composable
fun SingleReelPlayerItem(
    reel: TalentReel,
    matchingPerformer: Performer?,
    onToggleLike: () -> Unit,
    onBookPerformer: () -> Unit,
    onTipArtist: () -> Unit,
    onShareReel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }

    // Vinyl rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val backgroundGradient = remember(reel) {
        Brush.verticalGradient(
            colors = listOf(
                Color(reel.primaryColorHex).copy(alpha = 0.85f),
                Color(0xFF100922),
                Color(reel.secondaryColorHex).copy(alpha = 0.5f),
                Color(0xFF090514)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .clickable { isPlaying = !isPlaying }
            .testTag("reel_item_${reel.id}")
    ) {
        // Stage Center Visualizer & Spotlight effect
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .height(260.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x33000000))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Performer Category Icon & Stage Mic
                Text(
                    text = reel.performerCategory.iconEmoji,
                    fontSize = 58.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = reel.songOrAct,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "LIVE PERFORMANCE REEL",
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                    color = StageGold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Real-time animated Audio Waveform
                AudioWaveformCanvas(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(36.dp),
                    barCount = 16,
                    color = if (isPlaying) StageGoldLight else StageTextMuted,
                    isPlaying = isPlaying
                )
            }

            // Pause Overlay Icon if user tapped to pause
            AnimatedVisibility(
                visible = !isPlaying,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0x99000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Right Action Rail (Heart, Comment, Tip/Gift, Share, Vinyl)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (reel.isLiked) StageCoral.copy(alpha = 0.25f) else Color(0x66000000))
                        .clickable { onToggleLike() }
                        .testTag("reel_like_btn_${reel.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (reel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like Reel",
                        tint = if (reel.isLiked) StageCoral else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${reel.likesCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comments Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0x66000000))
                        .clickable { onToggleLike() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${reel.commentsCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tip / Virtual Gift Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B1C05))
                        .border(1.dp, StageGold, CircleShape)
                        .clickable { onTipArtist() }
                        .testTag("reel_tip_btn_${reel.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌹", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Tip",
                    color = StageGoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Share Button
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0x66000000))
                    .clickable { onShareReel() }
                    .testTag("reel_share_btn_${reel.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Spinning Vinyl Disc
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1B1F))
                    .border(2.dp, StageGold.copy(alpha = 0.6f), CircleShape)
                    .rotate(if (isPlaying) rotation else 0f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(StageCoral)
                )
            }
        }

        // Bottom Details Overlay: Performer info, tagline, soundtrack, Book CTA
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 96.dp)
        ) {
            // Performer Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PerformerAvatar(
                    name = reel.performerName,
                    category = reel.performerCategory,
                    sizeDp = 42
                )

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = reel.performerName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = StageNeonCyan,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = reel.performerCategory.displayName,
                        fontSize = 11.sp,
                        color = StageGoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Reel Title & Tagline
            Text(
                text = reel.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (reel.tagline.isNotBlank()) {
                Text(
                    text = reel.tagline,
                    fontSize = 12.sp,
                    color = StageTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sound Track Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = StageGold,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = reel.soundTrack,
                    fontSize = 11.sp,
                    color = StageGoldLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick "Book This Performer" Button
            Button(
                onClick = onBookPerformer,
                colors = ButtonDefaults.buttonColors(containerColor = StageCoral),
                shape = RoundedCornerShape(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.testTag("reel_quick_book_btn_${reel.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Book for Your Event",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
