package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class BookDetail(
    val title: String,
    val imageUrl: String,
    val authorName: String,
    val publisher: String,
    val isbn: String,
    val description: String,
    val recruitingRoomCount: Int,
    val recruitingReadCount: Int,
    val isSaved: Boolean
)