package com.saransh.medicinereminder.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saransh.medicinereminder.models.DailySchedule
import com.saransh.medicinereminder.utils.getNextDose
import com.saransh.medicinereminder.viewmodel.TodayScheduleViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun TodayScheduleScreen(
    todaySchedules: List<DailySchedule>,
    todayScheduleViewModel: TodayScheduleViewModel,
    navController: NavHostController,
    onAddClick: () -> Unit
) {
    val groupedSchedules = todaySchedules.groupBy { it.taskName }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ====== HEADER ======
            Text(
                text = "Your medicines for today",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), thickness = 1.dp)

            // ====== EMPTY STATE ======
            if (groupedSchedules.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No medicines scheduled for today",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                // ====== LIST OF CARDS ======
                groupedSchedules.forEach { (taskName, doses) ->
                    val nextDose = getNextDose(doses)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("medicine_detail/${doses.first().id}")
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    taskName,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                IconButton(
                                    onClick = {
                                        doses.forEach { dose ->
                                            todayScheduleViewModel.deleteSchedule(dose)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            nextDose?.let { dose ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = dose.startTime,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    )

                                    Text(
                                        text = dose.status.takeIf { it.isNotBlank() } ?: "Pending",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
