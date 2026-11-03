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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookingRequest
import com.example.model.BookingStatus
import com.example.model.ReviewItem
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BookingsDashboardScreen(
    viewModel: PerformerHubViewModel,
    onOpenChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookings by viewModel.bookings.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val isPerformerMode by viewModel.isPerformerMode.collectAsState()

    val totalEarnings = remember(bookings) {
        bookings.filter { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.COMPLETED }
            .sumOf { it.totalAmount }
    }
    val pendingAmount = remember(bookings) {
        bookings.filter { it.status == BookingStatus.PENDING }
            .sumOf { it.totalAmount }
    }

    val formattedTotal = NumberFormat.getNumberInstance(Locale("en", "IN")).format(totalEarnings)
    val formattedPending = NumberFormat.getNumberInstance(Locale("en", "IN")).format(pendingAmount)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
            .testTag("bookings_dashboard_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Studio Mode Tag
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isPerformerMode) "Performer Earnings Studio" else "Organizer Bookings Hub",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = StageTextPrimary
                        )
                        Text(
                            text = "Track your gig bookings, payouts and client reviews",
                            fontSize = 12.sp,
                            color = StageGoldLight
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { viewModel.openCreateProfileDialog() }
                            .testTag("dashboard_edit_profile_btn"),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2E174D),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StageNeonCyan)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = StageNeonCyan, modifier = Modifier.size(14.dp))
                            Text("Edit Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StageNeonCyan)
                        }
                    }
                }
            }
        }

        // Earnings Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Confirmed Earnings Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = StageGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Total Confirmed",
                                fontSize = 11.sp,
                                color = StageTextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹$formattedTotal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StageGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Payouts ready",
                            fontSize = 10.sp,
                            color = StageNeonGreen
                        )
                    }
                }

                // Pending Quotes Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassEmpty,
                                contentDescription = null,
                                tint = StageCoral,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Awaiting Action",
                                fontSize = 11.sp,
                                color = StageTextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹$formattedPending",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StageCoral
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${bookings.count { it.status == BookingStatus.PENDING }} request pending",
                            fontSize = 10.sp,
                            color = StageGoldLight
                        )
                    }
                }
            }
        }

        // Section: Bookings List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bookings & Inquiries (${bookings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StageTextPrimary
                )
                Text(
                    text = "Live Sync",
                    fontSize = 11.sp,
                    color = StageNeonCyan
                )
            }
        }

        items(bookings) { booking ->
            BookingCardItem(
                booking = booking,
                onAccept = { viewModel.updateBookingStatus(booking.id, BookingStatus.CONFIRMED) },
                onComplete = { viewModel.updateBookingStatus(booking.id, BookingStatus.COMPLETED) },
                onChat = onOpenChat
            )
        }

        // Section: Client Reviews & Ratings
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = StageGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ratings & Client Reviews",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                    }

                    Text(
                        text = "4.9 ★ Average",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StageGold
                    )
                }
            }
        }

        items(reviews) { review ->
            ReviewCardItem(review = review)
        }
    }
}

@Composable
fun BookingCardItem(
    booking: BookingRequest,
    onAccept: () -> Unit,
    onComplete: () -> Unit,
    onChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedAmount = remember(booking.totalAmount) {
        NumberFormat.getNumberInstance(Locale("en", "IN")).format(booking.totalAmount)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .testTag("booking_item_${booking.id}"),
        colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Event type & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = booking.eventType,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StageTextPrimary
                )

                val (badgeBg, badgeFg) = when (booking.status) {
                    BookingStatus.CONFIRMED -> Color(0xFF0F3B20) to StageNeonGreen
                    BookingStatus.PENDING -> Color(0xFF3D2700) to StageGold
                    BookingStatus.COMPLETED -> Color(0xFF1B2E4B) to StageNeonCyan
                    BookingStatus.CANCELLED -> Color(0xFF381525) to StageCoral
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = booking.status.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Performer / Organizer line
            Text(
                text = "Artist: ${booking.performerName} • Client: ${booking.organizerName}",
                fontSize = 12.sp,
                color = StageGoldLight,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Date, Time, Venue
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "📅 ${booking.eventDate}",
                    fontSize = 11.sp,
                    color = StageTextSecondary
                )
                Text(
                    text = "⏰ ${booking.eventTime}",
                    fontSize = 11.sp,
                    color = StageTextSecondary
                )
            }

            Text(
                text = "📍 ${booking.venueAddress}",
                fontSize = 11.sp,
                color = StageTextMuted,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (booking.specialRequirements.isNotBlank()) {
                Text(
                    text = "Note: ${booking.specialRequirements}",
                    fontSize = 11.sp,
                    color = StageTextSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amount & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Agreed Fee", fontSize = 10.sp, color = StageTextMuted)
                    Text(
                        text = "₹$formattedAmount",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StageGold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onChat,
                        colors = ButtonDefaults.buttonColors(containerColor = StageSurfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("chat_booking_btn_${booking.id}")
                    ) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(14.dp), tint = StageGold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Chat", fontSize = 11.sp, color = StageGold, fontWeight = FontWeight.Bold)
                    }

                    if (booking.status == BookingStatus.PENDING) {
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = StageNeonGreen),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("accept_booking_btn_${booking.id}")
                        ) {
                            Text("Accept", fontSize = 11.sp, color = Color(0xFF092910), fontWeight = FontWeight.Black)
                        }
                    } else if (booking.status == BookingStatus.CONFIRMED) {
                        Button(
                            onClick = onComplete,
                            colors = ButtonDefaults.buttonColors(containerColor = StageGold),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("complete_booking_btn_${booking.id}")
                        ) {
                            Text("Mark Done", fontSize = 11.sp, color = Color(0xFF1E1000), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewCardItem(
    review: ReviewItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = StageSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StageBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = review.clientName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = StageTextPrimary
                    )
                    Text(
                        text = review.eventType,
                        fontSize = 11.sp,
                        color = StageGoldLight
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StageGold,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment,
                fontSize = 12.sp,
                color = StageTextSecondary,
                lineHeight = 17.sp
            )

            Text(
                text = "Verified Review • ${review.date}",
                fontSize = 10.sp,
                color = StageTextMuted,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
