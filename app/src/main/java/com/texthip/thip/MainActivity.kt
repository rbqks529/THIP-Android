package com.texthip.thip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThipTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
                MainScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier,
            style = typography.bigtitle_b700_s22_h24,
            color = colors.Purple,
        )

        Text(
            text = "Hello $name!",
            modifier = modifier,
            style = typography.smalltitle_sb600_s16_h20,
            color = colors.NeonGreen,
        )

        Text(
            text = "Hello $name!",
            modifier = modifier,
            style = typography.menu_sb600_s12,
            color = colors.Red,
        )

        Text(
            text = "Hello $name!",
            modifier = modifier,
            style = typography.navi_m500_s10,
            color = colors.DarkGrey,
        )

        Text(
            text = "Hello $name!",
            modifier = modifier,
            style = typography.view_r400_s11_h20,
            color = colors.Black,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ThipTheme {
        Greeting("Android")
    }
}