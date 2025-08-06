package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class MostSearchedBook(
    val title: String,
    val imageUrl: String,
    val isbn: String,
    val ranking: Int
)