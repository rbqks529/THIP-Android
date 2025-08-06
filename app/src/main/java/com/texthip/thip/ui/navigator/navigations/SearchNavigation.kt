package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.SearchRoutes
import com.texthip.thip.ui.navigator.extensions.navigateToRegisterBook
import com.texthip.thip.ui.navigator.extensions.navigateToSearchBookDetail
import com.texthip.thip.ui.search.screen.SearchBookScreen
import com.texthip.thip.ui.search.screen.SearchBookDetailScreen

fun NavGraphBuilder.searchNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Search> {
        SearchBookScreen(
            onNavigateToRegisterBook = { navController.navigateToRegisterBook() },
            onNavigateToBookDetail = { isbn ->
                navController.navigateToSearchBookDetail(isbn)
            }
        )
    }
    
    composable<SearchRoutes.BookDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<SearchRoutes.BookDetail>()
        SearchBookDetailScreen(
            isbn = args.isbn,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}