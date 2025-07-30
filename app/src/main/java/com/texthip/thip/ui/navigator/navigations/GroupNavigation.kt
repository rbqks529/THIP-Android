package com.texthip.thip.ui.navigator.navigations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.group.data.viewmodel.GroupViewModel
import com.texthip.thip.ui.group.makeroom.screen.GroupMakeRoomScreen
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupMakeRoomViewModel
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.screen.GroupMyScreen
import com.texthip.thip.ui.group.room.screen.GroupRoomRecruitScreen
import com.texthip.thip.ui.group.room.screen.GroupRoomScreen
import com.texthip.thip.ui.group.screen.GroupDoneScreen
import com.texthip.thip.ui.group.screen.GroupScreen
import com.texthip.thip.ui.group.search.screen.GroupSearchScreen
import com.texthip.thip.ui.navigator.extensions.navigateBack
import com.texthip.thip.ui.navigator.extensions.navigateToAlarm
import com.texthip.thip.ui.navigator.extensions.navigateToGroupDone
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMakeRoom
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMy
import com.texthip.thip.ui.navigator.extensions.navigateToGroupRecruit
import com.texthip.thip.ui.navigator.extensions.navigateToRecommendedGroupRecruit
import com.texthip.thip.ui.navigator.extensions.navigateToGroupRoom
import com.texthip.thip.ui.navigator.extensions.navigateToGroupSearch
import com.texthip.thip.ui.navigator.routes.GroupRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Group
fun NavGraphBuilder.groupNavigation(navController: NavHostController) {
    // 메인 Group 화면
    composable<MainTabRoutes.Group> {
        val groupViewModel: GroupViewModel = hiltViewModel()
        
        GroupScreen(
            viewModel = groupViewModel,
            onNavigateToMakeRoom = {
                navController.navigateToGroupMakeRoom()
            },
            onNavigateToGroupDone = {
                navController.navigateToGroupDone()
            },
            onNavigateToAlarm = {
                navController.navigateToAlarm()
            },
            onNavigateToGroupSearch = {
                navController.navigateToGroupSearch()
            },
            onNavigateToGroupMy = {
                navController.navigateToGroupMy()
            },
            onNavigateToGroupRecruit = { roomId ->
                navController.navigateToGroupRecruit(roomId)
            },
            onNavigateToGroupRoom = { roomId ->
                navController.navigateToGroupRoom(roomId)
            }
        )
    }
    
    // Group MakeRoom 화면
    composable<GroupRoutes.MakeRoom> {
        val viewModel: GroupMakeRoomViewModel = hiltViewModel()
        GroupMakeRoomScreen(
            viewModel = viewModel,
            onNavigateBack = {
                navController.navigateBack()
            },
            onGroupCreated = {
                navController.navigateBack()
            }
        )
    }
    
    // Group Done 화면
    composable<GroupRoutes.Done> {
        val groupViewModel: GroupViewModel = hiltViewModel()
        val userName by groupViewModel.userName.collectAsState()
        val doneGroups by groupViewModel.doneGroups.collectAsState()
        
        GroupDoneScreen(
            name = userName,
            allDataList = doneGroups,
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
    
    // Group My 화면
    composable<GroupRoutes.My> {
        val groupViewModel: GroupViewModel = hiltViewModel()
        val myRoomGroups by groupViewModel.myRoomGroups.collectAsState()
        
        GroupMyScreen(
            allDataList = myRoomGroups,
            onCardClick = { room ->
                groupViewModel.onRoomCardClick(
                    room,
                    onNavigateToRecruit = { roomId ->
                        navController.navigateToGroupRecruit(roomId)
                    },
                    onNavigateToRoom = { roomId ->
                        navController.navigateToGroupRoom(roomId)
                    }
                )
            },
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
    
    // Group Search 화면
    composable<GroupRoutes.Search> {
        val groupViewModel: GroupViewModel = hiltViewModel()
        val searchGroups by groupViewModel.searchGroups.collectAsState()
        
        GroupSearchScreen(
            roomList = searchGroups,
            onNavigateBack = {
                navController.navigateBack()
            },
            onRoomClick = { room ->
                groupViewModel.onRoomCardClick(
                    room,
                    onNavigateToRecruit = { roomId ->
                        navController.navigateToGroupRecruit(roomId)
                    },
                    onNavigateToRoom = { roomId ->
                        navController.navigateToGroupRoom(roomId)
                    }
                )
            }
        )
    }
    
    // Group Recruit 화면
    composable<GroupRoutes.Recruit> { backStackEntry ->
        val route = backStackEntry.toRoute<GroupRoutes.Recruit>()
        val roomId = route.roomId
        val groupViewModel: GroupViewModel = hiltViewModel()
        
        // suspend 함수를 위한 LaunchedEffect 사용
        var roomDetail by remember { mutableStateOf<GroupRoomData?>(null) }
        LaunchedEffect(roomId) {
            roomDetail = groupViewModel.getRoomDetail(roomId)
        }
        
        roomDetail?.let { detail ->
            GroupRoomRecruitScreen(
                detail = detail,
                buttonType = GroupBottomButtonType.JOIN, // 기본값, 실제로는 사용자 상태에 따라 결정
                onRecommendationClick = { recommendation ->
                    navController.navigateToRecommendedGroupRecruit(recommendation.id)
                },
                onParticipation = {
                    // 참여 로직
                },
                onCancelParticipation = {
                    // 참여 취소 로직
                },
                onCloseRecruitment = {
                    // 모집 마감 로직
                },
                onBackClick = {
                    navController.navigateBack()
                }
            )
        } ?: run {
            // 로딩 중이거나 데이터를 찾을 수 없는 경우
        }
    }
    
    // Group Room 화면
    composable<GroupRoutes.Room> { backStackEntry ->
        val route = backStackEntry.toRoute<GroupRoutes.Room>()
        val roomId = route.roomId
        val groupViewModel: GroupViewModel = hiltViewModel()
        
        // suspend 함수를 위한 LaunchedEffect 사용
        var roomDetail by remember { mutableStateOf<GroupRoomData?>(null) }
        LaunchedEffect(roomId) {
            roomDetail = groupViewModel.getRoomDetail(roomId)
        }
        
        roomDetail?.let {
            GroupRoomScreen(
                onBackClick = {
                    navController.navigateBack()
                }
            )
        } ?: run {
            // 로딩 중이거나 데이터를 찾을 수 없는 경우
        }
    }
}