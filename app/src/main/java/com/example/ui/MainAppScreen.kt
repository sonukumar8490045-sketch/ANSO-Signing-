package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.dialogs.BookingDialogSheet
import com.example.ui.dialogs.CreateProfileDialog
import com.example.ui.dialogs.PostGigDialog
import com.example.ui.screens.BookingsDashboardScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FindEventsScreen
import com.example.ui.screens.LiveStageScreen
import com.example.ui.screens.PerformerDetailScreen
import com.example.ui.screens.ReelsScreen
import com.example.ui.theme.StageBorder
import com.example.ui.theme.StageCoral
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageSurfaceCard
import com.example.ui.theme.StageSurfaceDark
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary
import kotlinx.coroutines.delay

@Composable
fun MainAppScreen(
    viewModel: PerformerHubViewModel = viewModel()
) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val selectedPerformer by viewModel.selectedPerformer.collectAsState()
    val activeChatPerformer by viewModel.activeChatPerformer.collectAsState()
    val bookingPerformer by viewModel.bookingPerformer.collectAsState()
    val showCreateProfileDialog by viewModel.showCreateProfileDialog.collectAsState()
    val showPostGigDialog by viewModel.showPostGigDialog.collectAsState()
    val userFeedbackMessage by viewModel.userFeedbackMessage.collectAsState()

    // Auto dismiss feedback message after 3 seconds
    LaunchedEffect(userFeedbackMessage) {
        if (userFeedbackMessage != null) {
            delay(3000)
            viewModel.clearMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageSurfaceDark)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = StageSurfaceDark,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                // Hide bottom nav if viewing full sub-screen detail or chat
                if (selectedPerformer == null && activeChatPerformer == null) {
                    NavigationBar(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("main_bottom_nav"),
                        containerColor = StageSurfaceCard,
                        tonalElevation = 8.dp
                    ) {
                        val navItems = listOf(
                            AppNavDestination.EXPLORE to Icons.Default.Explore,
                            AppNavDestination.REELS to Icons.Default.PlayCircle,
                            AppNavDestination.LIVE_STAGE to Icons.Default.Podcasts,
                            AppNavDestination.FIND_EVENTS to Icons.Default.CalendarMonth,
                            AppNavDestination.DASHBOARD to Icons.Default.AccountBalanceWallet
                        )

                        navItems.forEach { (dest, icon) ->
                            val isSelected = currentDestination == dest
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(dest) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = dest.label,
                                        tint = if (isSelected) StageGold else StageTextMuted
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) StageGold else StageTextMuted
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = StageGold,
                                    unselectedIconColor = StageTextMuted,
                                    selectedTextColor = StageGold,
                                    unselectedTextColor = StageTextMuted,
                                    indicatorColor = Color(0xFF382305)
                                ),
                                modifier = Modifier.testTag("nav_item_${dest.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // If Chat is active, show ChatScreen
                if (activeChatPerformer != null) {
                    ChatScreen(
                        performer = activeChatPerformer!!,
                        viewModel = viewModel,
                        onBack = { viewModel.closeChat() }
                    )
                }
                // Else If Performer Detail is active, show PerformerDetailScreen
                else if (selectedPerformer != null) {
                    PerformerDetailScreen(
                        performer = selectedPerformer!!,
                        viewModel = viewModel,
                        onBack = { viewModel.viewPerformerDetail(null) },
                        onBookNow = { viewModel.openBookingSheet(selectedPerformer) },
                        onOpenChat = { viewModel.openChatWith(selectedPerformer) }
                    )
                }
                // Else show Main Destination Screens
                else {
                    when (currentDestination) {
                        AppNavDestination.EXPLORE -> {
                            ExploreScreen(
                                viewModel = viewModel,
                                onViewDetail = { viewModel.viewPerformerDetail(it) },
                                onBookPerformer = { viewModel.openBookingSheet(it) }
                            )
                        }
                        AppNavDestination.REELS -> {
                            ReelsScreen(
                                viewModel = viewModel,
                                onBookPerformer = { viewModel.openBookingSheet(it) }
                            )
                        }
                        AppNavDestination.LIVE_STAGE -> {
                            LiveStageScreen(
                                viewModel = viewModel,
                                onBookPerformer = { viewModel.openBookingSheet(it) }
                            )
                        }
                        AppNavDestination.FIND_EVENTS -> {
                            FindEventsScreen(viewModel = viewModel)
                        }
                        AppNavDestination.DASHBOARD -> {
                            BookingsDashboardScreen(
                                viewModel = viewModel,
                                onOpenChat = {
                                    val topPerf = viewModel.repository.performers.value.firstOrNull()
                                    viewModel.openChatWith(topPerf)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Booking Modal Bottom Sheet
        if (bookingPerformer != null) {
            BookingDialogSheet(
                performer = bookingPerformer!!,
                onDismiss = { viewModel.closeBookingSheet() },
                onConfirmBooking = { name, phone, type, date, time, venue, duration, amount, notes ->
                    viewModel.bookPerformer(
                        performer = bookingPerformer!!,
                        organizerName = name,
                        organizerContact = phone,
                        eventType = type,
                        date = date,
                        time = time,
                        venue = venue,
                        durationHours = duration,
                        totalAmount = amount,
                        notes = notes
                    )
                }
            )
        }

        // Create Profile Modal Dialog
        if (showCreateProfileDialog) {
            CreateProfileDialog(
                onDismiss = { viewModel.closeCreateProfileDialog() },
                onSubmitProfile = { name, stageName, cat, tagline, bio, loc, startPrice, hourly, skills, langs ->
                    viewModel.createPerformerProfile(
                        name, stageName, cat, tagline, bio, loc, startPrice, hourly, skills, langs
                    )
                }
            )
        }

        // Post Gig Modal Dialog
        if (showPostGigDialog) {
            PostGigDialog(
                onDismiss = { viewModel.closePostGigDialog() },
                onSubmitGig = { title, organizer, cat, eventType, city, venue, date, budget, desc ->
                    viewModel.postGig(title, organizer, cat, eventType, city, venue, date, budget, desc)
                }
            )
        }

        // Floating Toast / Feedback Banner
        AnimatedVisibility(
            visible = userFeedbackMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xF2201336),
                border = androidx.compose.foundation.BorderStroke(1.dp, StageGold),
                shadowElevation = 10.dp
            ) {
                Text(
                    text = userFeedbackMessage ?: "",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = StageGoldLight,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                )
            }
        }
    }
}
