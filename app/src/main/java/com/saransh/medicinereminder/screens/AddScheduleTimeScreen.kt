package com.saransh.medicinereminder.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddScheduleTimeScreen(
    medicineName: String,
    doctorName: String?,
    frequencyCount: Int,
    frequencyType: String,
    onSave: (medicineName: String, times: List<String>) -> Unit,
    onCancel: () -> Unit
) {
    var doseTimes by remember { mutableStateOf(List(frequencyCount) { "" }) }
    val context = LocalContext.current

    fun ordinal(n: Int): String = when (n % 10) {
        1 -> if (n % 100 != 11) "st" else "th"
        2 -> if (n % 100 != 12) "nd" else "th"
        3 -> if (n % 100 != 13) "rd" else "th"
        else -> "th"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = buildString {
                if (!doctorName.isNullOrBlank()) append("Dr. $doctorName has recommended you to take ")
                append("$medicineName $frequencyCount time${if (frequencyCount > 1) "s" else ""} $frequencyType")
            },
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        doseTimes.forEachIndexed { index, time ->
            TimePickerField(
                label = "${index + 1}${ordinal(index + 1)} Dose",
                time = time,
                onTimeSelected = { selectedTime ->
                    doseTimes = doseTimes.toMutableList().also { it[index] = selectedTime }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { onSave(medicineName, doseTimes) }) {
                Text("Save")
            }
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun TimePickerField(
    label: String,
    time: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var displayTime by remember { mutableStateOf(time) }

    OutlinedTextField(
        value = displayTime,
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = {
                val calendar = Calendar.getInstance()
                val timeParts = if (displayTime.isNotEmpty()) {
                    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    sdf.parse(displayTime)?.let { cal ->
                        listOf(cal.hours, cal.minutes)
                    } ?: listOf(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
                } else {
                    listOf(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
                }

                val hour = timeParts[0]
                val minute = timeParts[1]

                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        val cal = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, selectedHour)
                            set(Calendar.MINUTE, selectedMinute)
                        }
                        val formatted = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
                        displayTime = formatted
                        onTimeSelected(formatted)
                    },
                    hour,
                    minute,
                    false // 12-hour format
                ).show()
            }) {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Pick Time")
            }
        }
    )
}
