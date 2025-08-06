package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardBookList(
    modifier: Modifier = Modifier,
    title: String,
    author: String,
    publisher: String,
    imageUrl: String? = null, // API에서 받은 이미지 URL
    onClick: () -> Unit = {},
    isBookmarked: Boolean = false,
    onBookmarkClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .background(Color.Transparent),
    ) {
        // 책 이미지
        AsyncImage(
            model = imageUrl ?: R.drawable.bookcover_sample,
            contentDescription = "책 이미지",
            modifier = Modifier.size(width = 80.dp, height = 108.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 텍스트 정보
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = title,
                style = typography.smalltitle_sb600_s16_h20,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colors.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$author 저 · $publisher",
                style = typography.view_m500_s12_h20,
                color = colors.Grey01
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 북마크 아이콘 제거(쓰는 화면이 안보임)
        /*IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (isBookmarked) ImageVector.vectorResource(R.drawable.ic_save_filled) else ImageVector.vectorResource(R.drawable.ic_save),
                contentDescription = "북마크",
                tint = if (isBookmarked) colors.Purple else colors.Grey01
            )
        }*/
    }
}

// 프리뷰들
@Preview
@Composable
fun PreviewBookTitleCard() {
    var isBookmarked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardBookList(
            title = "책제목입니다.책제목입니다.책제목입니다.책제목입니다.책제목입니다.책제목입니다.",
            author = "리처드 도킨스",
            publisher = "을유문화사",
            isBookmarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }

}