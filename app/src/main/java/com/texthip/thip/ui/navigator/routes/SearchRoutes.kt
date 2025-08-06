package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class SearchRoutes : Routes() {
    @Serializable
    data class BookDetail(val isbn: String) : SearchRoutes()
    
    // 향후 추가될 Search 관련 화면들
    // @Serializable data object BookGroup : SearchRoutes
}