package com.texthip.thip.data.model.group.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JoinedRoomListResponse(
    @SerialName("roomList") val roomList: List<JoinedRoomResponse>,
    @SerialName("nickname") val nickname: String,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("last") val last: Boolean,
    @SerialName("first") val first: Boolean
)

@Serializable
data class JoinedRoomResponse(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String?,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("userPercentage") val userPercentage: Int
)

data class PaginationResult<T>(
    val data: List<T>,
    val hasMore: Boolean,
    val currentPage: Int,
    val nickname: String = ""
)
