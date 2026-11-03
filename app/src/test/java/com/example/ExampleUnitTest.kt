package com.example

import com.example.data.PerformerHubRepository
import com.example.model.BookingStatus
import com.example.model.PerformerCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun repository_initialPerformersAndGigs_areLoaded() {
        val repo = PerformerHubRepository()
        val performers = repo.performers.value
        assertTrue(performers.isNotEmpty())
        assertTrue(repo.gigs.value.isNotEmpty())
        val kabir = performers.find { it.stageName == "Kabir Live" }
        assertNotNull(kabir)
        assertEquals(PerformerCategory.SINGER, kabir?.category)
    }

    @Test
    fun bookingFlow_createsPendingBooking() {
        val repo = PerformerHubRepository()
        val booking = repo.submitBookingRequest(
            performerId = "perf_1",
            performerName = "Kabir Live",
            organizerName = "Test Client",
            organizerContact = "+91 99999 88888",
            eventType = "Sangeet",
            date = "Dec 25, 2026",
            time = "8:00 PM",
            venue = "Delhi",
            durationHours = 3,
            totalAmount = 36000,
            notes = "Test Note"
        )
        assertEquals(BookingStatus.PENDING, booking.status)
        assertTrue(repo.bookings.value.any { it.id == booking.id })
    }

    @Test
    fun reelLikeToggle_updatesCountAndState() {
        val repo = PerformerHubRepository()
        val firstReel = repo.performers.value.flatMap { it.reels }.first()
        val initialLikes = firstReel.likesCount
        val initialLiked = firstReel.isLiked

        repo.toggleLikeReel(firstReel.id)
        val updatedReel = repo.performers.value.flatMap { it.reels }.find { it.id == firstReel.id }
        assertNotNull(updatedReel)
        assertEquals(!initialLiked, updatedReel?.isLiked)
        assertEquals(if (initialLiked) initialLikes - 1 else initialLikes + 1, updatedReel?.likesCount)
    }

    @Test
    fun gigApplication_updatesApplicantCount() {
        val repo = PerformerHubRepository()
        val gig = repo.gigs.value.first()
        val initialApplicants = gig.applicantsCount
        repo.applyToGig(gig.id)
        val updatedGig = repo.gigs.value.find { it.id == gig.id }
        assertEquals(initialApplicants + 1, updatedGig?.applicantsCount)
        assertTrue(updatedGig?.isApplied == true)
    }
}
