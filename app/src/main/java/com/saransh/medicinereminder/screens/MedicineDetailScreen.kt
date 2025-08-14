package com.saransh.medicinereminder.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.saransh.medicinereminder.models.DailySchedule
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    schedule: DailySchedule,
    allSchedulesForMedicine: List<DailySchedule>,
    onDelete: () -> Unit // kept for future use, not displayed
) {
    var status by remember { mutableStateOf("") }
    var takenTime by remember { mutableStateOf("") }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top section with muted shades
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(text = "Medicine: ${schedule.taskName}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Time: ${schedule.startTime}", style = MaterialTheme.typography.bodyMedium)
                schedule.notes?.let {
                    Text(text = "Notes: $it", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButtonBlank(
                    modifier = Modifier.weight(1f),
                    label = "Taken",
                    color = Color(0xFF4CAF50),
                    selected = status == "Taken"
                ) {
                    status = "Taken"
                    takenTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                }

                ActionButtonBlank(
                    modifier = Modifier.weight(1f),
                    label = "Missed",
                    color = Color(0xFFEF5350),
                    selected = status == "Missed"
                ) {
                    status = "Missed"
                    takenTime = ""
                }

                ActionButtonBlank(
                    modifier = Modifier.weight(1f),
                    label = "Snooze",
                    color = Color(0xFFFFC107),
                    selected = status == "Snooze"
                ) {
                    status = "Snooze"
                    takenTime = ""
                }
            }

            // Status below buttons
            if (status.isNotEmpty()) {
                val displayText = if (status == "Taken" && takenTime.isNotEmpty()) {
                    "$status at $takenTime"
                } else {
                    status
                }
                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

            // Upcoming doses today
            if (allSchedulesForMedicine.size > 1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Upcoming doses today", style = MaterialTheme.typography.titleMedium)
                    val upcomingDoses = allSchedulesForMedicine
                        .filter { it.id != schedule.id }
                    upcomingDoses.forEach { dose ->
                        Text("${dose.startTime}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtonBlank(
    modifier: Modifier = Modifier,
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) color.copy(alpha = 0.25f) else Color.Transparent
    val borderColor = if (selected) color else Color.Gray

    Box(
        modifier = modifier
            .height(50.dp)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = onClick) {
            Text(
                text = label,
                color = if (selected) Color.Black else Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
