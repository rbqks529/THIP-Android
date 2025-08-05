package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class BookSearchItem(
    val title: String,
    val imageUrl: String,
    val authorName: String,
    val publisher: String,
    val isbn: String
)

@Serializable
data class BookSearchData(
    val searchResult: List<BookSearchItem>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean,
    val first: Boolean
)