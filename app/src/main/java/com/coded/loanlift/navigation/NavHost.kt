package com.coded.loanlift.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coded.loanlift.dashboardscreen.DashboardScreen
import com.coded.loanlift.login.ForgotPasswordScreen
import com.coded.loanlift.login.LoginScreen
import com.coded.loanlift.signUp.SignUpScreen

enum class NavRoutesEnum(val value: String) {
    NAV_ROUTE_LOGIN("login"),
    NAV_ROUTE_SIGNUP("signup"),
    NAV_ROUTE_FORGOT_PASSWORD("forgot_password"),
    NAV_ROUTE_DASHBOARD("dashboard"),
    NAV_ROUTE_LOADING_DASHBOARD("loading_dashboard"),
}

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavRoutesEnum.NAV_ROUTE_LOGIN.value
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        // Login
        composable(NavRoutesEnum.NAV_ROUTE_LOGIN.value) {
            LoginScreen(
                navController = navController,
                onForgotPasswordClick = {
                    navController.navigate(NavRoutesEnum.NAV_ROUTE_FORGOT_PASSWORD.value)
                }
            )
        }

// SignUp
        composable(NavRoutesEnum.NAV_ROUTE_SIGNUP.value) {
            SignUpScreen(
                navController = navController
            )
        }


        //  Forgot Password
        composable(NavRoutesEnum.NAV_ROUTE_FORGOT_PASSWORD.value) {
            ForgotPasswordScreen(
                onSubmitClick = {
                    navController.popBackStack(NavRoutesEnum.NAV_ROUTE_LOGIN.value, inclusive = false)
                }
            )
        }

        //Dashboard
        composable(NavRoutesEnum.NAV_ROUTE_DASHBOARD.value) {
            DashboardScreen()
        }

        // Loading before Dashboard
        composable(NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value) {
            LoadingDashboardScreen(navController = navController)
        }

    }
}
