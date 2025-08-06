package com.texthip.thip.data.model.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.response.CreateRoomResponse
import com.texthip.thip.data.model.group.response.JoinedRoomListResponse
import com.texthip.thip.data.model.group.response.MyRoomListResponse
import com.texthip.thip.data.model.group.response.RoomRecruitingResponse
import com.texthip.thip.data.model.group.response.RoomMainList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

    @GET("rooms/home/joined")
    suspend fun getJoinedRooms(
        @Query("page") page: Int = 1
    ): BaseResponse<JoinedRoomListResponse>

    @GET("rooms")
    suspend fun getRooms(
        @Query("category") category: String = "문학"   // 디폴트=문학
    ): BaseResponse<RoomMainList>

    @GET("rooms/my")
    suspend fun getMyRooms(
        @Query("type") type: String? = null,  // "playing", "recruiting", "expired", null
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyRoomListResponse>

    @GET("rooms/{roomId}/recruiting")
    suspend fun getRoomRecruiting(@Path("roomId") roomId: Int): BaseResponse<RoomRecruitingResponse>

    @POST("rooms")
    suspend fun createRoom(
        @Body request: CreateRoomRequest
    ): BaseResponse<CreateRoomResponse>

}