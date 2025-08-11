package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.mypage.mock.FeedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SavedFeedViewModel: ViewModel() {
    private val _feeds = MutableStateFlow(
        listOf(
            FeedItem(
                id = 1,
                userProfileImage = R.drawable.character_art,
                userName = "user",
                userRole = "학생",
                bookTitle = "라랄ㄹ라라",
                authName = "야야야",
                timeAgo = "15시간 전",
                content = "진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공",
                likeCount = 25,
                commentCount = 4,
                isLiked = false,
                isSaved = true
            ),
            FeedItem(
                id = 2,
                userProfileImage = R.drawable.character_art,
                userName = "user",
                userRole = "학생",
                bookTitle = "라랄ㄹ라라",
                authName = "야야야",
                timeAgo = "15시간 전",
                content = "너무 재밌네요..",
                likeCount = 25,
                commentCount = 4,
                isLiked = false,
                isSaved = true,
                imageUrls = null
            ),
            FeedItem(
                id = 3,
                userProfileImage = R.drawable.character_art,
                userName = "user",
                userRole = "학생",
                bookTitle = "라랄ㄹ라라",
                authName = "야야야",
                timeAgo = "15시간 전",
                content = "너무 재밌네요..",
                likeCount = 25,
                commentCount = 4,
                isLiked = false,
                isSaved = true,
                imageUrls = listOf(R.drawable.img_book_cover_sample)
            ),
            FeedItem(
                id = 4,
                userProfileImage = R.drawable.character_art,
                userName = "user",
                userRole = "학생",
                bookTitle = "책이름책이름",
                authName = "저자이름저자이름",
                timeAgo = "15시간 전",
                content = "진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공",
                likeCount = 25,
                commentCount = 4,
                isLiked = false,
                isSaved = true,
                imageUrls = listOf(R.drawable.img_book_cover_sample)
            ),
            FeedItem(
                id = 5,
                userProfileImage = R.drawable.character_art,
                userName = "user",
                userRole = "학생",
                bookTitle = "책이름책이름",
                authName = "저자이름저자이름",
                timeAgo = "15시간 전",
                content = "진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공",
                likeCount = 25,
                commentCount = 4,
                isLiked = true,
                isSaved = true
            ),


        )
    )
    val feeds: StateFlow<List<FeedItem>> = _feeds

    fun toggleBookmark(id: Int) {
        _feeds.value = _feeds.value.map {
            if (it.id == id) it.copy(isSaved = !it.isSaved) else it
        }
    }

    fun toggleLike(id: Int) {
        _feeds.value = _feeds.value.map {
            if (it.id == id) it.copy(isLiked = !it.isLiked) else it
        }
    }
}