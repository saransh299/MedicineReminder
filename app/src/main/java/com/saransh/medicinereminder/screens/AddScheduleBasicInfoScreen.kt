package com.saransh.medicinereminder.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleBasicInfoScreen(
    onNext: (
        medicineName: String,
        doctorName: String?,
        frequencyCount: Int,
        frequencyType: String
    ) -> Unit
) {
    var medicineName by remember { mutableStateOf(TextFieldValue("")) }
    var doctorName by remember { mutableStateOf(TextFieldValue("")) }

    val frequencyOptions = (1..10).toList()
    var selectedFrequency by remember { mutableStateOf(frequencyOptions.first()) }
    var frequencyDropdownExpanded by remember { mutableStateOf(false) }

    val frequencyTypeOptions = listOf("Daily", "Weekly", "Monthly", "Custom")
    var selectedFrequencyType by remember { mutableStateOf(frequencyTypeOptions.first()) }
    var frequencyTypeDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Medicine Details", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = medicineName,
            onValueChange = { medicineName = it },
            label = { Text("Medicine Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = doctorName,
            onValueChange = { doctorName = it },
            label = { Text("Doctor (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Text("Frequency", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        // Frequency Count Dropdown
        ExposedDropdownMenuBox(
            expanded = frequencyDropdownExpanded,
            onExpandedChange = { frequencyDropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedFrequency.toString(),
                onValueChange = {},
                readOnly = true,
                label = { Text("Times per day") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = frequencyDropdownExpanded,
                onDismissRequest = { frequencyDropdownExpanded = false }
            ) {
                frequencyOptions.forEach { freq ->
                    DropdownMenuItem(
                        text = { Text(freq.toString()) },
                        onClick = {
                            selectedFrequency = freq
                            frequencyDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Frequency Type Dropdown
        ExposedDropdownMenuBox(
            expanded = frequencyTypeDropdownExpanded,
            onExpandedChange = { frequencyTypeDropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedFrequencyType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Frequency Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyTypeDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = frequencyTypeDropdownExpanded,
                onDismissRequest = { frequencyTypeDropdownExpanded = false }
            ) {
                frequencyTypeOptions.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedFrequencyType = type
                            frequencyTypeDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = medicineName.text.isNotBlank(),
            onClick = {
                onNext(
                    medicineName.text,
                    doctorName.text.takeIf { it.isNotBlank() },
                    selectedFrequency,
                    selectedFrequencyType
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}
