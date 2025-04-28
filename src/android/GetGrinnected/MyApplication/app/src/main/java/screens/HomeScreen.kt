package screens

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.AppRepository
import com.example.myapplication.Check
import com.example.myapplication.CheckBox
import com.example.myapplication.EventCard
import com.example.myapplication.R
import com.example.myapplication.toEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.layout.Column as Column1
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.DataStoreSettings
import kotlinx.coroutines.launch
import androidx.compose.material3.ButtonDefaults
import com.example.myapplication.fixTime

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
    val tomorrow = today.plusDays(1)
    val twodays = today.plusDays(2)
    val threedays = today.plusDays(3)
    val fourdays = today.plusDays(4)
    val fivedays = today.plusDays(5)
    val sixdays = today.plusDays(6)
    // formats the date view
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // background color for the page
    val formatter2 = DateTimeFormatter.ofPattern("MM")
    val formatter3 = DateTimeFormatter.ofPattern("dd")
    val currentMonth = today.format(formatter2).toString()
    // sets the month based on devices local date
    val month = (when (currentMonth) {
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
        else -> {
            "Dec"
        }
    }).toString()
    val gradient =
        Brush.verticalGradient(
            listOf(Color.Red, Color.Blue, Color.Green),
            0.0f,
            10000.0f,
            TileMode.Repeated
        )
    // remembers where we are scrolled to
    val state = rememberScrollState()
    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
    val typography = MaterialTheme.typography
    // Gets events from our repo
    val eventEntities by AppRepository.events
    // Converts them to event data type
    val event = eventEntities.map { it.toEvent() }
    // Sorts them by time
    val events = fixTime(eventEntities.map { it.toEvent() }).sortedBy { it.event_start_time }
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

    // creates the UI field for the events
    Column1(
        modifier = Modifier.fillMaxSize()
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
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            // adds the logo to the top
            Image(
                painter = painterResource(id = R.drawable.getgrinnected_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(25.dp)
                    .background(Color.White)
                    .size(50.dp)
            )
            // centers the buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // creates day menu
                Button(onClick = { expanded.value = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.secondary,
                        contentColor = colorScheme.onPrimary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)) {
                    // displays selected day on the button
                    when (selectedView) {

                        0 -> { Text(today.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + today.format(formatter3), style = typography.labelLarge) }
                        1 -> { Text(tomorrow.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + tomorrow.format(formatter3), style = typography.labelLarge) }
                        2 -> { Text(twodays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + twodays.format(formatter3), style = typography.labelLarge) }
                        3 -> { Text(threedays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + threedays.format(formatter3), style = typography.labelLarge) }
                        4 -> { Text(fourdays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + fourdays.format(formatter3), style = typography.labelLarge) }
                        5 -> { Text(fivedays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month+ " " + fivedays.format(formatter3), style = typography.labelLarge) }
                        else -> { Text(sixdays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month+ " " + sixdays.format(formatter3), style = typography.labelLarge) }

                    }
                }
                // creates dropdown menu when button is clicked
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    DropdownMenuItem(text = { Text(today.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + today.format(formatter3), style = typography.labelLarge) }, onClick = {
                        selectedView = 0
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(tomorrow.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + tomorrow.format(formatter3), style = typography.labelLarge) }, onClick = {
                        selectedView = 1
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(twodays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + twodays.format(formatter3), style = typography.labelLarge) }, onClick = {
                        selectedView = 2
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(threedays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + threedays.format(formatter3), style = typography.labelLarge) }, onClick = {
                        selectedView = 3
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(fourdays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + fourdays.format(formatter3), style = typography.labelLarge) }, onClick = {
                        selectedView = 4
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(fivedays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + fivedays.format(formatter3), style = typography.labelLarge) }, onClick = {
                        selectedView = 5
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(sixdays.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).substring(0,3) + ", " + month + " " + sixdays.format(formatter3), style = typography.labelLarge)}, onClick = {
                        selectedView = 6
                        expanded.value = false
                    })
                }}
                // I don't know why this is necessary but was needed to properly space tag menu
                Row(
                    modifier = Modifier
                        .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    // creates tags menu
                    Button(onClick = { expanded2.value = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondary,
                            contentColor = colorScheme.onPrimary),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)){
                        Text("Tags", style = typography.labelLarge, maxLines = 1)
                    }
                    DropdownMenu(
                        expanded = expanded2.value,
                        onDismissRequest = { expanded2.value = false }) {
                        DropdownMenuItem(
                            text = {Text("Unselect All")},
                            onClick = {for (t in tags.indices){
                                tags[t].checked.value = false}
                            },
                        )
                        for (tag in tags.indices){
                            DropdownMenuItem(text = {CheckBox(check = tags[tag])}, onClick = {tags[tag].checked.value = !tags[tag].checked.value})
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
                        if (selectedView == 0) {
                            if (events[cardnum].event_start_time.substring(0, 10) == today.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else if (selectedView == 1) {
                            if (events[cardnum].event_start_time.substring(0, 10) == tomorrow.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else if (selectedView == 2) {
                            if (events[cardnum].event_start_time.substring(0, 10) == twodays.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else if (selectedView == 3) {
                            if (events[cardnum].event_start_time.substring(0, 10) == threedays.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else if (selectedView == 4) {
                            if (events[cardnum].event_start_time.substring(0, 10) == fourdays.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else if (selectedView == 5) {
                            if (events[cardnum].event_start_time.substring(0, 10) == fivedays.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else if (selectedView == 6) {
                            if (events[cardnum].event_start_time.substring(0, 10) == sixdays.format(
                                    formatter
                                ).toString()
                            ) {
                                EventCard(event = events[cardnum])
                                // creates space between cards
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                        }
                    }
                    // sorts by tag
                    else {
                        for (t in chosenTags.indices) {
                            if (selectedView == 0) {
                                if (event[cardnum].event_start_time.substring(0, 10) == today.format(
                                        formatter
                                    ).toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break
                                }
                            } else if (selectedView == 1) {
                                if (event[cardnum].event_start_time.substring(0, 10) == tomorrow.format(
                                        formatter
                                    ).toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break
                                }
                            } else if (selectedView == 2) {
                                if (event[cardnum].event_start_time.substring(0, 10) == twodays.format(
                                        formatter
                                    ).toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break
                                }
                            } else if (selectedView == 3) {
                                if (event[cardnum].event_start_time.substring(
                                        0,
                                        10
                                    ) == threedays.format(formatter)
                                        .toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break
                                }
                            } else if (selectedView == 4) {
                                if (event[cardnum].event_start_time.substring(0, 10) == fourdays.format(
                                        formatter
                                    ).toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break

                                }
                            } else if (selectedView == 5) {
                                if (event[cardnum].event_start_time.substring(0, 10) == fivedays.format(
                                        formatter
                                    ).toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break
                                }
                            } else if (selectedView == 6) {
                                if (event[cardnum].event_start_time.substring(0, 10) == sixdays.format(
                                        formatter
                                    ).toString() && event[cardnum].tags.contains(chosenTags[t])
                                ) {
                                    EventCard(event = event[cardnum])
                                    // creates space between cards
                                    Spacer(modifier = Modifier.height(16.dp))
                                    break
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
                    fontWeight = FontWeight.Bold
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



/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CustomizableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    // Customization options
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") },
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    // Track expanded state of search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                // Customizable input field implementation
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Show search results in a lazy column for better performance
            LazyColumn {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = supportingContent?.let { { it(resultText) } },
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
// [END android_compose_components_customizable_searchbar]

@Preview(showBackground = true)
@Composable
fun CustomizableSearchBarExample() {
    // Manage query state
    var query by rememberSaveable { mutableStateOf("") }
    val items = listOf(
        "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
        "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow",
        "Nougat", "Oreo", "Pie"
    )

    // Filter items based on query
    val filteredItems by remember {
        derivedStateOf {
            if (query.isEmpty()) {
                items
            } else {
                items.filter { it.contains(query, ignoreCase = true) }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomizableSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* Handle search submission */ },
            searchResults = filteredItems,
            onResultClick = { query = it },
            // Customize appearance with optional parameters
            placeholder = { Text("Search desserts") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "More options") },
            supportingContent = { Text("Android dessert") },
            leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Starred item") }
        )

        // Display the filtered list below the search bar
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 72.dp, // Provides space for the search bar
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.semantics {
                traversalIndex = 1f
            },
        ) {
            items(count = filteredItems.size) {
                Text(text = filteredItems[it])
            }
        }
    }
}
*/
