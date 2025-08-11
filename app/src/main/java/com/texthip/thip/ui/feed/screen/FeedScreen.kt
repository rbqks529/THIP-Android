package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FloatingButton
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.header.HeaderMenuBarTab
import com.texthip.thip.ui.common.topappbar.LogoTopAppBar
import com.texthip.thip.ui.feed.component.FeedSubscribeBarlist
import com.texthip.thip.ui.feed.component.MyFeedCard
import com.texthip.thip.ui.feed.component.MySubscribeBarlist
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.mypage.component.SavedFeedCard
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FeedScreen(
    navController: NavController? = null,
    nickname: String = "",
    userRole: String = "",
    feeds: List<FeedItem> = emptyList(),
    totalFeedCount: Int = 0,
    selectedTabIndex: Int = 0,
    followerProfileImageUrls: List<String> = emptyList()
) {
    val selectedIndex = rememberSaveable { mutableIntStateOf(selectedTabIndex) }
    val feedStateList = remember {
        mutableStateListOf<FeedItem>().apply {
            addAll(feeds)
        }
    }
    val mySubscriptions = listOf(
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image1.jpg",
            nickname = "abcabcabcabc",
            role = "문학가",
            roleColor = colors.SocialScience
        ),
        MySubscriptionData(
            profileImageUrl = "https://example.com/image.jpg",
            nickname = "aaaaaaa",
            role = "작가",
            roleColor = colors.SocialScience
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LogoTopAppBar(
                leftIcon = painterResource(R.drawable.ic_plusfriend),
                hasNotification = false,
                onLeftClick = {},
                onRightClick = {},
            )
            Spacer(modifier = Modifier.height(32.dp))
            HeaderMenuBarTab(
                titles = listOf("피드", "내 피드"),
                selectedTabIndex = selectedIndex.value,
                onTabSelected = { selectedIndex.value = it }
            )

            // 스크롤 영역 전체
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedIndex.value == 1) {
                    // 내 피드
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        AuthorHeader(
                            profileImage = null,
                            nickname = nickname,
                            badgeText = userRole,
                            buttonText = "",
                            buttonWidth = 60.dp,
                            showButton = false
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FeedSubscribeBarlist(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            followerProfileImageUrls = followerProfileImageUrls,
                            onClick = {
                            }
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = stringResource(R.string.whole_num, totalFeedCount),
                            style = typography.menu_m500_s14_h24,
                            color = colors.Grey,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp, start = 20.dp)
                        )
                        HorizontalDivider(
                            color = colors.DarkGrey02,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    if (totalFeedCount == 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 110.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = stringResource(R.string.create_feed),
                                    style = typography.smalltitle_sb600_s18_h24,
                                    color = colors.Grey
                                )
                            }
                        }
                    } else {
                        itemsIndexed(feedStateList, key = { _, item -> item.id }) { index, feed ->
                            Spacer(modifier = Modifier.height(if (index == 0) 20.dp else 40.dp))
                            MyFeedCard(
                                feedItem = feed,
                                onLikeClick = {
                                    val updated = feed.copy(
                                        isLiked = !feed.isLiked,
                                        likeCount = if (feed.isLiked) feed.likeCount - 1 else feed.likeCount + 1
                                    )
                                    feedStateList[index] = updated
                                },
                                onContentClick = {} //FeedCommentScreen으로
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            if (index != feeds.lastIndex) {
                                HorizontalDivider(
                                    color = colors.DarkGrey02,
                                    thickness = 10.dp
                                )
                            }
                        }
                    }
                } else {
                    //피드
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        MySubscribeBarlist(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            subscriptions = mySubscriptions,
                            onClick = {
                            }
                        )
                    }
                    itemsIndexed(feedStateList, key = { _, item -> item.id }) { index, feed ->
                        val profileImage = feed.userProfileImage?.let { painterResource(it) }

                        SavedFeedCard(
                            feedItem = feed,
                            profileImage = profileImage,
                            onBookmarkClick = {
                                val updated = feed.copy(isSaved = !feed.isSaved)
                                feedStateList[index] = updated
                            },
                            onLikeClick = {
                                val updated = feed.copy(
                                    isLiked = !feed.isLiked,
                                    likeCount = if (feed.isLiked) feed.likeCount - 1 else feed.likeCount + 1
                                )
                                feedStateList[index] = updated
                            },
                            onContentClick = {} //FeedCommentScreen으로

                        )
                        if (index != feeds.lastIndex) {
                            HorizontalDivider(
                                color = colors.DarkGrey02,
                                thickness = 10.dp
                            )
                        }
                    }
                }
            }
        }
        FloatingButton(
            icon = painterResource(id = R.drawable.ic_write),
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    ThipTheme {
        val mockFeeds = List(5) {
            FeedItem(
                id = it + 1,
                userProfileImage = R.drawable.character_literature,
                userName = "user.$it",
                userRole = "문학 칭호",
                bookTitle = "책 제목 ",
                authName = "한강",
                timeAgo = "1시간 전",
                content = "내용내용내용 입니다. 내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.내용내용내용 입니다.",
                likeCount = it,
                commentCount = it,
                isLiked = false,
                isSaved = false,
                isLocked = it % 2 == 0,
                imageUrls = listOf(R.drawable.img_book_cover_sample)
            )
        }
        val mockFollowerImages = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg",
            "https://example.com/image5.jpg"
        )

        ThipTheme {
            FeedScreen(
                nickname = "ThipUser01",
                userRole = "문학 칭호",
                selectedTabIndex = 0,
                feeds = mockFeeds,
                totalFeedCount = mockFeeds.size,
                followerProfileImageUrls = mockFollowerImages
            )
        }
    }
}
