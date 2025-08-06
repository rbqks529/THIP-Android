package com.texthip.thip.ui.search.screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.search.component.SearchActiveField
import com.texthip.thip.ui.search.component.SearchBookFilteredResult
import com.texthip.thip.ui.search.component.SearchEmptyResult
import com.texthip.thip.ui.search.component.SearchRecentBook
import com.texthip.thip.ui.search.mock.BookData
import com.texthip.thip.ui.search.viewmodel.SearchBookViewModel
import com.texthip.thip.ui.theme.ThipTheme
import kotlinx.coroutines.delay
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SearchBookScreen(
    modifier: Modifier = Modifier,
    onNavigateToRegisterBook: () -> Unit = {},
    viewModel: SearchBookViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sharedPrefs = remember { 
        context.getSharedPreferences("book_search_prefs", Context.MODE_PRIVATE) 
    }

    // ViewModel 상태들
    val bookSearchResults by viewModel.bookSearchResults.collectAsState()
    val isBookSearching by viewModel.isBookSearching.collectAsState()
    val bookSearchError by viewModel.bookSearchError.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val totalElements by viewModel.totalElements.collectAsState()
    val hasMoreResults by viewModel.hasMoreResults.collectAsState()
    val mostSearchedBooks by viewModel.mostSearchedBooks.collectAsState()
    val isMostSearchedLoading by viewModel.isMostSearchedLoading.collectAsState()

    // 로컬 상태들
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearched by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    
    // 현재 날짜를 MM.dd 형식으로 포맷
    val currentDate = remember {
        SimpleDateFormat("MM.dd", Locale.getDefault()).format(Date())
    }

    // SharedPreferences에서 최근 검색어 로드
    LaunchedEffect(Unit) {
        try {
            val jsonString = sharedPrefs.getString("recent_book_searches", "[]") ?: "[]"
            val searches = Json.decodeFromString<List<String>>(jsonString)
            viewModel.setRecentSearches(searches)
        } catch (_: Exception) {
            viewModel.setRecentSearches(emptyList())
        }
    }

    // SharedPreferences에 최근 검색어 저장
    fun saveRecentSearches(searches: List<String>) {
        try {
            val jsonString = Json.encodeToString(ListSerializer(String.serializer()), searches)
            sharedPrefs.edit {
                putString("recent_book_searches", jsonString)
            }
        } catch (_: Exception) {
            // 저장 실패 시 무시
        }
    }

    // 최근 검색어 변경 감지하여 SharedPreferences에 저장
    LaunchedEffect(recentSearches) {
        if (recentSearches.isNotEmpty()) {
            saveRecentSearches(recentSearches)
        }
    }

    // 실시간 검색 (debounced)
    LaunchedEffect(searchText) {
        if (searchText.isNotBlank() && !isSearched) {
            delay(200) // 200ms 딜레이
            if (searchText.isNotBlank()) {
                viewModel.searchBooks(searchText, isNewSearch = true)
            }
        } else if (searchText.isBlank()) {
            viewModel.clearSearchResults()
        }
    }

    LaunchedEffect(isSearched) {
        if (isSearched) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LeftNameTopAppBar(
                title = stringResource(R.string.book_search_topappbar)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                SearchBookTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    hint = stringResource(R.string.book_search_hint),
                    text = searchText,
                    onValueChange = {
                        searchText = it
                        isSearched = false
                    },
                    onSearch = { query ->
                        if (query.isNotBlank()) {
                            viewModel.addRecentSearch(query)
                            viewModel.searchBooks(query, isNewSearch = true)
                        }
                        isSearched = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    searchText.isBlank() && !isSearched -> {
                        SearchRecentBook(
                            recentSearches = recentSearches,
                            popularBooks = mostSearchedBooks.map { 
                                BookData(
                                    title = it.title,
                                    publisher = "",
                                    imageUrl = it.imageUrl,
                                    isbn = it.isbn
                                )
                            },
                            popularBookDate = currentDate,
                            onSearchClick = { keyword ->
                                searchText = keyword
                                viewModel.searchBooks(keyword, isNewSearch = true)
                                isSearched = true
                            },
                            onRemove = { keyword ->
                                viewModel.removeRecentSearch(keyword)
                            },
                            onBookClick = { book ->
                                // 책 클릭 시 처리
                            }
                        )
                    }

                    searchText.isNotBlank() && !isSearched -> {
                        // 실시간 검색 결과 표시
                        if (bookSearchResults.isEmpty() && searchText.isNotBlank()) {
                            SearchEmptyResult(
                                mainText = stringResource(R.string.book_no_search_result1),
                                subText = stringResource(R.string.book_no_search_result2),
                                onRequestBook = onNavigateToRegisterBook
                            )
                        } else {
                            SearchActiveField(
                                bookList = bookSearchResults.map { 
                                    BookData(
                                        title = it.title,
                                        author = it.authorName,
                                        publisher = it.publisher,
                                        imageUrl = it.imageUrl,
                                        isbn = it.isbn
                                    )
                                },
                                hasMoreResults = hasMoreResults,
                                onLoadMore = { viewModel.loadMoreResults() }
                            )
                        }
                    }

                    isSearched -> {
                        SearchBookFilteredResult(
                            resultCount = totalElements,
                            bookList = bookSearchResults.map { 
                                BookData(
                                    title = it.title,
                                    author = it.authorName,
                                    publisher = it.publisher,
                                    imageUrl = it.imageUrl,
                                    isbn = it.isbn
                                )
                            },
                            hasMoreResults = hasMoreResults,
                            onRequestBook = onNavigateToRegisterBook,
                            onLoadMore = { viewModel.loadMoreResults() }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBookSearchScreen_Default() {
    ThipTheme {
        SearchBookScreen()
    }
}
