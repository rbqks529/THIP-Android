package com.texthip.thip.ui.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.search.mock.BookData
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun SearchActiveField(
    bookList: List<BookData>,
    hasMoreResults: Boolean = false,
    onLoadMore: () -> Unit = {},
    onBookClick: (BookData) -> Unit = {}
) {
    val listState = rememberLazyListState()
    
    // 스크롤 끝에 도달했을 때 더 로드
    val shouldLoadMore = remember(bookList.size, hasMoreResults) {
        derivedStateOf {
            if (!hasMoreResults || bookList.isEmpty()) return@derivedStateOf false
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisibleIndex >= bookList.size - 3
        }
    }
    
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }
    
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(bookList) { index, book ->
            CardBookList(
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                imageUrl = book.imageUrl,
                onClick = { onBookClick(book) }
            )
            if (index < bookList.size - 1) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.DarkGrey02)
                )
            }
        }
    }
}