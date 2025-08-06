package com.texthip.thip.data.model.repository

import android.content.Context
import com.texthip.thip.R
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.group.response.PaginationResult
import com.texthip.thip.data.model.group.response.RoomMainResponse
import com.texthip.thip.data.model.service.GroupService
import com.texthip.thip.ui.group.done.mock.MyRoomCardData
import com.texthip.thip.ui.group.done.mock.MyRoomsPaginationResult
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupService: GroupService,
    @param:ApplicationContext private val context: Context
) {
    private val genres = listOf(
        context.getString(R.string.literature),
        context.getString(R.string.science_it),
        context.getString(R.string.social_science),
        context.getString(R.string.humanities),
        context.getString(R.string.art)
    )
    private var cachedUserName: String? = null
    
    // UI 장르명 → API 카테고리명 매핑
    private fun mapGenreToApiCategory(genre: String): String {
        return when (genre) {
            "과학·IT" -> "과학/IT"
            else -> genre
        }
    }
    
    fun getUserName(): Result<String> {
        return try {
            val name = cachedUserName ?: "사용자" // 캐시된 이름이 없으면 기본값
            Result.success(name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getMyJoinedRooms(page: Int): Result<PaginationResult<GroupCardData>> {
        return try {
            groupService.getJoinedRooms(page)
                .handleBaseResponse()
                .mapCatching { data ->
                    data?.let { joinedRoomsDto ->
                        // API 응답에서 받은 닉네임을 캐시에 저장
                        cachedUserName = joinedRoomsDto.nickname
                        
                        val groups = joinedRoomsDto.roomList.map { dto ->
                            GroupCardData(
                                id = dto.roomId,
                                title = dto.bookTitle,
                                members = dto.memberCount,
                                imageUrl = dto.bookImageUrl,
                                progress = dto.userPercentage,
                                nickname = joinedRoomsDto.nickname
                            )
                        }

                        PaginationResult(
                            data = groups,
                            hasMore = !joinedRoomsDto.last,
                            currentPage = joinedRoomsDto.page,
                            nickname = joinedRoomsDto.nickname
                        )
                    } ?: PaginationResult(
                        data = emptyList(),
                        hasMore = false,
                        currentPage = page,
                        nickname = ""
                    )
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRoomSections(category: String = ""): Result<List<GroupRoomSectionData>> {
        return try {
            val finalCategory = category.ifEmpty { context.getString(R.string.literature) }
            val apiCategory = mapGenreToApiCategory(finalCategory)
            groupService.getRooms(apiCategory)
                .handleBaseResponse()
                .mapCatching { data ->
                    data?.let { roomsData ->
                        val sections = listOf(
                            GroupRoomSectionData(
                                title = context.getString(R.string.room_section_deadline),
                                rooms = roomsData.deadlineRoomList.map { dto -> 
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            ),
                            GroupRoomSectionData(
                                title = context.getString(R.string.room_section_popular), 
                                rooms = roomsData.popularRoomList.map { dto ->
                                    convertToGroupCardItemRoomData(dto, extractDaysFromDeadline(dto.deadlineDate))
                                },
                                genres = genres
                            )
                        )
                        sections
                    }.orEmpty()
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 완료된 모임방 API 연동
    suspend fun getMyRoomsByType(type: String?, cursor: String? = null): Result<MyRoomsPaginationResult> {
        return try {
            groupService.getMyRooms(type, cursor)
                .handleBaseResponse()
                .mapCatching { myRoomsDto ->
                    myRoomsDto?.let { data ->
                        val myRoomCards = data.roomList.map { room ->
                            MyRoomCardData(
                                roomId = room.roomId,
                                bookImageUrl = room.bookImageUrl,
                                bookTitle = room.bookTitle,
                                memberCount = room.memberCount,
                                endDate = room.endDate
                            )
                        }

                        MyRoomsPaginationResult(
                            data = myRoomCards,
                            nextCursor = data.nextCursor,
                            isLast = data.isLast
                        )
                    } ?: MyRoomsPaginationResult(
                        data = emptyList(),
                        nextCursor = null,
                        isLast = true
                    )
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun convertToGroupCardItemRoomData(dto: RoomMainResponse, daysLeft: Int): GroupCardItemRoomData {
        return GroupCardItemRoomData(
            id = dto.roomId,
            title = dto.roomName,
            participants = dto.memberCount,
            maxParticipants = dto.recruitCount,
            isRecruiting = true,
            endDate = daysLeft,
            imageUrl = dto.bookImageUrl, // API에서 받은 실제 이미지 URL
            genreIndex = 0,
            isSecret = false
        )
    }
    
    private fun extractDaysFromDeadline(deadlineDate: String): Int {
        return when {
            deadlineDate.contains("일 뒤") -> {
                deadlineDate.replace("일 뒤", "").trim().toIntOrNull() ?: 0
            }
            else -> 0 // 파싱할 수 없는 경우 0 반환
        }
    }
    
    suspend fun getSearchGroups(): Result<List<GroupCardItemRoomData>> {
        return try {
            // 기존에 로드된 섹션 데이터들을 합쳐서 반환
            val sectionsResult = getRoomSections()
            if (sectionsResult.isSuccess) {
                val allRooms = sectionsResult.getOrThrow().flatMap { it.rooms }
                Result.success(allRooms)
            } else {
                Result.failure(sectionsResult.exceptionOrNull() ?: Exception("Failed to load search groups"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 모집중인 모임방 상세 정보 API 연동
    suspend fun getRoomRecruiting(roomId: Int): Result<GroupRoomData> {
        return try {
            groupService.getRoomRecruiting(roomId)
                .handleBaseResponse()
                .mapCatching { recruitingDto ->
                    recruitingDto?.let { data ->
                        // 책 정보 변환
                        val bookData = GroupBookData(
                            title = data.bookTitle,
                            author = data.authorName,
                            publisher = "출판사 정보 없음", // API에서 제공하지 않음
                            description = data.bookDescription,
                            imageUrl = data.bookImageUrl // API에서 받은 실제 이미지 URL
                        )
                        
                        // 추천 모임방 변환
                        val recommendations = data.recommendRooms.map { recommendDto ->
                            GroupCardItemRoomData(
                                id = recommendDto.hashCode(), // 임시 ID (실제로는 roomId가 필요)
                                title = recommendDto.roomName,
                                participants = recommendDto.memberCount,
                                maxParticipants = recommendDto.recruitCount,
                                isRecruiting = true,
                                endDate = extractDaysFromDeadline(recommendDto.recruitEndDate),
                                imageUrl = recommendDto.roomImageUrl, // API에서 받은 실제 이미지 URL
                                genreIndex = 0, // 기본값
                                isSecret = true // 기본값
                            )
                        }
                        
                        // GroupRoomData로 변환
                        GroupRoomData(
                            id = data.roomId,
                            title = data.roomName,
                            isSecret = !data.isPublic,
                            description = data.roomDescription,
                            startDate = data.progressStartDate,
                            endDate = data.progressEndDate,
                            members = data.memberCount,
                            maxMembers = data.recruitCount,
                            daysLeft = extractDaysFromDeadline(data.recruitEndDate),
                            genre = data.category,
                            bookData = bookData,
                            recommendations = recommendations,
                            buttonType = determineButtonType(data.isHost, data.isJoining),
                            roomImageUrl = data.roomImageUrl,
                            bookImageUrl = data.bookImageUrl
                        )
                    } ?: throw Exception("No recruiting data found for room $roomId")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 버튼 타입 결정 로직
    private fun determineButtonType(isHost: Boolean, isJoining: Boolean): GroupBottomButtonType {
        return when {
            isHost -> GroupBottomButtonType.CLOSE // 호스트는 모집 마감 가능
            isJoining -> GroupBottomButtonType.CANCEL // 참여 중이면 취소 가능
            else -> GroupBottomButtonType.JOIN // 참여하지 않았으면 참여 가능
        }
    }

    // 모임방 생성 API 연동
    suspend fun createRoom(request: CreateRoomRequest): Result<Int> {
        return try {
            groupService.createRoom(request)
                .handleBaseResponse()
                .mapCatching { createRoomResponse ->
                    createRoomResponse?.roomId ?: throw Exception("방 생성 실패: roomId가 없습니다")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}