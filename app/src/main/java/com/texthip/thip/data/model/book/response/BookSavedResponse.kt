package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class BookSavedResponse(
    val isbn: String,
    val bookTitle: String,
    val authorName: String,
    val publisher: String,
    val imageUrl: String?
)

@Serializable
data class BookListResponse(
    val bookList: List<BookSavedResponse>
)