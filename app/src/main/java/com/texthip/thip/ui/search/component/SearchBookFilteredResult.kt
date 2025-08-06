package com.texthip.thip.ui.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.search.mock.BookData
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SearchBookFilteredResult(
    resultCount: Int,
    bookList: List<BookData>,
    hasMoreResults: Boolean = false,
    onRequestBook: () -> Unit = {},
    onLoadMore: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.group_searched_room_size, resultCount),
                color = colors.Grey,
                style = typography.menu_m500_s14_h24
            )
        }
        Spacer(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.DarkGrey02)
        )

        if (bookList.isEmpty()) {
            SearchEmptyResult(
                mainText = stringResource(R.string.book_no_search_result1),
                subText = stringResource(R.string.book_no_search_result2),
                onRequestBook = onRequestBook
            )
        } else {
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
                        imageUrl = book.imageUrl
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
    }
}

@Preview
@Composable
fun PreviewBookFilteredSearchResult() {
    ThipTheme {
        SearchBookFilteredResult(
            resultCount = 3,
            bookList = listOf(
                BookData(
                    title = "이기적 유전자",
                    author = "리처드 도킨스",
                    publisher = "을유문화사"
                ),
                BookData(
                    title = "총, 균, 쇠",
                    author = "재레드 다이아몬드",
                    publisher = "문학사상사"
                ),
                BookData(
                    title = "코스모스",
                    author = "칼 세이건",
                    publisher = "사이언스북스"
                )
            )
        )
    }
}
