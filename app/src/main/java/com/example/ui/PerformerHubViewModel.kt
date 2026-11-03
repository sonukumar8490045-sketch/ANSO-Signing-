package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PerformerHubRepository
import com.example.model.BookingRequest
import com.example.model.BookingStatus
import com.example.model.ChatMessage
import com.example.model.EventGig
import com.example.model.LiveStageSession
import com.example.model.Performer
import com.example.model.PerformerCategory
import com.example.model.ReviewItem
import com.example.model.TalentReel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppNavDestination(val label: String, val iconName: String) {
    EXPLORE("Explore", "Search"),
    REELS("Talent Reels", "Play"),
    LIVE_STAGE("Live Stage", "Radio"),
    FIND_EVENTS("Find Gigs", "Calendar"),
    DASHBOARD("Dashboard", "Wallet")
}

class PerformerHubViewModel(
    val repository: PerformerHubRepository = PerformerHubRepository()
) : ViewModel() {

    private val _currentDestination = MutableStateFlow(AppNavDestination.EXPLORE)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

    private val _selectedCategory = MutableStateFlow(PerformerCategory.ALL)
    val selectedCategory: StateFlow<PerformerCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPerformer = MutableStateFlow<Performer?>(null)
    val selectedPerformer: StateFlow<Performer?> = _selectedPerformer.asStateFlow()

    private val _bookingPerformer = MutableStateFlow<Performer?>(null)
    val bookingPerformer: StateFlow<Performer?> = _bookingPerformer.asStateFlow()

    private val _activeChatPerformer = MutableStateFlow<Performer?>(null)
    val activeChatPerformer: StateFlow<Performer?> = _activeChatPerformer.asStateFlow()

    private val _showCreateProfileDialog = MutableStateFlow(false)
    val showCreateProfileDialog: StateFlow<Boolean> = _showCreateProfileDialog.asStateFlow()

    private val _showPostGigDialog = MutableStateFlow(false)
    val showPostGigDialog: StateFlow<Boolean> = _showPostGigDialog.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    val isPerformerMode: StateFlow<Boolean> = repository.isPerformerMode
    val gigs: StateFlow<List<EventGig>> = repository.gigs
    val bookings: StateFlow<List<BookingRequest>> = repository.bookings
    val reviews: StateFlow<List<ReviewItem>> = repository.reviews
    val liveStreams: StateFlow<List<LiveStageSession>> = repository.liveStreams
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages

    val allReels: StateFlow<List<TalentReel>> = repository.performers.combine(_selectedCategory) { perfList, category ->
        val reels = perfList.flatMap { it.reels }
        if (category == PerformerCategory.ALL) reels
        else reels.filter { it.performerCategory == category }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredPerformers: StateFlow<List<Performer>> = combine(
        repository.performers,
        _selectedCategory,
        _searchQuery
    ) { list, category, query ->
        list.filter { p ->
            val matchCategory = (category == PerformerCategory.ALL || p.category == category)
            val matchQuery = query.isBlank() ||
                    p.name.contains(query, ignoreCase = true) ||
                    p.stageName.contains(query, ignoreCase = true) ||
                    p.location.contains(query, ignoreCase = true) ||
                    p.skills.any { it.contains(query, ignoreCase = true) }
            matchCategory && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(destination: AppNavDestination) {
        _currentDestination.value = destination
    }

    fun selectCategory(category: PerformerCategory) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun viewPerformerDetail(performer: Performer?) {
        _selectedPerformer.value = performer
    }

    fun openBookingSheet(performer: Performer?) {
        _bookingPerformer.value = performer
    }

    fun closeBookingSheet() {
        _bookingPerformer.value = null
    }

    fun openChatWith(performer: Performer?) {
        _activeChatPerformer.value = performer
    }

    fun closeChat() {
        _activeChatPerformer.value = null
    }

    fun openCreateProfileDialog() {
        _showCreateProfileDialog.value = true
    }

    fun closeCreateProfileDialog() {
        _showCreateProfileDialog.value = false
    }

    fun openPostGigDialog() {
        _showPostGigDialog.value = true
    }

    fun closePostGigDialog() {
        _showPostGigDialog.value = false
    }

    fun toggleAppMode() {
        repository.toggleAppMode()
        val currentMode = repository.isPerformerMode.value
        showMessage(if (currentMode) "Switched to Performer Studio 🎭" else "Switched to Event Organizer View 🎟️")
    }

    fun toggleLikeReel(reelId: String) {
        repository.toggleLikeReel(reelId)
    }

    fun toggleSavePerformer(performerId: String) {
        repository.toggleSavePerformer(performerId)
        showMessage("Saved to your shortlist! ❤️")
    }

    fun applyToGig(gigId: String) {
        repository.applyToGig(gigId)
        showMessage("Pitch submitted successfully to the organizer! 🚀")
    }

    fun postGig(
        title: String,
        organizer: String,
        category: PerformerCategory,
        eventType: String,
        city: String,
        venue: String,
        date: String,
        budget: String,
        desc: String
    ) {
        repository.postNewGig(title, organizer, category, eventType, city, venue, date, budget, desc)
        _showPostGigDialog.value = false
        showMessage("Gig requirement posted! Performers can now apply. ✨")
    }

    fun bookPerformer(
        performer: Performer,
        organizerName: String,
        organizerContact: String,
        eventType: String,
        date: String,
        time: String,
        venue: String,
        durationHours: Int,
        totalAmount: Long,
        notes: String
    ) {
        repository.submitBookingRequest(
            performerId = performer.id,
            performerName = performer.stageName,
            organizerName = organizerName,
            organizerContact = organizerContact,
            eventType = eventType,
            date = date,
            time = time,
            venue = venue,
            durationHours = durationHours,
            totalAmount = totalAmount,
            notes = notes
        )
        _bookingPerformer.value = null
        showMessage("Booking request sent to ${performer.stageName}! 🎉")
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        repository.updateBookingStatus(bookingId, status)
        showMessage("Booking status updated: ${status.label}")
    }

    fun sendLiveComment(streamId: String, user: String, text: String, giftEmoji: String = "", isGift: Boolean = false) {
        repository.addLiveComment(streamId, user, text, giftEmoji, isGift)
        if (isGift) {
            showMessage("Gift sent! Stage energy boosted! $giftEmoji")
        }
    }

    fun sendChatMessage(text: String, isFromPerformer: Boolean = false) {
        repository.sendChatMessage(text, isFromPerformer)
    }

    fun createPerformerProfile(
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
    ) {
        val created = repository.addPerformerProfile(
            name, stageName, category, tagline, bio, location, startingPrice, hourlyRate, skills, languages
        )
        _showCreateProfileDialog.value = false
        _selectedPerformer.value = created
        showMessage("Congratulations! Your Performer Profile is live on stage! 🌟")
    }

    fun addReview(performerId: String, clientName: String, eventType: String, rating: Int, comment: String) {
        repository.addReview(performerId, clientName, eventType, rating, comment)
        showMessage("Review submitted! Thank you for supporting the artist. ⭐")
    }

    fun showMessage(msg: String) {
        _userFeedbackMessage.value = msg
    }

    fun clearMessage() {
        _userFeedbackMessage.value = null
    }
}
