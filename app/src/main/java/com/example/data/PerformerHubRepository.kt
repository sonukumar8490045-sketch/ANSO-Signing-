package com.example.data

import com.example.model.BookingRequest
import com.example.model.BookingStatus
import com.example.model.ChatMessage
import com.example.model.EventGig
import com.example.model.LiveChatMessage
import com.example.model.LiveStageSession
import com.example.model.Performer
import com.example.model.PerformerCategory
import com.example.model.ReviewItem
import com.example.model.TalentReel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class PerformerHubRepository {

    private val initialPerformers = listOf(
        Performer(
            id = "perf_1",
            name = "Kabir Verma",
            stageName = "Kabir Live",
            category = PerformerCategory.SINGER,
            tagline = "Sufi & Bollywood Soul Stirring Singer 🎸",
            bio = "Professional playback & stage singer with 8+ years performing at grand wedding sangeets, concerts, and luxury corporate galas across India and Dubai. Known for heartfelt Sufi vocals, Bollywood acoustic sets, and unplugged melodies that get everyone singing along.",
            location = "Mumbai, Maharashtra",
            rating = 4.9,
            reviewCount = 186,
            startingPrice = 25000,
            hourlyRate = 12000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Bollywood Classics", "Sufi Unplugged", "Acoustic Guitar", "Punjabi Pop", "Semi-Classical"),
            languages = listOf("Hindi", "English", "Punjabi", "Urdu"),
            badges = listOf("Trending #1", "Top Rated", "Celebrity Pick"),
            showsCompleted = 142,
            reels = listOf(
                TalentReel(
                    id = "reel_1",
                    performerId = "perf_1",
                    performerName = "Kabir Live",
                    performerCategory = PerformerCategory.SINGER,
                    title = "Tum Hi Ho & Raabta Acoustic Mashup",
                    songOrAct = "Raabta / Tum Hi Ho",
                    durationText = "0:45",
                    likesCount = 2840,
                    commentsCount = 312,
                    viewsCount = "64.2K",
                    isLiked = false,
                    tagline = "Live at Udaipur Palace Sangeet Night ✨",
                    soundTrack = "Kabir Live Unplugged - Acoustic Guitar",
                    primaryColorHex = 0xFF581C87,
                    secondaryColorHex = 0xFFD946EF
                ),
                TalentReel(
                    id = "reel_1_b",
                    performerId = "perf_1",
                    performerName = "Kabir Live",
                    performerCategory = PerformerCategory.SINGER,
                    title = "O Re Piya (Sufi Live Improvisation)",
                    songOrAct = "O Re Piya",
                    durationText = "0:52",
                    likesCount = 4120,
                    commentsCount = 480,
                    viewsCount = "92.0K",
                    isLiked = true,
                    tagline = "Pure goosebumps during Jaipur Royal Courtyard concert 🌙",
                    soundTrack = "Sufi Soul Beats",
                    primaryColorHex = 0xFF1E1B4B,
                    secondaryColorHex = 0xFF818CF8
                )
            )
        ),
        Performer(
            id = "perf_2",
            name = "Aarav Sharma",
            stageName = "Aarav Laughs",
            category = PerformerCategory.COMEDIAN,
            tagline = "Clean Family & Corporate Stand-up Comedian 🎭",
            bio = "Featured on top YouTube comedy channels with over 15M views. Specializes in sharp, clean, relatable comedy covering Indian families, wedding drama, corporate IT life, and modern relationships. 100% crowd laughter guaranteed!",
            location = "Delhi NCR",
            rating = 4.8,
            reviewCount = 94,
            startingPrice = 35000,
            hourlyRate = 18000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Observational Comedy", "Crowd Work", "Clean Corporate Humor", "Wedding Roasts", "Hindi Satire"),
            languages = listOf("Hindi", "English"),
            badges = listOf("Viral Comic", "Verified Pro"),
            showsCompleted = 98,
            reels = listOf(
                TalentReel(
                    id = "reel_2",
                    performerId = "perf_2",
                    performerName = "Aarav Laughs",
                    performerCategory = PerformerCategory.COMEDIAN,
                    title = "When Sharma Ji goes to an Indian Buffet",
                    songOrAct = "Desi Buffet Logic",
                    durationText = "0:58",
                    likesCount = 9850,
                    commentsCount = 890,
                    viewsCount = "240K",
                    isLiked = false,
                    tagline = "Plate balance karna ek art hai bhai 😂",
                    soundTrack = "Live Crowd Laughter & Mic FX",
                    primaryColorHex = 0xFF831843,
                    secondaryColorHex = 0xFFFB7185
                )
            )
        ),
        Performer(
            id = "perf_3",
            name = "Natya Rhythm Crew",
            stageName = "Team Desi Grooves",
            category = PerformerCategory.DANCER,
            tagline = "High Voltage Bollywood & Folk Fusion Troupe 💃",
            bio = "A 6-member explosive dance crew trained in contemporary, Bollywood, Bhangra, and lyrical hip-hop. Champions of multiple national reality dance battles. Available for wedding sangeet entry acts, corporate launches, and live concert backing.",
            location = "Bengaluru, Karnataka",
            rating = 4.9,
            reviewCount = 210,
            startingPrice = 45000,
            hourlyRate = 22000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Bollywood Flashmobs", "Bhangra Energy", "Sangeet Choreography", "Acrobatic Lifts", "Theme Entry"),
            languages = listOf("Hindi", "English", "Kannada"),
            badges = listOf("Dance Champions", "Top Sangeet Choice"),
            showsCompleted = 215,
            reels = listOf(
                TalentReel(
                    id = "reel_3",
                    performerId = "perf_3",
                    performerName = "Team Desi Grooves",
                    performerCategory = PerformerCategory.DANCER,
                    title = "Electric Dhol & Hip-hop Sangeet Entry",
                    songOrAct = "Gallan Goodiyaan X Turn Down For What",
                    durationText = "0:48",
                    likesCount = 5740,
                    commentsCount = 612,
                    viewsCount = "145K",
                    isLiked = false,
                    tagline = "The bride and groom entry went crazy! 🔥",
                    soundTrack = "Electro Dhol Mix - DJ Zenith",
                    primaryColorHex = 0xFF064E3B,
                    secondaryColorHex = 0xFF34D399
                )
            )
        ),
        Performer(
            id = "perf_4",
            name = "DJ Zenith (Karan Malhotra)",
            stageName = "DJ Zenith",
            category = PerformerCategory.DJ,
            tagline = "Club, Festival & Big Fat Indian Wedding DJ 🎧",
            bio = "Official DJ for major music fests in Goa and Mumbai. Blends international EDM, commercial house, Punjabi basslines, and Bollywood bangers seamlessly. Comes equipped with high-end console and custom visuals mapping.",
            location = "Goa & Mumbai",
            rating = 5.0,
            reviewCount = 312,
            startingPrice = 30000,
            hourlyRate = 15000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Bollywood Club Mixes", "EDM & Commercial House", "Live Mashups", "Laser Show Sync", "After-Party Sets"),
            languages = listOf("English", "Hindi", "Punjabi"),
            badges = listOf("Goa Fest Resident", "5-Star Rating"),
            showsCompleted = 340,
            reels = listOf(
                TalentReel(
                    id = "reel_4",
                    performerId = "perf_4",
                    performerName = "DJ Zenith",
                    performerCategory = PerformerCategory.DJ,
                    title = "150 BPM Desi Bass Drop - 2000+ Crowd",
                    songOrAct = "London Thumakda Festival Edit",
                    durationText = "0:39",
                    likesCount = 12400,
                    commentsCount = 1040,
                    viewsCount = "380K",
                    isLiked = true,
                    tagline = "Nobody left the dance floor till 4 AM ⚡",
                    soundTrack = "Zenith Bass Heavy Bootleg",
                    primaryColorHex = 0xFF312E81,
                    secondaryColorHex = 0xFF38BDF8
                )
            )
        ),
        Performer(
            id = "perf_5",
            name = "Zack Mirage",
            stageName = "Zack The Illusionist",
            category = PerformerCategory.MAGICIAN,
            tagline = "Celebrity Mentalist & Mind Reader ✨",
            bio = "Mesmerizing audiences worldwide with unbelievable psychological illusions, telepathy, close-up sleight of hand, and interactive stage mind-reading. Seen on national television specials and VIP celebrity parties.",
            location = "Jaipur & Delhi",
            rating = 4.9,
            reviewCount = 120,
            startingPrice = 28000,
            hourlyRate = 14000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Mind Reading", "Stage Hypnosis", "VIP Close-up Magic", "Mentalism", "Interactive Prediction"),
            languages = listOf("English", "Hindi"),
            badges = listOf("TV Featured", "VIP Favorite"),
            showsCompleted = 130,
            reels = listOf(
                TalentReel(
                    id = "reel_5",
                    performerId = "perf_5",
                    performerName = "Zack The Illusionist",
                    performerCategory = PerformerCategory.MAGICIAN,
                    title = "Reading the CEO's First Childhood Memory",
                    songOrAct = "Impossible Telepathy Act",
                    durationText = "0:55",
                    likesCount = 8900,
                    commentsCount = 760,
                    viewsCount = "210K",
                    isLiked = false,
                    tagline = "How did he guess the exact school name?! 🤯",
                    soundTrack = "Suspense Ambient Synth - Zack Live",
                    primaryColorHex = 0xFF4C1D95,
                    secondaryColorHex = 0xFFA78BFA
                )
            )
        ),
        Performer(
            id = "perf_6",
            name = "Meera Sundaram",
            stageName = "Sitar Euphoria",
            category = PerformerCategory.MUSICIAN,
            tagline = "Contemporary Electric Sitar & Fusion Artist 🎸",
            bio = "Graduated from Sangeet Natak Akademi, Meera blends centuries-old Indian classical ragas with deep synth pads and Lo-Fi beats. Perfect for cocktail soirees, art galas, ambient wedding welcomes, and fusion concerts.",
            location = "New Delhi",
            rating = 4.8,
            reviewCount = 78,
            startingPrice = 22000,
            hourlyRate = 11000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Electric Sitar", "Indian Classical Ragas", "Lo-Fi Fusion", "Ambient Soundscapes", "Acoustic Sitar"),
            languages = listOf("Hindi", "English"),
            badges = listOf("Classical Prodigy", "Aesthetic Vibes"),
            showsCompleted = 82,
            reels = listOf(
                TalentReel(
                    id = "reel_6",
                    performerId = "perf_6",
                    performerName = "Sitar Euphoria",
                    performerCategory = PerformerCategory.MUSICIAN,
                    title = "Raga Bhairavi Meets Cyberpunk Beats",
                    songOrAct = "Fusion Twilight Live",
                    durationText = "0:50",
                    likesCount = 6720,
                    commentsCount = 430,
                    viewsCount = "118K",
                    isLiked = false,
                    tagline = "Soul soothing strings at sunset 🌅",
                    soundTrack = "Electric Sitar Ambient Live",
                    primaryColorHex = 0xFF701A75,
                    secondaryColorHex = 0xFFF472B6
                )
            )
        ),
        Performer(
            id = "perf_7",
            name = "Riya Sen",
            stageName = "Anchor Riya",
            category = PerformerCategory.ANCHOR,
            tagline = "Charismatic Sangeet & Corporate Anchor 🎙️",
            bio = "High-energy, spontaneous, and charming event host who turns any gathering into an unforgettable celebration. Fluent in Hindi, English, and Marathi with 300+ events anchored smoothly.",
            location = "Pune & Mumbai",
            rating = 4.9,
            reviewCount = 155,
            startingPrice = 18000,
            hourlyRate = 8000,
            isVerified = true,
            isAvailable = true,
            skills = listOf("Crowd Engagement", "Sangeet Games", "Award Ceremonies", "Bilingual Hosting", "Spontaneous Comedy"),
            languages = listOf("Hindi", "English", "Marathi"),
            badges = listOf("Host of the Year", "Energetic"),
            showsCompleted = 310,
            reels = emptyList()
        )
    )

    private val initialGigs = listOf(
        EventGig(
            id = "gig_1",
            title = "Grand Sangeet Night Singer & Band Needed",
            organizerName = "Kapoor & Singhal Wedding Planners",
            eventType = "Shaadi / Wedding Sangeet",
            city = "Udaipur, Rajasthan",
            venue = "The Leela Palace Courtyard",
            date = "Dec 14, 2026",
            budgetRange = "₹60,000 - ₹90,000",
            description = "Looking for a soulful yet energetic Bollywood and Sufi vocalist for 2.5 hours live performance during Royal Sangeet. Full sound system provided.",
            requiredCategory = PerformerCategory.SINGER,
            applicantsCount = 16,
            isApplied = false,
            tag = "Featured • Royal Wedding"
        ),
        EventGig(
            id = "gig_2",
            title = "Annual Tech Summit Comedy Act",
            organizerName = "InnoTech Global Bangalore",
            eventType = "Corporate Gala Dinner",
            city = "Bengaluru, Karnataka",
            venue = "ITC Gardenia Grand Ballroom",
            date = "Nov 28, 2026",
            budgetRange = "₹40,000 - ₹70,000",
            description = "Need a clean, hilarious stand-up comic for a 45-minute set before awards distribution. Audience of 400 software leaders and engineers.",
            requiredCategory = PerformerCategory.COMEDIAN,
            applicantsCount = 9,
            isApplied = false,
            tag = "High Budget • Corporate"
        ),
        EventGig(
            id = "gig_3",
            title = "Weekend Rooftop Sunset Club DJ",
            organizerName = "Skyline Lounge & Bar",
            eventType = "Club Night / Weekend Residency",
            city = "Bandra West, Mumbai",
            venue = "Skyline Level 34",
            date = "Every Saturday night",
            budgetRange = "₹30,000 / Night",
            description = "Looking for deep house, melodic techno, and commercial Bollywood remix DJ to headline weekend nights from 9 PM till 1:30 AM.",
            requiredCategory = PerformerCategory.DJ,
            applicantsCount = 12,
            isApplied = true,
            tag = "Recurring Residency"
        ),
        EventGig(
            id = "gig_4",
            title = "High Energy Bollywood Dance Entry Troupe",
            organizerName = "Royal Heritage Events",
            eventType = "Destination Wedding",
            city = "Jaipur, Rajasthan",
            venue = "Jai Mahal Palace",
            date = "Jan 08, 2027",
            budgetRange = "₹50,000 - ₹80,000",
            description = "Need a 6 to 8 dancer troupe for Groom & Bride grand entry act followed by 2 choreographed fusion performances.",
            requiredCategory = PerformerCategory.DANCER,
            applicantsCount = 7,
            isApplied = false,
            tag = "High Energy"
        ),
        EventGig(
            id = "gig_5",
            title = "Mentalist & Close-up Magician for Private VIP Party",
            organizerName = "Rajesh Singhania",
            eventType = "50th Milestone Birthday Gala",
            city = "South Delhi",
            venue = "Private Farmhouse, Chattarpur",
            date = "Dec 05, 2026",
            budgetRange = "₹35,000 - ₹50,000",
            description = "Looking for an illusionist/mentalist to perform close-up table magic during cocktail hour followed by a 30-minute interactive stage mind-reading show.",
            requiredCategory = PerformerCategory.MAGICIAN,
            applicantsCount = 4,
            isApplied = false,
            tag = "Exclusive VIP"
        )
    )

    private val initialBookings = listOf(
        BookingRequest(
            id = "bk_101",
            performerId = "perf_1",
            performerName = "Kabir Live",
            organizerName = "Pooja Malhotra (Bride's Sister)",
            organizerContact = "+91 98200 45678",
            eventType = "Cocktail & Sangeet Evening",
            eventDate = "Dec 18, 2026",
            eventTime = "8:00 PM - 11:00 PM",
            venueAddress = "Taj Exotica Resort, Benaulim, Goa",
            durationHours = 3,
            totalAmount = 45000,
            status = BookingStatus.CONFIRMED,
            specialRequirements = "Please include 'Jeena Jeena' and 'Channa Mereya' in the unplugged set. Acoustic setup with sound engineer.",
            createdAt = "2 days ago"
        ),
        BookingRequest(
            id = "bk_102",
            performerId = "perf_4",
            performerName = "DJ Zenith",
            organizerName = "Aman Joshi",
            organizerContact = "+91 99876 12345",
            eventType = "New Year's Eve Mega Bash",
            eventDate = "Dec 31, 2026",
            eventTime = "10:00 PM - 3:00 AM",
            venueAddress = "Club W, Candolim Beach, Goa",
            durationHours = 5,
            totalAmount = 75000,
            status = BookingStatus.CONFIRMED,
            specialRequirements = "High energy countdown drop at midnight, EDM + Bollywood remixes.",
            createdAt = "Yesterday"
        ),
        BookingRequest(
            id = "bk_103",
            performerId = "perf_2",
            performerName = "Aarav Laughs",
            organizerName = "Apex Financial Solutions HR",
            organizerContact = "+91 97110 88990",
            eventType = "Annual Corporate Awards Night",
            eventDate = "Jan 15, 2027",
            eventTime = "7:30 PM - 8:30 PM",
            venueAddress = "JW Marriott Aerocity, New Delhi",
            durationHours = 1,
            totalAmount = 35000,
            status = BookingStatus.PENDING,
            specialRequirements = "1 hour clean standup routine, gentle roasting of the sales leadership.",
            createdAt = "3 hours ago"
        )
    )

    private val initialReviews = listOf(
        ReviewItem(
            id = "rev_1",
            performerId = "perf_1",
            clientName = "Rohit & Ananya Khanna",
            eventType = "Wedding Sangeet, Jaipur",
            rating = 5,
            date = "Oct 24, 2026",
            comment = "Kabir was the absolute soul of our sangeet! Every single guest was singing at the top of their lungs. His acoustic rendition of Sufi songs brought tears of joy. Highly recommended!"
        ),
        ReviewItem(
            id = "rev_2",
            performerId = "perf_1",
            clientName = "Tata Steel Leadership Council",
            eventType = "Annual Gala, Jamshedpur",
            rating = 5,
            date = "Sep 15, 2026",
            comment = "Extremely professional, arrived early for soundcheck, and captivated a 600+ audience. Superb stage presence!"
        ),
        ReviewItem(
            id = "rev_3",
            performerId = "perf_4",
            clientName = "Priya Mehta",
            eventType = "Destination Birthday, Goa",
            rating = 5,
            date = "Oct 30, 2026",
            comment = "DJ Zenith was legendary! The bass drops, the smooth transitions, and his crowd hype skills are unmatched. 10/10!"
        )
    )

    private val initialLiveStreams = listOf(
        LiveStageSession(
            id = "stream_1",
            performerId = "perf_1",
            performerName = "Kabir Verma",
            performerStageName = "Kabir Live",
            category = PerformerCategory.SINGER,
            streamTitle = "Acoustic Sufi & Late Night Requests 🎤✨",
            currentViewers = 1420,
            totalTipsGold = 4850,
            messages = listOf(
                LiveChatMessage("msg_1", "Simran_K", "Bhai please sing 'Afreen Afreen' next! ❤️"),
                LiveChatMessage("msg_2", "Rohan_Delhi", "That guitar solo was phenomenal brother 🎸🔥"),
                LiveChatMessage("msg_3", "EventPlannerGoa", "Sent a Golden Mic! You're booked for my next show 🏆", isGift = true, giftEmoji = "🏆"),
                LiveChatMessage("msg_4", "Pooja_M", "Loving this acoustic session so much!"),
                LiveChatMessage("msg_5", "Deepak_Music", "Sent 5x Roses 🌹🌹🌹", isGift = true, giftEmoji = "🌹")
            )
        ),
        LiveStageSession(
            id = "stream_2",
            performerId = "perf_4",
            performerName = "Karan Malhotra",
            performerStageName = "DJ Zenith",
            category = PerformerCategory.DJ,
            streamTitle = "Saturday Sunset Club Jam - Live From Goa 🎧🔥",
            currentViewers = 2190,
            totalTipsGold = 8200,
            messages = listOf(
                LiveChatMessage("msg_201", "Vikram_Party", "This beat drop is illegal brooo!! 🔥"),
                LiveChatMessage("msg_202", "Elena_Clubber", "Sent a Diamond Trophy! 💎", isGift = true, giftEmoji = "💎"),
                LiveChatMessage("msg_203", "Sameer_Goa", "Best DJ in the scene right now hands down.")
            )
        )
    )

    private val initialChat = listOf(
        ChatMessage("c1", "Pooja Malhotra (Organizer)", false, "Hi Kabir! Loved your reels on PerformerHub. We want to book you for our Goa Sangeet on Dec 18th.", "10:30 AM"),
        ChatMessage("c2", "Kabir Live", true, "Hello Pooja! Thank you so much. Dec 18th is currently open on my calendar. What time slot are you aiming for?", "10:34 AM"),
        ChatMessage("c3", "Pooja Malhotra (Organizer)", false, "From 8 PM to 11 PM. Unplugged Bollywood and Sufi vibes.", "10:36 AM"),
        ChatMessage("c4", "Kabir Live", true, "Perfect! I've sent a verified quote card for ₹45,000 including my acoustic setup.", "10:40 AM", isQuoteCard = true, quoteAmount = 45000),
        ChatMessage("c5", "Pooja Malhotra (Organizer)", false, "Accepted and confirmed! Looking forward to an amazing evening! 🎉", "11:00 AM")
    )

    // StateFlows
    private val _performers = MutableStateFlow(initialPerformers)
    val performers: StateFlow<List<Performer>> = _performers.asStateFlow()

    private val _gigs = MutableStateFlow(initialGigs)
    val gigs: StateFlow<List<EventGig>> = _gigs.asStateFlow()

    private val _bookings = MutableStateFlow(initialBookings)
    val bookings: StateFlow<List<BookingRequest>> = _bookings.asStateFlow()

    private val _reviews = MutableStateFlow(initialReviews)
    val reviews: StateFlow<List<ReviewItem>> = _reviews.asStateFlow()

    private val _liveStreams = MutableStateFlow(initialLiveStreams)
    val liveStreams: StateFlow<List<LiveStageSession>> = _liveStreams.asStateFlow()

    private val _chatMessages = MutableStateFlow(initialChat)
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Mode: Audience/Organizer (Discover & Book) vs Performer Studio (Manage profile, gigs, earnings)
    private val _isPerformerMode = MutableStateFlow(false)
    val isPerformerMode: StateFlow<Boolean> = _isPerformerMode.asStateFlow()

    fun toggleAppMode() {
        _isPerformerMode.update { !it }
    }

    fun toggleLikeReel(reelId: String) {
        _performers.update { list ->
            list.map { perf ->
                perf.copy(
                    reels = perf.reels.map { reel ->
                        if (reel.id == reelId) {
                            val newLiked = !reel.isLiked
                            reel.copy(
                                isLiked = newLiked,
                                likesCount = if (newLiked) reel.likesCount + 1 else reel.likesCount - 1
                            )
                        } else reel
                    }
                )
            }
        }
    }

    fun toggleSavePerformer(performerId: String) {
        _performers.update { list ->
            list.map {
                if (it.id == performerId) it.copy(isSaved = !it.isSaved) else it
            }
        }
    }

    fun submitBookingRequest(
        performerId: String,
        performerName: String,
        organizerName: String,
        organizerContact: String,
        eventType: String,
        date: String,
        time: String,
        venue: String,
        durationHours: Int,
        totalAmount: Long,
        notes: String
    ): BookingRequest {
        val newBooking = BookingRequest(
            id = "bk_" + UUID.randomUUID().toString().take(6),
            performerId = performerId,
            performerName = performerName,
            organizerName = organizerName.ifBlank { "Event Organizer" },
            organizerContact = organizerContact.ifBlank { "+91 98000 11223" },
            eventType = eventType,
            eventDate = date,
            eventTime = time,
            venueAddress = venue,
            durationHours = durationHours,
            totalAmount = totalAmount,
            status = BookingStatus.PENDING,
            specialRequirements = notes,
            createdAt = "Just now"
        )
        _bookings.update { listOf(newBooking) + it }
        return newBooking
    }

    fun updateBookingStatus(bookingId: String, newStatus: BookingStatus) {
        _bookings.update { list ->
            list.map { if (it.id == bookingId) it.copy(status = newStatus) else it }
        }
    }

    fun applyToGig(gigId: String) {
        _gigs.update { list ->
            list.map {
                if (it.id == gigId) {
                    it.copy(
                        isApplied = !it.isApplied,
                        applicantsCount = if (!it.isApplied) it.applicantsCount + 1 else it.applicantsCount - 1
                    )
                } else it
            }
        }
    }

    fun postNewGig(
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
        val newGig = EventGig(
            id = "gig_" + UUID.randomUUID().toString().take(6),
            title = title,
            organizerName = organizer.ifBlank { "Event Host" },
            eventType = eventType,
            city = city,
            venue = venue,
            date = date,
            budgetRange = budget,
            description = desc,
            requiredCategory = category,
            applicantsCount = 1,
            isApplied = false,
            tag = "New • Open for Pitches"
        )
        _gigs.update { listOf(newGig) + it }
    }

    fun addLiveComment(streamId: String, username: String, text: String, giftEmoji: String = "", isGift: Boolean = false) {
        val newMsg = LiveChatMessage(
            id = "msg_" + UUID.randomUUID().toString().take(6),
            user = username.ifBlank { "TalentLover" },
            message = text,
            isGift = isGift,
            giftEmoji = giftEmoji,
            timestamp = "Just now"
        )
        _liveStreams.update { list ->
            list.map { stream ->
                if (stream.id == streamId) {
                    val addTip = if (isGift) 250L else 0L
                    stream.copy(
                        messages = stream.messages + newMsg,
                        totalTipsGold = stream.totalTipsGold + addTip
                    )
                } else stream
            }
        }
    }

    fun sendChatMessage(text: String, isFromPerformer: Boolean = false) {
        val msg = ChatMessage(
            id = "c_" + UUID.randomUUID().toString().take(6),
            senderName = if (isFromPerformer) "Kabir Live" else "You (Organizer)",
            isFromPerformer = isFromPerformer,
            text = text,
            time = "Just now"
        )
        _chatMessages.update { it + msg }
    }

    fun addPerformerProfile(
        name: String,
        stageName: String,
        category: PerformerCategory,
        tagline: String,
        bio: String,
        location: String,
        startingPrice: Long,
        hourlyRate: Long,
        skillsString: String,
        languagesString: String
    ): Performer {
        val newPerf = Performer(
            id = "perf_" + UUID.randomUUID().toString().take(6),
            name = name,
            stageName = stageName.ifBlank { name },
            category = category,
            tagline = tagline,
            bio = bio,
            location = location.ifBlank { "Mumbai, India" },
            rating = 5.0,
            reviewCount = 1,
            startingPrice = startingPrice,
            hourlyRate = hourlyRate,
            isVerified = true,
            isAvailable = true,
            skills = skillsString.split(",").map { it.trim() }.filter { it.isNotBlank() },
            languages = languagesString.split(",").map { it.trim() }.filter { it.isNotBlank() },
            badges = listOf("New Talent 🌟", "Spotlight Ready"),
            showsCompleted = 0,
            reels = listOf(
                TalentReel(
                    id = "reel_" + UUID.randomUUID().toString().take(6),
                    performerId = "user_perf",
                    performerName = stageName.ifBlank { name },
                    performerCategory = category,
                    title = "$stageName - Debut Showcase",
                    songOrAct = "Signature Performance Piece",
                    durationText = "0:45",
                    likesCount = 38,
                    commentsCount = 4,
                    viewsCount = "520",
                    isLiked = false,
                    tagline = "Welcome to my stage journey! 🎤✨",
                    soundTrack = "Original Talent Reel",
                    primaryColorHex = 0xFF4A044E,
                    secondaryColorHex = 0xFFC026D3
                )
            )
        )
        _performers.update { listOf(newPerf) + it }
        return newPerf
    }

    fun addReview(performerId: String, clientName: String, eventType: String, rating: Int, comment: String) {
        val newRev = ReviewItem(
            id = "rev_" + UUID.randomUUID().toString().take(6),
            performerId = performerId,
            clientName = clientName.ifBlank { "Verified Host" },
            eventType = eventType,
            rating = rating,
            date = "Today",
            comment = comment
        )
        _reviews.update { listOf(newRev) + it }
        // Update performer rating
        _performers.update { list ->
            list.map { p ->
                if (p.id == performerId) {
                    val updatedCount = p.reviewCount + 1
                    val newAvg = ((p.rating * p.reviewCount) + rating) / updatedCount
                    p.copy(rating = Math.round(newAvg * 10.0) / 10.0, reviewCount = updatedCount)
                } else p
            }
        }
    }
}
