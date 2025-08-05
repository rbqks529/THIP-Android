package com.texthip.thip.data.model.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.response.CreateRoomResponse
import com.texthip.thip.data.model.group.response.JoinedRoomsDto
import com.texthip.thip.data.model.group.response.MyRoomsDto
import com.texthip.thip.data.model.group.response.RoomRecruitingDto
import com.texthip.thip.data.model.group.response.RoomsHomeDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

    @GET("rooms/home/joined")
    suspend fun getJoinedRooms(
        @Query("page") page: Int = 1
    ): BaseResponse<JoinedRoomsDto>

    @GET("rooms")
    suspend fun getRooms(
        @Query("category") category: String = "문학"   // 디폴트=문학
    ): BaseResponse<RoomsHomeDto>

    @GET("rooms/my")
    suspend fun getMyRooms(
        @Query("type") type: String? = null,  // "playing", "recruiting", "expired", null
        @Query("cursor") cursor: String? = null
    ): BaseResponse<MyRoomsDto>

    @GET("rooms/{roomId}/recruiting")
    suspend fun getRoomRecruiting(@Path("roomId") roomId: Int): BaseResponse<RoomRecruitingDto>

    @POST("rooms")
    suspend fun createRoom(
        @Body request: CreateRoomRequest
    ): BaseResponse<CreateRoomResponse>

}