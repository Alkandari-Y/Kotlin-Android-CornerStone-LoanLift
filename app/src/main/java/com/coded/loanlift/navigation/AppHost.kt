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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.screens.kyc.KycScreen
import com.coded.loanlift.screens.accounts.AccountCreateScreen
import com.coded.loanlift.screens.accounts.AccountDetailsScreen
import com.coded.loanlift.screens.accounts.AllAccountsScreen
import com.coded.loanlift.screens.pledges.PledgeDetailsScreen
import com.coded.loanlift.screens.accounts.TransferScreen
import com.coded.loanlift.viewModels.AccountViewModel
import com.coded.loanlift.screens.campaigns.owner.AllCampaignsOwnerScreen
import com.coded.loanlift.screens.campaigns.general.AllPublicActiveCampaignsScreen
import com.coded.loanlift.screens.campaigns.owner.CampaignOwnerDetailsScreen
import com.coded.loanlift.screens.campaigns.general.PublicCampaignDetailsScreen
import com.coded.loanlift.screens.campaigns.owner.CampaignCreateScreen
import com.coded.loanlift.screens.pledges.AllPledgesScreen
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
    const val NAV_ROUTE_ACCOUNT_VIEW_ALL = "accounts"

    const val NAV_ROUTE_CAMPAIGN_OWNER_VIEW_ALL = "campaigns/manage"
    const val NAV_ROUTE_CAMPAIGN_OWNER_DETAILS = "campaigns/manage/{campaignId}"
    const val NAV_ROUTE_CREATE_CAMPAIGN = "campaigns/manage/create"
    const val NAV_ROUTE_CAMPAIGN_EXPLORE = "campaigns/explore"
    const val NAV_ROUTE_CAMPAIGN_PUBLIC_DETAILS = "campaigns/explore/{campaignId}"

    const val NAV_ROUTE_CREATE_PLEDGE = "pledges/create"
    const val NAV_ROUTE_PLEDGE_DETAILS = "pledges/manage/{pledgeId}"
    const val NAV_ROUTE_PLEDGE_VIEW_ALL = "pledges"

    const val NAV_ROUTE_EDIT_KYC = "/kyc"

    const val NAV_ROUTE_TRANSFER = "accounts/transfer/{sourceAccount}"

    fun accountDetailRoute(accountNum: String) = "accounts/manage/$accountNum"
    fun campaignOwnerDetailRoute(campaignId: Long) = "campaigns/manage/$campaignId"
    fun campaignPublicDetailRoute(campaignId: Long) = "campaigns/explore/$campaignId"

    fun pledgeDetailRoute(pledgeId: Long) = "pledges/manage/${pledgeId}"
    fun transferRoute(sourceAccount: String) = "accounts/transfer/$sourceAccount"

}

@Composable
fun AppHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val dashboardViewModel = remember { DashboardViewModel(context) }


    LaunchedEffect(Unit){
        if (
            TokenManager.getToken(context) != null &&
                    TokenManager.isRememberMeEnabled(context) &&
                    !TokenManager.isAccessTokenExpired(context)
            ) {
                navController.navigate(NavRoutes.NAV_ROUTE_DASHBOARD)
            }
    }

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
                onAccountClick = { accountNum ->
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
                    navController.navigate(NavRoutes.NAV_ROUTE_CAMPAIGN_EXPLORE)
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_EDIT_KYC)
                },
                onViewAllCampaignsClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_CAMPAIGN_OWNER_VIEW_ALL)
                },
                onExploreAllCampaignsClick = {
                    navController.navigate(NavRoutes.NAV_ROUTE_CAMPAIGN_EXPLORE)
                },
                onViewAllAccounts = {
                    navController.navigate(NavRoutes.NAV_ROUTE_ACCOUNT_VIEW_ALL)
                },
                onViewAllPledges = {
                    navController.navigate(NavRoutes.NAV_ROUTE_PLEDGE_VIEW_ALL)
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
                    onAccountClick = { accountNum ->
                        navController.navigate(NavRoutes.accountDetailRoute(accountNum))
                    }
                )
            }
        }

        composable(NavRoutes.NAV_ROUTE_ACCOUNT_DETAILS) { backStackEntry ->
            val accountNum = backStackEntry.arguments?.getString("accountNum")
            if (accountNum != null) {
                AccountDetailsScreen(
                    onBackClick = { navController.popBackStack() },
                    onTransferClick = { navController.navigate(NavRoutes.NAV_ROUTE_TRANSFER) },
                    viewModel = dashboardViewModel,
                    accountNum = accountNum
                )
            }
        }

        composable(NavRoutes.NAV_ROUTE_CREATE_ACCOUNT) {
            val accountViewModel = remember { AccountViewModel(context) }
            AccountCreateScreen(navController = navController, viewModel = accountViewModel)
        }

        composable(NavRoutes.NAV_ROUTE_EDIT_KYC) {
            val kycViewModel = remember { KycViewModel(context) }
            KycScreen(
                navController = navController,
                viewModel= kycViewModel)
        }

        composable(NavRoutes.NAV_ROUTE_TRANSFER) { backStackEntry ->
            val sourceAccount = backStackEntry.arguments?.getString("sourceAccount") ?: ""
            TransferScreen(
                viewModel = dashboardViewModel,
                sourceAccountNumber = sourceAccount,
                onBackClick = { navController.popBackStack() }
            )
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
                    navController = navController,
                    pledgeId = pledgeId.toLong()
                )
            }
        }

        composable(NavRoutes.NAV_ROUTE_CREATE_CAMPAIGN) {
            CampaignCreateScreen(
                viewModel = dashboardViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.NAV_ROUTE_CAMPAIGN_EXPLORE) {
            AllPublicActiveCampaignsScreen (
                viewModel = dashboardViewModel,
                navController = navController,
                onBackClick = {
                    navController.popBackStack()
                },
                onCampaignClick = { campaignId: Long ->
                    navController.navigate(NavRoutes.campaignPublicDetailRoute(campaignId))
                }
            )
        }

        composable(NavRoutes.NAV_ROUTE_CAMPAIGN_PUBLIC_DETAILS) { backStackEntry ->
            val campaignId = backStackEntry.arguments?.getString("campaignId")
            if (campaignId != null) {
                PublicCampaignDetailsScreen(
                    viewModel = dashboardViewModel,
                    navController = navController,
                    campaignId = campaignId.toLong(),
                    onBackClick = { navController.popBackStack() },
                    onPledgeDetailClick = { pledgeId ->
                        navController.navigate(NavRoutes.pledgeDetailRoute(pledgeId))
                    }
                )
            }
        }

        composable(NavRoutes.NAV_ROUTE_ACCOUNT_VIEW_ALL) {
            AllAccountsScreen(
                navController = navController,
                viewModel = dashboardViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onAccountClick = { accountNum: String ->
                    navController.navigate(NavRoutes.accountDetailRoute(accountNum))
                },
            )
        }

        composable(NavRoutes.NAV_ROUTE_PLEDGE_VIEW_ALL) {
            AllPledgesScreen(
                navController = navController,
                viewModel = dashboardViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onPledgeClick = { pledgeId ->
                    navController.navigate(NavRoutes.pledgeDetailRoute(pledgeId))

                }
            )
        }
    }
}
