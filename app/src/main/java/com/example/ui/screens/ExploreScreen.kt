package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Performer
import com.example.ui.PerformerHubViewModel
import com.example.ui.components.PerformerAvatar
import com.example.ui.components.PerformerCard
import com.example.ui.components.StageCategoryChipsRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: PerformerHubViewModel,
    onViewDetail: (Performer) -> Unit,
    onBookPerformer: (Performer) -> Unit,
    modifier: Modifier = Modifier
) {
    val performers by viewModel.filteredPerformers.collectAsState()
    val allPerformers by viewModel.repository.performers.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isPerformerMode by viewModel.isPerformerMode.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("explore_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // App Header: Branding + Mode Toggle
        item {
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
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Performer",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = StageTextPrimary
                            )
                            Text(
                                text = "Hub",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = StageGold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StageCoral
                            ) {
                                Text(
                                    text = "LIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Talent Aapka, Stage Hamara!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = StageGoldLight
                        )
                    }

                    // Mode Switcher Button (Organizer / Performer Studio)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { viewModel.toggleAppMode() }
                            .testTag("mode_toggle_btn"),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isPerformerMode) Color(0xFF381A5E) else Color(0xFF281C06),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isPerformerMode) StageNeonCyan else StageGold
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Mode",
                                tint = if (isPerformerMode) StageNeonCyan else StageGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isPerformerMode) "Performer Studio" else "Organizer View",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPerformerMode) StageNeonCyan else StageGold
                            )
                        }
                    }
                }
            }
        }

        // Hero Stage Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("hero_stage_banner")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.performer_hero_stage_1793680862484),
                    contentDescription = "Concert Stage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC0D0818), Color(0xF20D0818)),
                                startY = 50f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StageGold.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "PREMIER TALENT MARKETPLACE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1E1000),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Book World-Class Artists for Your Events",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Singers, Dancers, Comedians, DJs & Magicians ready for stage.",
                        fontSize = 11.sp,
                        color = StageTextSecondary,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.openPostGigDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = StageCoral),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("hero_post_gig_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Post a Gig", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.openCreateProfileDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = StageSurfaceVariant),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("hero_join_performer_btn")
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = StageGold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Join as Performer", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = StageGold)
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_performer_input"),
                    placeholder = {
                        Text(
                            "Search singers, stand-up, DJs, cities...",
                            color = StageTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = StageGold
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = StageTextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = StageSurfaceCard,
                        unfocusedContainerColor = StageSurfaceCard,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder,
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary
                    )
                )
            }
        }

        // Category Filter Chips Row
        item {
            StageCategoryChipsRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )
        }

        // Spotlight Artists Carousel (Horizontal)
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = StageGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Spotlight Performers",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                    }
                    Text(
                        text = "Verified Elite",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StageNeonGreen
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allPerformers.take(5)) { performer ->
                        SpotlightPerformerItem(
                            performer = performer,
                            onClick = { onViewDetail(performer) }
                        )
                    }
                }
            }
        }

        // Trending Talent Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Trending Talent (${performers.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StageTextPrimary
                )
                Text(
                    text = "Available Now",
                    fontSize = 12.sp,
                    color = StageGoldLight
                )
            }
        }

        // Empty state if search has no results
        if (performers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🎭", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No performers match your search",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing filters or search for another talent category.",
                            fontSize = 12.sp,
                            color = StageTextMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                viewModel.updateSearchQuery("")
                                viewModel.selectCategory(com.example.model.PerformerCategory.ALL)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StageGold)
                        ) {
                            Text("Reset Filters", color = Color(0xFF1E1000), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(performers) { performer ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    PerformerCard(
                        performer = performer,
                        onViewDetail = { onViewDetail(performer) },
                        onBookNow = { onBookPerformer(performer) },
                        onSaveToggle = { viewModel.toggleSavePerformer(performer.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SpotlightPerformerItem(
    performer: Performer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("spotlight_item_${performer.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PerformerAvatar(
                name = performer.name,
                category = performer.category,
                sizeDp = 52
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = performer.stageName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = StageTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = performer.category.displayName.split(" ").first(),
                fontSize = 11.sp,
                color = StageGoldLight,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF281C06)
            ) {
                Text(
                    text = "★ ${performer.rating}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = StageGold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
