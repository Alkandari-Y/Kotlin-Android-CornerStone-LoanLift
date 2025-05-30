package com.coded.loanlift.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.coded.loanlift.screens.home.DashboardScreen
import com.coded.loanlift.screens.auth.ForgotPasswordScreen
import com.coded.loanlift.screens.auth.LoginScreen
import com.coded.loanlift.screens.auth.SignUpScreen
import androidx.compose.animation.*
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.viewModels.AuthViewModel
import com.coded.loanlift.viewModels.DashboardViewModel

enum class NavRoutesEnum(val value: String) {
    NAV_ROUTE_LOGIN("login"),
    NAV_ROUTE_SIGNUP("signup"),
    NAV_ROUTE_FORGOT_PASSWORD("forgot_password"),
    NAV_ROUTE_DASHBOARD("dashboard"),
    NAV_ROUTE_LOADING_DASHBOARD("loading_dashboard"),
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavRoutesEnum.NAV_ROUTE_LOGIN.value
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (TokenManager.getToken(LocalContext.current) != null &&
            TokenManager.isRememberMeEnabled(LocalContext.current) &&
            !TokenManager.isAccessTokenExpired(LocalContext.current)) {
            NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value
        } else {
            startDestination
        }
//        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
//        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
//        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
//        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
    ) {

        composable(NavRoutesEnum.NAV_ROUTE_LOGIN.value) {
            val context = LocalContext.current
            val authViewModel = remember { AuthViewModel(context) }

            LoginScreen(
                navController = navController,
                onForgotPasswordClick = {
                    navController.navigate(NavRoutesEnum.NAV_ROUTE_FORGOT_PASSWORD.value)
                },
                viewModel = authViewModel
            )
        }

        composable(NavRoutesEnum.NAV_ROUTE_SIGNUP.value) {
            val context = LocalContext.current
            val authViewModel = remember { AuthViewModel(context) }
            SignUpScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(NavRoutesEnum.NAV_ROUTE_FORGOT_PASSWORD.value) {
            ForgotPasswordScreen {
                navController.popBackStack(NavRoutesEnum.NAV_ROUTE_LOGIN.value, false)
            }
        }

        composable(NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value) {
            LoadingDashboardScreen(navController = navController)
        }

        composable(NavRoutesEnum.NAV_ROUTE_DASHBOARD.value) {
            val context = LocalContext.current
            val dashboardViewModel = remember { DashboardViewModel(context) }
            DashboardScreen(
                navController = navController,
                viewModel = dashboardViewModel
            )
        }
    }
}
