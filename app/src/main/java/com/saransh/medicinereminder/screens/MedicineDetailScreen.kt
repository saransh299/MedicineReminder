package com.saransh.medicinereminder.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saransh.medicinereminder.models.DailySchedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    schedule: DailySchedule,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(schedule.taskName) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Medicine: ${schedule.taskName}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Time: ${schedule.startTime}", style = MaterialTheme.typography.bodyMedium)
            schedule.notes?.let {
                Text(text = "Notes: $it", style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "Status: ${schedule.status}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete Schedule")
            }
        }
    }
}
