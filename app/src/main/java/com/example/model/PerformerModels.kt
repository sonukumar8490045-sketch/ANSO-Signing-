package com.example.model

enum class PerformerCategory(val displayName: String, val iconEmoji: String) {
    ALL("All Stage", "🌟"),
    SINGER("Singer & Vocalist", "🎤"),
    DANCER("Dancer & Troupe", "💃"),
    COMEDIAN("Stand-up Comedy", "🎭"),
    DJ("DJ & Music Producer", "🎧"),
    MUSICIAN("Instrumentalist", "🎸"),
    MAGICIAN("Illusionist & Mentalist", "✨"),
    ACTOR("Actor & Theatre", "🎬"),
    ANCHOR("Anchor & Emcee", "🎙️")
}

data class TalentReel(
    val id: String,
    val performerId: String,
    val performerName: String,
    val performerCategory: PerformerCategory,
    val title: String,
    val songOrAct: String,
    val durationText: String,
    val likesCount: Int,
    val commentsCount: Int,
    val viewsCount: String,
    val isLiked: Boolean = false,
    val tagline: String = "",
    val soundTrack: String = "Original Sound - Live Stage",
    val primaryColorHex: Long = 0xFF6B21A8,
    val secondaryColorHex: Long = 0xFFEC4899
)

data class Performer(
    val id: String,
    val name: String,
    val stageName: String,
    val category: PerformerCategory,
    val tagline: String,
    val bio: String,
    val location: String,
    val rating: Double,
    val reviewCount: Int,
    val startingPrice: Long,
    val hourlyRate: Long,
    val isVerified: Boolean = true,
    val isAvailable: Boolean = true,
    val skills: List<String>,
    val languages: List<String>,
    val badges: List<String>,
    val showsCompleted: Int,
    val reels: List<TalentReel> = emptyList(),
    val isSaved: Boolean = false
)

enum class BookingStatus(val label: String) {
    PENDING("Awaiting Confirmation"),
    CONFIRMED("Booking Confirmed 🎉"),
    COMPLETED("Completed & Paid 💰"),
    CANCELLED("Cancelled")
}

data class BookingRequest(
    val id: String,
    val performerId: String,
    val performerName: String,
    val organizerName: String,
    val organizerContact: String,
    val eventType: String,
    val eventDate: String,
    val eventTime: String,
    val venueAddress: String,
    val durationHours: Int,
    val totalAmount: Long,
    val status: BookingStatus,
    val specialRequirements: String,
    val createdAt: String
)

data class EventGig(
    val id: String,
    val title: String,
    val organizerName: String,
    val eventType: String,
    val city: String,
    val venue: String,
    val date: String,
    val budgetRange: String,
    val description: String,
    val requiredCategory: PerformerCategory,
    val applicantsCount: Int,
    val isApplied: Boolean = false,
    val tag: String = "High Demand"
)

data class ReviewItem(
    val id: String,
    val performerId: String,
    val clientName: String,
    val eventType: String,
    val rating: Int,
    val date: String,
    val comment: String
)

data class LiveChatMessage(
    val id: String,
    val user: String,
    val message: String,
    val isGift: Boolean = false,
    val giftEmoji: String = "",
    val timestamp: String = "Just now"
)

data class LiveStageSession(
    val id: String,
    val performerId: String,
    val performerName: String,
    val performerStageName: String,
    val category: PerformerCategory,
    val streamTitle: String,
    val currentViewers: Int,
    val totalTipsGold: Long,
    val messages: List<LiveChatMessage>,
    val isStreaming: Boolean = true
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val isFromPerformer: Boolean,
    val text: String,
    val time: String,
    val isQuoteCard: Boolean = false,
    val quoteAmount: Long? = null
)
