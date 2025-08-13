package com.saransh.medicinereminder.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun AddScheduleTimeScreen(
    medicineName: String,
    doctorName: String?,
    frequencyCount: Int,
    frequencyType: String,
    onSave: (List<String>, List<String>) -> Unit,
    onBack: () -> Unit
) {
    var times by remember { mutableStateOf(List(frequencyCount) { "" }) }
    var repeats by remember { mutableStateOf(List(frequencyCount) { "" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp)) // Added top space so heading isn't too close to status bar

        // Top heading
        Text(
            text = buildString {
                append(medicineName)
                if (!doctorName.isNullOrEmpty()) {
                    append(", recommended by Dr. $doctorName")
                }
                append(", to be taken $frequencyCount ${if (frequencyCount == 1) "time" else "times"} $frequencyType")
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(times) { index, time ->
                DoseTimePickerCard(
                    doseNumber = index + 1,
                    time = time,
                    onTimeSelected = { selectedTime ->
                        times = times.toMutableList().also { it[index] = selectedTime }
                    }
                )
            }
        }

        Button(
            onClick = { onSave(times, repeats) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Schedule")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun DoseTimePickerCard(
    doseNumber: Int,
    time: String,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                TimePickerDialog(
                    context,
                    { _, selectedHour: Int, selectedMinute: Int ->
                        val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                        onTimeSelected(formattedTime)
                    },
                    hour,
                    minute,
                    false
                ).show()
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${doseNumber}${ordinalSuffix(doseNumber)} Dose",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (time.isEmpty()) "Tap to select time" else time,
                color = if (time.isEmpty()) Color.Gray else Color.Black
            )
        }
    }
}

fun ordinalSuffix(number: Int): String {
    return if (number in 11..13) {
        "th"
    } else {
        when (number % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}
