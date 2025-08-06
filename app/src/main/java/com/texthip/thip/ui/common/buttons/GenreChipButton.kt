package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GenreChipButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
    onCloseClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clickable {
                onClick()
            }
            .border(
                width = 1.dp,
                color = colors.Grey02,
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp, start = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = text,
                color = colors.Grey01,
                style = typography.info_r400_s12,
            )
            Icon(
                painter = painterResource(R.drawable.ic_x_20),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onCloseClick()
                    }
            )
        }
    }
}

@Preview
@Composable
private fun GenreChipButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
    ) {
        GenreChipButton(
            text = stringResource(R.string.essay),
        )
    }
}