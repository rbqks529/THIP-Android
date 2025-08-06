package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.BookSearchItem
import com.texthip.thip.data.model.book.response.MostSearchedBook
import com.texthip.thip.data.model.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    // 책 검색 관련 상태
    private val _bookSearchResults = MutableStateFlow<List<BookSearchItem>>(emptyList())
    val bookSearchResults: StateFlow<List<BookSearchItem>> = _bookSearchResults.asStateFlow()
    
    private val _isBookSearching = MutableStateFlow(false)
    val isBookSearching: StateFlow<Boolean> = _isBookSearching.asStateFlow()
    
    private val _bookSearchQuery = MutableStateFlow("")
    val bookSearchQuery: StateFlow<String> = _bookSearchQuery.asStateFlow()
    
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private val _hasMoreResults = MutableStateFlow(false)
    val hasMoreResults: StateFlow<Boolean> = _hasMoreResults.asStateFlow()
    
    private val _bookSearchError = MutableStateFlow<String?>(null)
    val bookSearchError: StateFlow<String?> = _bookSearchError.asStateFlow()
    
    private val _totalPages = MutableStateFlow(0)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()
    
    private val _totalElements = MutableStateFlow(0)
    val totalElements: StateFlow<Int> = _totalElements.asStateFlow()
    
    // 최근 검색어 관리
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()
    
    // 가장 많이 검색된 책 데이터
    private val _mostSearchedBooks = MutableStateFlow<List<MostSearchedBook>>(emptyList())
    val mostSearchedBooks: StateFlow<List<MostSearchedBook>> = _mostSearchedBooks.asStateFlow()
    
    private val _isMostSearchedLoading = MutableStateFlow(false)
    val isMostSearchedLoading: StateFlow<Boolean> = _isMostSearchedLoading.asStateFlow()

    // 책 검색 실행
    fun searchBooks(keyword: String, isNewSearch: Boolean = true) {
        if (keyword.isBlank()) return
        
        viewModelScope.launch {
            _isBookSearching.value = true
            _bookSearchError.value = null
            
            if (isNewSearch) {
                _currentPage.value = 1
                _bookSearchResults.value = emptyList()
                _bookSearchQuery.value = keyword
            }
            
            bookRepository.searchBooks(keyword, _currentPage.value)
                .onSuccess { bookSearchData ->
                    val newResults = bookSearchData.searchResult
                    
                    if (isNewSearch) {
                        _bookSearchResults.value = newResults
                    } else {
                        _bookSearchResults.value = _bookSearchResults.value + newResults
                    }
                    
                    _hasMoreResults.value = !bookSearchData.last
                    _totalPages.value = bookSearchData.totalPages
                    _totalElements.value = bookSearchData.totalElements
                    
                    if (!bookSearchData.last) {
                        _currentPage.value = _currentPage.value + 1
                    }
                }
                .onFailure { error ->
                    _bookSearchError.value = error.message ?: "책 검색 중 오류가 발생했습니다."
                }
            
            _isBookSearching.value = false
        }
    }
    
    // 더 많은 결과 로드
    fun loadMoreResults() {
        if (_hasMoreResults.value && !_isBookSearching.value && _bookSearchQuery.value.isNotBlank()) {
            searchBooks(_bookSearchQuery.value, isNewSearch = false)
        }
    }
    
    // 검색 결과 초기화
    fun clearSearchResults() {
        _bookSearchResults.value = emptyList()
        _bookSearchQuery.value = ""
        _currentPage.value = 1
        _hasMoreResults.value = false
        _bookSearchError.value = null
        _totalPages.value = 0
        _totalElements.value = 0
    }
    
    // 에러 상태 초기화
    fun clearError() {
        _bookSearchError.value = null
    }
    
    // 최근 검색어 추가
    fun addRecentSearch(keyword: String) {
        if (keyword.isBlank() || _recentSearches.value.contains(keyword)) return
        
        val newSearches = listOf(keyword) + _recentSearches.value.take(9) // 최대 10개 유지
        _recentSearches.value = newSearches
    }
    
    // 최근 검색어 제거
    fun removeRecentSearch(keyword: String) {
        _recentSearches.value = _recentSearches.value.filterNot { it == keyword }
    }
    
    // 최근 검색어 설정 (SharedPreferences에서 로드할 때 사용)
    fun setRecentSearches(searches: List<String>) {
        _recentSearches.value = searches
    }
    
    // 가장 많이 검색된 책 로드
    fun loadMostSearchedBooks() {
        viewModelScope.launch {
            _isMostSearchedLoading.value = true
            
            bookRepository.getMostSearchedBooks()
                .onSuccess { books ->
                    _mostSearchedBooks.value = books
                }
                .onFailure { error ->
                    // 에러 처리 (필요시 에러 상태 추가 가능)
                }
            
            _isMostSearchedLoading.value = false
        }
    }
    
    init {
        // 뷰모델 초기화 시 가장 많이 검색된 책 로드
        loadMostSearchedBooks()
    }
}