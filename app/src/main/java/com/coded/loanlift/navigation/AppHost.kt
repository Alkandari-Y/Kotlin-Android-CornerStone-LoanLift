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
import com.coded.loanlift.screens.kyc.KycScreen
import com.coded.loanlift.screens.accounts.AccountCreateScreen
import com.coded.loanlift.screens.accounts.AccountDetailsScreen
import com.coded.loanlift.screens.campaigns.AllCampaignsOwnerScreen
import com.coded.loanlift.screens.campaigns.CampaignOwnerDetailsScreen
import com.coded.loanlift.screens.pledges.PledgeDetailsScreen
import com.coded.loanlift.viewModels.AuthViewModel
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.KycViewModel
import com.coded.loanlift.viewModels.PledgeDetailsViewModel

object NavRoutes {
    const val NAV_ROUTE_LOGIN = "login"
    const val NAV_ROUTE_SIGNUP = "signup"
    const val NAV_ROUTE_FORGOT_PASSWORD = "forgot_password"
    const val NAV_ROUTE_DASHBOARD = "dashboard"
    const val NAV_ROUTE_LOADING_DASHBOARD = "loading_dashboard"


    const val NAV_ROUTE_CREATE_ACCOUNT = "accounts/create"
    const val NAV_ROUTE_ACCOUNT_DETAILS = "accounts/manage/{accountNum}"


    const val NAV_ROUTE_CAMPAIGN_OWNER_VIEW_ALL = "campaigns/manage"
    const val NAV_ROUTE_CAMPAIGN_OWNER_DETAILS = "campaigns/manage/{campaignId}"
    const val NAV_ROUTE_CREATE_CAMPAIGN = "campaigns/manage/create"

    const val NAV_ROUTE_CREATE_PLEDGE = "pledges/create"
    const val NAV_ROUTE_PLEDGE_DETAILS = "pledges/manage/{pledgeId}"

    const val NAV_ROUTE_EDIT_KYC = "/kyc"

    fun accountDetailRoute(accountNum: String) = "accounts/manage/${accountNum}"
    fun campaignOwnerDetailRoute(campaignId: Long) = "campaigns/manage/${campaignId}"
    fun pledgeDetailRoute(pledgeId: Long) = "pledges/manage/${pledgeId}"


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val dashboardViewModel = remember { DashboardViewModel(context) }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoutes.NAV_ROUTE_LOGIN

    ) {

        composable(NavRoutes.NAV_ROUTE_LOGIN) {
            val authViewModel = remember { AuthViewModel(context) }

            LoginScreen(
                navController = navController,
                onForgotPasswordClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_FORGOT_PASSWORD)
                },
                viewModel = authViewModel
            )
        }

        composable(NavRoutes.NAV_ROUTE_SIGNUP) {
            val authViewModel = remember { AuthViewModel(context) }
            SignUpScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(NavRoutes.NAV_ROUTE_FORGOT_PASSWORD) {
            ForgotPasswordScreen {
                navController.popBackStack(NavRoutes.NAV_ROUTE_LOGIN, false)
            }
        }

        composable(NavRoutes.NAV_ROUTE_LOADING_DASHBOARD) {
            LoadingDashboardScreen(
                navController = navController,
                viewModel= dashboardViewModel
            )
        }

        composable(NavRoutes.NAV_ROUTE_DASHBOARD) {
            DashboardScreen(
                navController = navController,
                viewModel = dashboardViewModel,
                onLogoutClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAccountClick = { accountNum: String ->
                    navController.navigate(NavRoutes.accountDetailRoute(accountNum))
                },
                onCampaignClick = { campaignId: Long ->
                    navController.navigate(NavRoutes.campaignOwnerDetailRoute(campaignId))
                },
                onPledgeCLick = { pledgeId: Long ->
                    navController.navigate(NavRoutes.pledgeDetailRoute(pledgeId))
                },
                onAccountCreateClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_CREATE_ACCOUNT)
                },
                onCampaignCreateClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_CREATE_CAMPAIGN)
                },
                onPledgeCreateClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_CREATE_PLEDGE)
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_EDIT_KYC)
                },
                onViewAllCampaignsClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_CAMPAIGN_OWNER_VIEW_ALL)
                }
            )
        }

        composable(NavRoutes.NAV_ROUTE_CAMPAIGN_OWNER_DETAILS) { backStackEntry ->
            val campaignId = backStackEntry.arguments?.getString("campaignId")
            if (campaignId != null) {
                CampaignOwnerDetailsScreen(
                    navController = navController,
                    viewModel = dashboardViewModel,
                    campaignId = campaignId.toLong(),
                    onBackClick = { navController.popBackStack() },
                )
            }
        }

        // these screens need to be updated
        composable(NavRoutes.NAV_ROUTE_ACCOUNT_DETAILS) {
            AccountDetailsScreen(
                onCampaignClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.NAV_ROUTE_CREATE_ACCOUNT) {
            AccountCreateScreen(navController = navController)
        }

        composable(NavRoutes.NAV_ROUTE_EDIT_KYC) {
            val kycViewModel = remember { KycViewModel(context) }
            KycScreen(
                navController = navController,
                viewModel= kycViewModel)
        }

        composable(NavRoutes.NAV_ROUTE_CAMPAIGN_OWNER_VIEW_ALL) {
            AllCampaignsOwnerScreen(
                navController = navController,
                viewModel = dashboardViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onCampaignClick = { campaignId: Long ->
                    navController.navigate(NavRoutes.campaignOwnerDetailRoute(campaignId))
                },
            )
        }

        composable(NavRoutes.NAV_ROUTE_PLEDGE_DETAILS) { backStackEntry ->
            val pledgeId = backStackEntry.arguments?.getString("pledgeId")

            val pledgeDetailsViewModel = remember { PledgeDetailsViewModel(context) }
            if (pledgeId != null) {
                PledgeDetailsScreen(
                    viewModel = pledgeDetailsViewModel,
                    pledgeId = pledgeId.toLong()
                )
            }
        }
    }
}
