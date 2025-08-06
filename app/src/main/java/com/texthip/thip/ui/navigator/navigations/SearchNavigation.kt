package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.extensions.navigateToRegisterBook
import com.texthip.thip.ui.search.screen.SearchBookScreen

fun NavGraphBuilder.searchNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Search> {
        SearchBookScreen(
            onNavigateToRegisterBook = { navController.navigateToRegisterBook() }
        )
    }
}