package com.texthip.thip.ui.group.makeroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.repository.GroupRepository
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.repository.BookRepository
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.GroupMakeRoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GroupMakeRoomViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupMakeRoomUiState())
    val uiState: StateFlow<GroupMakeRoomUiState> = _uiState.asStateFlow()

    // 책 목록 상태
    private val _savedBooks = MutableStateFlow<List<BookData>>(emptyList())
    val savedBooks: StateFlow<List<BookData>> = _savedBooks.asStateFlow()
    
    private val _groupBooks = MutableStateFlow<List<BookData>>(emptyList())
    val groupBooks: StateFlow<List<BookData>> = _groupBooks.asStateFlow()
    
    private val _isLoadingBooks = MutableStateFlow(false)
    val isLoadingBooks: StateFlow<Boolean> = _isLoadingBooks.asStateFlow()

    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")

    // 책 선택
    fun selectBook(book: BookData) {
        _uiState.value = _uiState.value.copy(selectedBook = book)
    }

    // 책 검색 시트 표시 상태 변경
    fun toggleBookSearchSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showBookSearchSheet = show)
        if (show) {
            loadBooks()
        }
    }
    
    // 책 목록 로드
    private fun loadBooks() {
        viewModelScope.launch {
            _isLoadingBooks.value = true
            try {
                // 저장한 책 로드
                val savedBooksResult = bookRepository.getBooks("saved")
                savedBooksResult.onSuccess { bookDtos ->
                    _savedBooks.value = bookDtos.map { it.toBookData() }
                }.onFailure {
                    _savedBooks.value = emptyList()
                }
                
                // 모임 책 로드
                val groupBooksResult = bookRepository.getBooks("joining")
                groupBooksResult.onSuccess { bookDtos ->
                    _groupBooks.value = bookDtos.map { it.toBookData() }
                }.onFailure {
                    _groupBooks.value = emptyList()
                }
            } catch (e: Exception) {
                _savedBooks.value = emptyList()
                _groupBooks.value = emptyList()
            } finally {
                _isLoadingBooks.value = false
            }
        }
    }
    
    // BookDto를 BookData로 변환
    private fun BookSavedResponse.toBookData(): BookData {
        return BookData(
            title = this.bookTitle,
            imageUrl = this.imageUrl,
            author = this.authorName,
            isbn = this.isbn
        )
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
    fun createGroup(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            onError("입력 정보를 확인해주세요")
            return
        }

        val selectedBook = currentState.selectedBook
        if (selectedBook?.isbn == null) {
            onError("책 정보가 올바르지 않습니다")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

                // API 요청 데이터 생성
                val request = CreateRoomRequest(
                    isbn = selectedBook.isbn,
                    category = getApiCategoryName(currentState.selectedGenreIndex),
                    roomName = currentState.roomTitle.trim(),
                    description = currentState.roomDescription.trim(),
                    progressStartDate = currentState.meetingStartDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    progressEndDate = currentState.meetingEndDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    recruitCount = currentState.memberLimit,
                    password = if (currentState.isPrivate) currentState.password else null,
                    isPublic = !currentState.isPrivate
                )

                // API 호출
                val result = groupRepository.createRoom(request)
                result.onSuccess { roomId ->
                    onSuccess(roomId)
                }.onFailure { exception ->
                    onError("모임방 생성에 실패했습니다: ${exception.message}")
                }
            } catch (e: Exception) {
                onError("네트워크 오류가 발생했습니다: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    // 장르 인덱스를 API 카테고리명으로 변환
    private fun getApiCategoryName(genreIndex: Int): String {
        return when (genreIndex) {
            0 -> "문학"
            1 -> "과학/IT"
            2 -> "사회과학"
            3 -> "인문학"
            4 -> "예술"
            else -> "문학" // 기본값
        }
    }

    // 에러 메시지 클리어
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}