package com.texthip.thip.ui.mypage.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit
) {
    var isChecked by rememberSaveable { mutableStateOf(true) }
    var toastMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var toastDateTime by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3000)
            toastMessage = null
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 2000)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 2000)
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .zIndex(1f)
        ) {
            toastMessage?.let { message ->
                ToastWithDate(
                    message = stringResource(
                        if (message == "push_on") R.string.push_on else R.string.push_off
                    ),
                    date = toastDateTime,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Column(
            Modifier
                .background(colors.Black)
                .fillMaxSize()
        ) {
            DefaultTopAppBar(
                title = stringResource(R.string.notification_settings),
                onLeftClick = onNavigateBack,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.push_notification),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.notification_description),
                        style = typography.menu_r400_s14_h24,
                        color = colors.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .weight(1f)
                    )
                    ToggleSwitchButton(
                        isChecked = isChecked,
                        onToggleChange = {
                            isChecked = it
                            toastMessage = if (it) "push_on" else "push_off"
                            // 토글 버튼 클릭 시점의 현재 시간 저장
                            val dateFormat = SimpleDateFormat("yyyy년 M월 d일 H시 m분", Locale.KOREAN)
                            toastDateTime = dateFormat.format(Date())
                        }
                    )
                }

            }


        }
    }
}

@Preview
@Composable
private fun NotificationScreenPrev() {
    NotificationScreen(
        onNavigateBack = {}
    )
}