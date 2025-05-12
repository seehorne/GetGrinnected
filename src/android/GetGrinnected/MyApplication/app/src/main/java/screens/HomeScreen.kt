package screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.GetGrinnected.myapplication.AppRepository
import com.GetGrinnected.myapplication.Check
import com.GetGrinnected.myapplication.CheckBox
import com.GetGrinnected.myapplication.DataStoreSettings
import com.GetGrinnected.myapplication.EventCard
import com.GetGrinnected.myapplication.R
import com.GetGrinnected.myapplication.SnackBarController
import com.GetGrinnected.myapplication.toEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.layout.Column as Column1

/**
 * Anthony Schwindt, Ethan Hughes
 *
 * A composable function that represents the Home screen of our app. (More to come)
 *
 */

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(tags: List<Check>) {
    // remembers what page the app is on
    var selectedView by remember { mutableIntStateOf(0) }
    // holds whether the dropdown menu's are up or down
    val expanded = remember { mutableStateOf(false) }
    val expanded2 = remember { mutableStateOf(false) }
    // holds dates for the current view dropdown
    val today = LocalDate.now()
    // number of days you can see ahead in homepage
    val days = 6
    val daysList  = mutableListOf<LocalDate>()
    for (day in 0..days) {
        daysList.add(today.plusDays(day.toLong()))
    }
    // formats the date view
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatter3 = DateTimeFormatter.ofPattern("dd")
    // remembers where we are scrolled to
    val state = rememberScrollState()
    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
    val typography = MaterialTheme.typography
    // Gets events from our repo
    val eventEntities by AppRepository.events
    // Converts them to event data type
    val events = eventEntities.map { it.toEvent() }
    // makes the page scrollable
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    // used to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // State to determine whether we are currently refreshing
    var isRefreshing by remember { mutableStateOf(false) }
    // The current context of our app
    val context = LocalContext.current

    // State for refreshing the API on Pull, handles the action associated with pulling
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, // Boolean value associated with whether it is refreshing
        onRefresh = {
            // Launches task
            coroutineScope.launch {
                // Sets is refreshing to true
                isRefreshing = true
                // Syncs local db with api
                AppRepository.syncFromApi()
                // Gets the current time
                val now = System.currentTimeMillis()
                // Sets out last sync time to the current time
                DataStoreSettings.setLastSyncTime(context, now)
                // Sets is Refreshing to false
                isRefreshing = false
            }
        }
    )


    // Host for our snack bar
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackBarController.events.collectLatest { event ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        // creates the UI field for the events
        Column1(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
        ) {
            // creates the top bar for the home page (I think might be erroneous with the row below
            Box(
                modifier = Modifier
                    .background(color = colorScheme.primary)
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 8.dp),
            )
            {
                //creates a row to align the logo and buttons on the home page
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    // adds the logo to the top
                    Image(
                        painter = painterResource(id = R.drawable.getgrinnected_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .padding(start = 6.dp, top = 25.dp, end = 6.dp)
                            .background(Color.White)
                            .size(50.dp)
                    )

                    // centers the buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                            .padding(top = 25.dp)
                    ) {
                        // creates day menu
                        Button(
                            onClick = { expanded.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorScheme.secondary,
                                contentColor = colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Select Day Filter",
                                    modifier = Modifier.size(20.dp)
                                        .semantics { role = Role.Button }
                                )
                                Spacer(Modifier.size(6.dp))
                                // displays selected day on button
                                Text(
                                    daysList[selectedView].dayOfWeek.getDisplayName(
                                        java.time.format.TextStyle.FULL,
                                        Locale.getDefault()).substring(0, 3) + ", " + toMonth(daysList[selectedView]) + " " + daysList[selectedView].format(formatter3), style = typography.labelLarge
                                )
                            }
                        }
                        Spacer(Modifier.size(6.dp))
                        // creates dropdown menu when button is clicked
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }) {
                            for (day in daysList.indices) {
                                DropdownMenuItem(text = {
                                    Text(
                                        daysList[day].dayOfWeek.getDisplayName(
                                            java.time.format.TextStyle.FULL,
                                            Locale.getDefault()
                                        ).substring(
                                            0,
                                            3
                                        ) + ", " + toMonth(daysList[day]) + " " + daysList[day].format(
                                            formatter3
                                        ), style = typography.labelLarge
                                    )

                                }, onClick = {
                                    selectedView = day
                                    expanded.value = false
                                })
                            }
                        }
                        // creates tags menu
                        Button(
                            onClick = { expanded2.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorScheme.secondary,
                                contentColor = colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    contentDescription = "Select Tags Filters",
                                    modifier = Modifier.size(20.dp)
                                        .semantics { role = Role.Button }
                                )
                                Spacer(Modifier.size(6.dp))

                                Text("Tags", style = typography.labelLarge, maxLines = 1)
                            }
                        }
                        // the list of tags
                        DropdownMenu(
                            expanded = expanded2.value,
                            onDismissRequest = { expanded2.value = false }) {
                            // creates the clear option to remove all selected tags
                            DropdownMenuItem(
                                text = { Text("Clear") },
                                onClick = {
                                    // goes through and unselects all tags
                                    for (t in tags.indices) {
                                        tags[t].checked.value = false
                                    }
                                },
                            )
                            // creates a list item with checkbox for each tag found in the events
                            for (tag in tags.indices) {
                                DropdownMenuItem(
                                    text = { CheckBox(check = tags[tag]) },
                                    onClick = {
                                        tags[tag].checked.value = !tags[tag].checked.value
                                    })
                            }
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            }
            // Box is used to setup our pull to refresh
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                // Sets up the UI for the screen for card layout
                Column1(
                    modifier = Modifier
                        // sets the color of the background
                        .background(color = colorScheme.background)
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                        .verticalScroll(state),
                    // centers the event cards
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    // creates a visual spacer for the top of the page
                    Spacer(modifier = Modifier.height(50.dp))

                    val chosenTags = mutableListOf<String>()
                    for (i in tags.indices) {
                        if (tags[i].checked.value) {
                            chosenTags.add(tags[i].label)
                        }
                    }
                    // populates the page with events
                    for (cardnum in events.indices) {
                        if (chosenTags.isEmpty()) {
                            for (day in daysList.indices) {
                                if (selectedView == day) {
                                    if (events[cardnum].event_start_time.substring(
                                            0,
                                            10
                                        ) == daysList[day].format(formatter).toString()
                                    ) {
                                        EventCard(event = events[cardnum])
                                        // creates space between cards
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                        // sorts by tag
                        else {
                            for (day in daysList.indices) {
                                for (t in chosenTags.indices) {
                                    if (selectedView == day) {
                                        if (events[cardnum].event_start_time.substring(
                                                0,
                                                10
                                            ) == daysList[day].format(formatter).toString()
                                            && events[cardnum].tags.contains(chosenTags[t])
                                        ) {
                                            EventCard(event = events[cardnum])
                                            // creates space between cards
                                            Spacer(modifier = Modifier.height(16.dp))
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // creates a space at the bottom for visual appeal
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No more events match filters",
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        // This ensures we alert a screen reader when we have scrolled down to this section
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                            contentDescription = "No more events match filters."
                        }

                    )
                    Spacer(modifier = Modifier.height(120.dp))

                }
                // Handles the pull to refresh indicator
                PullRefreshIndicator(
                    // Tracks the refreshing state
                    refreshing = isRefreshing,
                    // References the pulling state action we setup earlier
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}


/**
 * takes a date and returns the first three letters of the associated month
 * @param localDate a LocalDate
 * @return a String that is the first three letters of associated month
 */
@RequiresApi(Build.VERSION_CODES.O)
fun toMonth(localDate: LocalDate): String{
    val formatter2 = DateTimeFormatter.ofPattern("MM")
    val month = (when (localDate.format(formatter2)) {
        "01" -> { "Jan" }
        "02" -> { "Feb" }
        "03" -> { "Mar" }
        "04" -> { "Apr" }
        "05" -> { "May" }
        "06" -> { "Jun" }
        "07" -> { "Jul" }
        "08" -> { "Aug" }
        "09" -> { "Sep" }
        "10" -> { "Oct" }
        "11" -> { "Nov" }
        else -> {"Dec"}
    }).toString()
    return month
}