package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.BookDetail
import com.texthip.thip.data.model.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _bookDetail = MutableStateFlow<BookDetail?>(null)
    val bookDetail: StateFlow<BookDetail?> = _bookDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadBookDetail(isbn: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            bookRepository.getBookDetail(isbn)
                .onSuccess { bookDetail ->
                    _bookDetail.value = bookDetail
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "책 정보를 가져오는데 실패했습니다"
                }
            
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}