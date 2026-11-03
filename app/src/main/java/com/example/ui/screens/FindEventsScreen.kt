package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EventGig
import com.example.model.PerformerCategory
import com.example.ui.PerformerHubViewModel
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

@Composable
fun FindEventsScreen(
    viewModel: PerformerHubViewModel,
    modifier: Modifier = Modifier
) {
    val gigs by viewModel.gigs.collectAsState()
    var selectedFilterCategory by remember { mutableStateOf<PerformerCategory?>(null) }

    val filteredGigs = remember(gigs, selectedFilterCategory) {
        if (selectedFilterCategory == null) gigs
        else gigs.filter { it.requiredCategory == selectedFilterCategory }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("find_events_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Gigs & Show Board",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = StageTextPrimary
                            )
                            Text(
                                text = "Paid performance opportunities from verified organizers",
                                fontSize = 12.sp,
                                color = StageGoldLight
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF201336),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "${gigs.size} Active Gigs",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StageGold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Category Filter Pills
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val filterOptions = listOf(
                        null to "All Opportunities",
                        PerformerCategory.SINGER to "Singers",
                        PerformerCategory.COMEDIAN to "Comedy",
                        PerformerCategory.DJ to "DJs",
                        PerformerCategory.DANCER to "Dancers"
                    )

                    filterOptions.forEach { (cat, label) ->
                        val isSelected = selectedFilterCategory == cat
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedFilterCategory = cat }
                                .testTag("gig_filter_${label.lowercase()}"),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) StageGold else StageSurfaceVariant,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF1E1000) else StageTextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // List of Gig Cards
            items(filteredGigs) { gig ->
                GigCard(
                    gig = gig,
                    onApply = { viewModel.applyToGig(gig.id) }
                )
            }
        }

        // Floating Action Button: Post a Gig
        FloatingActionButton(
            onClick = { viewModel.openPostGigDialog() },
            containerColor = StageCoral,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 100.dp)
                .testTag("fab_post_gig")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Post a Gig")
                Text("Post Gig", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun GigCard(
    gig: EventGig,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .testTag("gig_card_${gig.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tag & Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF381525)
                ) {
                    Text(
                        text = gig.tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = StageCoral,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StageSurfaceVariant
                ) {
                    Text(
                        text = "${gig.requiredCategory.iconEmoji} ${gig.requiredCategory.displayName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StageGoldLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = gig.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StageTextPrimary
            )

            // Organizer & Event Type
            Text(
                text = "By ${gig.organizerName} • ${gig.eventType}",
                fontSize = 12.sp,
                color = StageTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = gig.description,
                fontSize = 12.sp,
                color = StageTextMuted,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Details row: City/Venue, Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = StageNeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${gig.city}",
                        fontSize = 11.sp,
                        color = StageTextSecondary,
                        maxLines = 1
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = StageGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = gig.date,
                        fontSize = 11.sp,
                        color = StageTextSecondary,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom action row: Budget & Apply button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Budget Offered",
                        fontSize = 10.sp,
                        color = StageTextMuted
                    )
                    Text(
                        text = gig.budgetRange,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StageNeonGreen
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = StageTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${gig.applicantsCount} applied",
                            fontSize = 11.sp,
                            color = StageTextMuted
                        )
                    }

                    Button(
                        onClick = onApply,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (gig.isApplied) StageNeonGreen else StageGold,
                            contentColor = Color(0xFF1E1000)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("apply_gig_btn_${gig.id}")
                    ) {
                        if (gig.isApplied) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pitch Sent", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Apply & Pitch", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
