package com.anwesha.anweshascan

import java.io.Serializable


data class ScanApiData(
    val signature: String,
    val event_id: String
)

data class CurrentUserStatus(
    val anwesha_id: String,
    val event_id: String,
    val has_entered: Boolean
)

data class CurrentUserResponse(
    val message: String
)

data class ScanApiResponse(
    val anwesha_id: String?,
    val username: String?,
    val message: String,
    val has_entered: Boolean?
)

data class EventsList(
    val id: String? = null,
    val name: String? = null,
    val organizer: String? = null,
    val venue: String? = null,
    val description: String? = null,
    val start_time: String? = null,
    val end_time: String? = null,
    val prize: String? = null,
    val registration_fee: String? = null,
    val registration_deadline: String? = null,
    val video: String? = null,
    val poster: String? = null,
    val tags: String? = null,
    val max_team_size: Int? = null,
    val min_team_size: Int? = null,
    val is_active: Boolean? = null,
    val is_online: Boolean? = null,
    val registration_link: String? = null,
    val order: Int,
    val payment_link: String?,
    val payment_key: String?

): Serializable