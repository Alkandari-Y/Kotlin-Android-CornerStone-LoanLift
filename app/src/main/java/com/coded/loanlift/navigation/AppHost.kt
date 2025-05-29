package com.coded.loanlift.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.coded.loanlift.dashboardscreen.DashboardScreen
import com.coded.loanlift.login.ForgotPasswordScreen
import com.coded.loanlift.login.LoginScreen
import com.coded.loanlift.signUp.SignUpScreen
import androidx.compose.animation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
        startDestination = startDestination,
//        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
//        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
//        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
//        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
    ) {
        composable(NavRoutesEnum.NAV_ROUTE_LOGIN.value) {
            LoginScreen(
                navController = navController,
                onForgotPasswordClick = {
                    navController.navigate(NavRoutesEnum.NAV_ROUTE_FORGOT_PASSWORD.value)
                }
            )
        }

        composable(NavRoutesEnum.NAV_ROUTE_SIGNUP.value) {
            SignUpScreen(navController = navController)
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
            DashboardScreen()
        }

        }
    }
