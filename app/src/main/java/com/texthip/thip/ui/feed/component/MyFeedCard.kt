package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun MyFeedCard(
    modifier: Modifier = Modifier,
    feedItem: FeedItem,
    onLikeClick: () -> Unit = {},
    onContentClick: () -> Unit = {}
) {
    val images = feedItem.imageUrls.orEmpty().map { painterResource(id = it) }
    val hasImages = images.isNotEmpty()
    val maxLines = if (hasImages) 3 else 8

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        ActionBookButton(
            bookTitle = feedItem.bookTitle,
            bookAuthor = feedItem.authName,
            onClick = {}
        )

        Text(
            text = feedItem.content,
            style = typography.feedcopy_r400_s14_h20,
            color = colors.White,
            maxLines = maxLines,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable { onContentClick() }
        )

        if (hasImages) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                images.take(3).forEach { image ->
                    Image(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable { onLikeClick() },
                painter = painterResource(if (feedItem.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = feedItem.likeCount.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
                modifier = Modifier.padding(start = 5.dp, end = 12.dp)
            )
            Icon(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = null,
                tint = colors.White
            )
            Text(
                text = feedItem.commentCount.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
                modifier = Modifier.padding(start = 5.dp, end = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (feedItem.isLocked) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyFeedCardPrev() {
    val feed1 = FeedItem(
        id = 1,
        userProfileImage = R.drawable.character_literature,
        userName = "user.01",
        userRole = stringResource(R.string.influencer),
        bookTitle = "책 제목",
        authName = "한강",
        timeAgo = "3시간 전",
        content = "무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷",
        likeCount = 10,
        commentCount = 5,
        isLiked = false,
        isSaved = true,
        isLocked = true,
        imageUrls = null
    )
    val feed2 = FeedItem(
        id = 2,
        userProfileImage = R.drawable.character_art,
        userName = "user.01",
        userRole = stringResource(R.string.influencer),
        bookTitle = "책 제목",
        authName = "한강",
        timeAgo = "3시간 전",
        content = "세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!세 줄만 보여줄거임!!",
        likeCount = 10,
        commentCount = 5,
        isLiked = false,
        isSaved = true,
        isLocked = false,
        imageUrls = listOf(R.drawable.img_book_cover_sample, R.drawable.img_book_cover_sample)
    )

    Column {
        MyFeedCard(
            feedItem = feed1
        )
        MyFeedCard(
            feedItem = feed2
        )
    }


}