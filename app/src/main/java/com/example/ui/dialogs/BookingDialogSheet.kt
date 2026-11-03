package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.model.Performer
import com.example.ui.components.PerformerAvatar
import com.example.ui.theme.StageBorder
import com.example.ui.theme.StageCoral
import com.example.ui.theme.StageGold
import com.example.ui.theme.StageGoldLight
import com.example.ui.theme.StageNeonGreen
import com.example.ui.theme.StageSurfaceCard
import com.example.ui.theme.StageSurfaceDark
import com.example.ui.theme.StageSurfaceVariant
import com.example.ui.theme.StageTextMuted
import com.example.ui.theme.StageTextPrimary
import com.example.ui.theme.StageTextSecondary
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDialogSheet(
    performer: Performer,
    onDismiss: () -> Unit,
    onConfirmBooking: (
        organizerName: String,
        organizerContact: String,
        eventType: String,
        date: String,
        time: String,
        venue: String,
        durationHours: Int,
        totalAmount: Long,
        notes: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var organizerName by remember { mutableStateOf("Rohit Sharma") }
    var organizerContact by remember { mutableStateOf("+91 98201 23456") }
    var selectedEventType by remember { mutableStateOf("Shaadi / Wedding Sangeet") }
    var eventDate by remember { mutableStateOf("Dec 22, 2026") }
    var eventTime by remember { mutableStateOf("7:30 PM - 10:30 PM") }
    var venueAddress by remember { mutableStateOf("Taj Lands End, Bandra, Mumbai") }
    var durationHours by remember { mutableFloatStateOf(2.5f) }
    var specialNotes by remember { mutableStateOf("Need acoustic entry song and high-energy crowd favorite finale!") }

    val calculatedTotal = remember(durationHours, performer.hourlyRate, performer.startingPrice) {
        val hours = durationHours.coerceAtLeast(1f)
        val fee = (hours * performer.hourlyRate).toLong()
        fee.coerceAtLeast(performer.startingPrice)
    }

    val formattedTotal = NumberFormat.getNumberInstance(Locale("en", "IN")).format(calculatedTotal)

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
                .testTag("booking_bottom_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PerformerAvatar(name = performer.name, category = performer.category, sizeDp = 44)
                    Column {
                        Text(
                            text = "Book ${performer.stageName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StageTextPrimary
                        )
                        Text(
                            text = "${performer.category.displayName} • Starting at ₹${performer.startingPrice}",
                            fontSize = 11.sp,
                            color = StageGoldLight
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = StageTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Event Type selection
            Text(
                text = "Select Event Occasion",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = StageTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            val eventTypes = listOf(
                "Shaadi / Wedding Sangeet",
                "Corporate Gala",
                "College Fest / Concert",
                "Birthday / Anniversary",
                "Club / Lounge Night"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                eventTypes.take(2).forEach { type ->
                    val isSelected = selectedEventType == type
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedEventType = type },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) StageGold else StageSurfaceVariant
                    ) {
                        Text(
                            text = type,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF1E1000) else StageTextPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Organizer Name & Phone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = organizerName,
                    onValueChange = { organizerName = it },
                    label = { Text("Your Name", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = organizerContact,
                    onValueChange = { organizerContact = it },
                    label = { Text("Phone Number", fontSize = 11.sp) },
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

            // Date & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("Event Date", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = StageTextPrimary,
                        unfocusedTextColor = StageTextPrimary,
                        focusedBorderColor = StageGold,
                        unfocusedBorderColor = StageBorder
                    )
                )

                OutlinedTextField(
                    value = eventTime,
                    onValueChange = { eventTime = it },
                    label = { Text("Show Slot Time", fontSize = 11.sp) },
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

            // Venue
            OutlinedTextField(
                value = venueAddress,
                onValueChange = { venueAddress = it },
                label = { Text("Venue & City", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Duration Slider & Auto Price Calculation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Performance Duration: ${String.format(Locale.US, "%.1f", durationHours)} Hours",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StageTextPrimary
                )
                Text(
                    text = "Rate: ₹${performer.hourlyRate}/hr",
                    fontSize = 11.sp,
                    color = StageGoldLight
                )
            }

            Slider(
                value = durationHours,
                onValueChange = { durationHours = it },
                valueRange = 1f..6f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = StageGold,
                    activeTrackColor = StageGold,
                    inactiveTrackColor = StageBorder
                ),
                modifier = Modifier.testTag("booking_duration_slider")
            )

            // Special Notes
            OutlinedTextField(
                value = specialNotes,
                onValueChange = { specialNotes = it },
                label = { Text("Special Requests / Song / Stage Notes", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = StageTextPrimary,
                    unfocusedTextColor = StageTextPrimary,
                    focusedBorderColor = StageGold,
                    unfocusedBorderColor = StageBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estimated Total & Confirm Button
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF281C06),
                border = androidx.compose.foundation.BorderStroke(1.dp, StageGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Total Booking Fee", fontSize = 11.sp, color = StageTextMuted)
                        Text(
                            text = "₹$formattedTotal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = StageGold
                        )
                    }

                    Button(
                        onClick = {
                            onConfirmBooking(
                                organizerName,
                                organizerContact,
                                selectedEventType,
                                eventDate,
                                eventTime,
                                venueAddress,
                                durationHours.toInt(),
                                calculatedTotal,
                                specialNotes
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StageCoral),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("confirm_booking_btn")
                    ) {
                        Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Confirm & Book", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
