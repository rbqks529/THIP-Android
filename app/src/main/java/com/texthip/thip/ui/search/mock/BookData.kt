package com.texthip.thip.ui.search.mock

import com.texthip.thip.R

data class BookData(
    val title: String,
    val author: String = "",
    val publisher: String = "",
    val imageUrl: String? = null,
    val isbn: String = ""
)
