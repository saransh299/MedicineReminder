package com.saransh.medicinereminder.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircularPicker(
    modifier: Modifier = Modifier,
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selectedValue) + values.size * 1000
    )

    val items = remember {
        List(values.size * 2000) { index ->
            values[index % values.size]
        }
    }

    var snappedItemIndex by remember { mutableStateOf(-1) }
    var snapJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = modifier
            .height(120.dp)
            .width(60.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            userScrollEnabled = true
        ) {
            itemsIndexed(items) { _, item ->
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString().padStart(2, '0'),
                        style = if (item == selectedValue) {
                            MaterialTheme.typography.titleLarge
                        } else {
                            MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
    }

    // Update selectedValue based on center item
    LaunchedEffect(listState.layoutInfo) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        if (visibleItems.isNotEmpty()) {
            val viewportCenter = listState.layoutInfo.viewportSize.height / 2
            val centerItem = visibleItems.minByOrNull {
                abs((it.offset + it.size / 2) - viewportCenter)
            }
            centerItem?.let {
                val index = it.index % values.size
                val value = values[index]
                if (value != selectedValue) {
                    onValueChange(value)
                }
            }
        }
    }

    // Snap once after scroll settles, with debounce
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            snapJob?.cancel()
            snapJob = coroutineScope.launch {
                delay(100L)
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    val viewportCenter = listState.layoutInfo.viewportSize.height / 2
                    val centerItem = visibleItems.minByOrNull {
                        abs((it.offset + it.size / 2) - viewportCenter)
                    }
                    centerItem?.let {
                        if (snappedItemIndex != it.index) {
                            snappedItemIndex = it.index
                            listState.animateScrollToItem(it.index)
                        }
                    }
                }
            }
        } else {
            snapJob?.cancel()
        }
    }
}
