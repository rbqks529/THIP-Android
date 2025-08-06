package com.texthip.thip.data.model.group.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyRoomListResponse(
    @SerialName("roomList") val roomList: List<MyRoomResponse>,
    @SerialName("nextCursor") val nextCursor: String?,
    @SerialName("isLast") val isLast: Boolean
)

@Serializable
data class MyRoomResponse(
    @SerialName("roomId") val roomId: Int,
    @SerialName("bookImageUrl") val bookImageUrl: String,
    @SerialName("bookTitle") val bookTitle: String,
    @SerialName("memberCount") val memberCount: Int,
    @SerialName("endDate") val endDate: String? // "완료된" 모임방의 경우 null
)