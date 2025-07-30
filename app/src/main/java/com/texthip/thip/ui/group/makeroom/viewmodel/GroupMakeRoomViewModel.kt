package com.texthip.thip.ui.group.makeroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.GroupMakeRoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GroupMakeRoomViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GroupMakeRoomUiState())
    val uiState: StateFlow<GroupMakeRoomUiState> = _uiState.asStateFlow()

    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")

    // 책 선택
    fun selectBook(book: BookData) {
        _uiState.value = _uiState.value.copy(selectedBook = book)
    }

    // 책 검색 시트 표시 상태 변경
    fun toggleBookSearchSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showBookSearchSheet = show)
    }

    // 장르 선택
    fun selectGenre(index: Int) {
        _uiState.value = _uiState.value.copy(selectedGenreIndex = index)
    }

    // 방 제목 변경
    fun updateRoomTitle(title: String) {
        _uiState.value = _uiState.value.copy(roomTitle = title)
    }

    // 방 설명 변경
    fun updateRoomDescription(description: String) {
        _uiState.value = _uiState.value.copy(roomDescription = description)
    }

    // 모임 날짜 범위 설정
    fun setDateRange(startDate: LocalDate, endDate: LocalDate) {
        _uiState.value = _uiState.value.copy(
            meetingStartDate = startDate,
            meetingEndDate = endDate
        )
    }

    // 인원 수 설정
    fun setMemberLimit(count: Int) {
        _uiState.value = _uiState.value.copy(memberLimit = count)
    }

    // 비밀방 설정
    fun togglePrivate(isPrivate: Boolean) {
        _uiState.value = _uiState.value.copy(
            isPrivate = isPrivate,
            password = if (!isPrivate) "" else _uiState.value.password
        )
    }

    // 비밀번호 설정
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    // 그룹 생성 요청
    fun createGroup(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            //onError("입력 정보를 확인해주세요")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

                val request = currentState.toRequest()
                // TODO: Repository에 createGroup 메서드 추가 후 구현
                // val result = groupRepository.createGroup(request)
                
                // 임시로 성공 처리
                onSuccess()
                
                // if (result.isSuccess) {
                //     onSuccess()
                // } else {
                //     onError(result.message ?: "그룹 생성에 실패했습니다")
                // }
            } catch (e: Exception) {
                //onError("네트워크 오류가 발생했습니다: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // 에러 메시지 클리어
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}