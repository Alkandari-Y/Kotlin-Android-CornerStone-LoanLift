package com.coded.loanlift.composables.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T1, T2> SortAndFilterRow(
    sortLabel: String,
    sortOptions: List<T1>,
    selectedSort: T1,
    onSortSelected: (T1) -> Unit,
    filterLabel: String,
    filterOptions: List<T2>,
    selectedFilter: T2,
    onFilterSelected: (T2) -> Unit,
    sortLabelMapper: (T1) -> String = { it.toString() },
    filterLabelMapper: (T2) -> String = { it.toString() }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        DropdownMenuWithLabel(
            label = sortLabel,
            options = sortOptions,
            selected = selectedSort,
            onSelected = onSortSelected,
            labelMapper = sortLabelMapper
        )

        Spacer(modifier = Modifier.width(16.dp))

        DropdownMenuWithLabel(
            label = filterLabel,
            options = filterOptions,
            selected = selectedFilter,
            onSelected = onFilterSelected,
            labelMapper = filterLabelMapper
        )
    }
}