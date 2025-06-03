package com.coded.loanlift.composables.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.pledges.PledgeCard
import com.coded.loanlift.data.response.pledges.UserPledgeDto

@Composable
fun PledgesSection(
    pledges: List<UserPledgeDto>,
    onPledgeClick: (Long) -> Unit,
    onPledgeCreate: () -> Unit,
    listState: LazyListState
) {
    DashboardSection(
        sectionTitle = "My Pledges",
        onLinkClick = { /* TODO */ }
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pledges) { pledge ->
                PledgeCard(
                    modifier = Modifier
                        .width(280.dp),
                    pledge = pledge,
                    onCardClick = { onPledgeClick(pledge.id) }
                )
            }

            item {
                CreateEntityCard(
                    modifier = Modifier.width(280.dp),
                    icon = Icons.Filled.AttachMoney,
                    title = "Create A Pledge",
                    buttonText = "Earn Now",
                    onCreateClick = { onPledgeCreate() }
                )
            }
        }
    }
}
