package com.texthip.thip.ui.navigator.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.texthip.thip.ui.common.alarmpage.screen.AlarmScreen
import com.texthip.thip.ui.common.alarmpage.viewmodel.AlarmViewModel
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.navigator.extensions.navigateBack

// Common 관련 네비게이션
fun NavGraphBuilder.commonNavigation(navController: NavHostController) {
    // Alarm 화면
    composable<CommonRoutes.Alarm> {
        val alarmViewModel: AlarmViewModel = hiltViewModel()
        val alarmItems by alarmViewModel.alarmItems.collectAsState()
        
        AlarmScreen(
            alarmItems = alarmItems,
            onCardClick = { alarmViewModel.onCardClick(it) },
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
}