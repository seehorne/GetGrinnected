package screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Event
import com.example.myapplication.EventCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A composable function that represents the Calendar screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(event: List<Event>, modifier: Modifier = Modifier) {
    var selectedView by remember { mutableIntStateOf(2) }
    val expanded = remember { mutableStateOf(false) }
    val calendarInputList by remember {
        mutableStateOf(createCalendarList(event))
    }
    var clickedCalendarElem by remember {
        mutableStateOf<CalendarInput?>(null)
    }
    val scrollState = rememberScrollState()
    val currentMonth = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM")
    // background color for the page
    val gradient =
        Brush.verticalGradient(
            listOf(Color.Red, Color.Blue, Color.Green),
            0.0f,
            10000.0f,
            TileMode.Repeated
        )
    Column(modifier = modifier.fillMaxSize().background(gradient)) {
        Box(modifier = modifier.padding(16.dp)) {
            Row {
                Button(onClick = { expanded.value = true }) {
                    if (selectedView == 0) {
                        Text("Day")
                    } else if (selectedView == 1) {
                        Text("Week")
                    } else {
                        Text("Month")
                    }
                }
            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
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
            0 -> DayViewScreen(modifier)
            1 -> WeekViewScreen(modifier)
            2 -> {
                MonthViewScreen(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .aspectRatio(1.3f),
                    calendarInput = calendarInputList,
                    onDayClick = { day ->
                        clickedCalendarElem = calendarInputList.first { it.day == day }
                    },
                    month = (
                            if(currentMonth.format(formatter).toString() == "01"){
                                "January"
                            }
                            else if (currentMonth.format(formatter).toString() == "02"){
                                "February"
                            }
                            else if (currentMonth.format(formatter).toString() == "03"){
                                "March"
                            }
                            else if (currentMonth.format(formatter).toString() == "04"){
                                "April"
                            }
                            else if (currentMonth.format(formatter).toString() == "05"){
                                "May"
                            }
                            else if (currentMonth.format(formatter).toString() == "06"){
                                "June"
                            }
                            else if (currentMonth.format(formatter).toString() == "07"){
                                "July"
                            }
                            else if (currentMonth.format(formatter).toString() == "08"){
                                "August"
                            }
                            else if (currentMonth.format(formatter).toString() == "09"){
                                "September"
                            }
                            else if (currentMonth.format(formatter).toString() == "10"){
                                "October"
                            }
                            else if (currentMonth.format(formatter).toString() == "11"){
                                "November"
                            }
                            else{
                                "December"
                            }).toString()
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    clickedCalendarElem?.let { dayInfo ->
                        Text(
                            text = "April (Day ${dayInfo.day})",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        dayInfo.events.forEach {
                            EventCard(it)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Creates the hard coded calendar input list for use in the calendar
 * @return a list of CalendarInput values
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun createCalendarList(event: List<Event>): List<CalendarInput>{
    val calendarInputs = mutableListOf<CalendarInput>()
    val currentDay = mutableListOf<Event>()
    val currentMonth = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
    for(i in 1 .. 31){
        for(j in event.indices){
            if (event[j].event_start_time.substring(8, 10) == i.toString() && event[j].event_start_time.substring(5, 7) == currentMonth.format(formatter).toString())
            {
                currentDay.add(event[j])
            }
        }
        calendarInputs.add(
            CalendarInput(
                i,
                events = currentDay.toMutableList()
            )
            )
        currentDay.clear()
    }
    return calendarInputs
}
