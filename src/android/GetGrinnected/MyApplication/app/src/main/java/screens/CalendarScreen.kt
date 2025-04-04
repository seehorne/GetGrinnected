package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that represents the Calendar screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 */
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    var selectedView by remember { mutableIntStateOf(2) }
    val expanded = remember { mutableStateOf(false) }

    Column (modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(16.dp)) {
            Row {
                Button (onClick = { expanded.value = true }) {
                    if (selectedView == 0){
                        Text("Day")
                    } else if (selectedView == 1){
                        Text("Week")
                    } else {
                        Text("Month")
                    }
                }
            }
            DropdownMenu (expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(text = { Text("Day View") }, onClick = {
                    selectedView = 0
                    expanded.value = false
                })
                DropdownMenuItem(text = { Text("Week View") }, onClick = {
                    selectedView = 1
                    expanded.value = false
                })
                DropdownMenuItem(text = { Text("Month View") }, onClick = {
                    selectedView = 2
                    expanded.value = false
                })
            }
        }
        when (selectedView) {
            0 -> DayViewScreen(Modifier)
            1 -> WeekViewScreen(Modifier)
            2 -> MonthViewScreen(Modifier)
        }
    }
}