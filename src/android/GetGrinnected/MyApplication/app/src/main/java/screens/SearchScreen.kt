package screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.GetGrinnected.myapplication.AppRepository
import com.GetGrinnected.myapplication.Event
import com.GetGrinnected.myapplication.toEvent
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchScreen() {
    // holds dates for the current view dropdown
    val today = LocalDate.now()
    val days = 6
    val daysList = mutableListOf<LocalDate>()
    for (day in 0..days) {
        daysList.add(today.plusDays(day.toLong()))
    }
    // Gets events from our repo
    val eventEntities by AppRepository.events
    // Converts them to event data type
    val events = eventEntities.map { it.toEvent() }
    // String associated with storing and changing the username of an account when we edit
    var description by remember { mutableStateOf("") }
    val search = remember {
        mutableStateOf(false)
    }
    var selectedDate by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) { // creates day menu
            /*
            Text(
                "Advanced Search",
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                // This ensures we alert a screen reader when we have scrolled down to this section
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = "Advanced Search for events"
                })
             */
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // creates day menu
            androidx.compose.material3.OutlinedTextField(
                value = description,
                onValueChange = {
                    search.value = false
                    description = it
                },
                singleLine = true,
                label = {
                    androidx.compose.material3.Text(
                        "Search",
                        color = colorScheme.onBackground,
                        style = typography.labelLarge
                    )
                }
            )
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // creates day menu
            selectedDate = datePickerDocked()
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // creates day menu
            Button(
                onClick = { search.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Search")
                }
            }
        }
        if (search.value) {
            var sortedEvents = mutableListOf<Event>()
            if(description != ""){
                for (e in events.indices){
                    if (selectedDate != ""){
                        if (events[e].event_name.uppercase().contains(description.uppercase())
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        } else if (events[e].event_description.uppercase().contains(description.uppercase())
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        } else if ((events[e].organizations?.toString()?.uppercase()
                                ?.contains(description.uppercase()) ?: true) == true
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        }else if (events[e].event_location?.uppercase()
                                ?.contains(description.uppercase()) ?: true
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        }
                    }
                    else {
                        if (events[e].event_name.uppercase().contains(description.uppercase())) {
                            sortedEvents.add(events[e])
                        } else if (events[e].event_description.uppercase()
                                .contains(description.uppercase())
                        ) {
                            sortedEvents.add(events[e])
                        } else if (events[e].organizations?.toString()?.uppercase()
                                ?.contains(description.uppercase()) != false
                        ) {
                            sortedEvents.add(events[e])
                        }else if (events[e].event_location?.uppercase()
                                ?.contains(description.uppercase()) != false
                        ) {
                            sortedEvents.add(events[e])
                        }
                    }
                }
            } else{
                if (selectedDate != ""){
                    for (e in events.indices) {
                        if (events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))){
                            sortedEvents.add(events[e])
                        }
                    }
                } else {
                    sortedEvents = events.toMutableList()
                }
            }
            SearchResults(sortedEvents.distinct())
        }
    }
}






/**
 * Creates a date picking composable as designed Android developer.com
 * It creates a calendar dropdown that makes selecting any day easy.
 * Currently doesn't work as it is off by a day and its return value is not the correct date format
 * @return a String date that was selected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerDocked(): String{
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
    return selectedDate
}

/**
 * takes a millis and returns a date
 */
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
/*
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val searchQuery = viewModel.searchQuery
    val eventEntities by AppRepository.events
    // Converts them to event data type
    val events = eventEntities.map { it.toEvent() }
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    SearchScreen(
        searchQuery = searchQuery,
        searchResults = events,
        onSearchQueryChange = {viewModel.onSearchQueryChange(it) }
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<Event>,
    onSearchQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {},
            placeholder = {
                Text(text = "Search Events")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            content = {
                LazyColumn(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = searchResults.size,
                    key = { index -> searchResults[index].eventid },
                    itemContent = { index ->
                        val event = searchResults[index]
                        EventCard(event = event)
                    }
                )
            }},
            active = true,
            onActiveChange = {},
            tonalElevation = 0.dp
        )
}}



@Composable
fun EventListItem(
    event: Event,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = event.event_name)
        event.event_date?.let { Text(text = it) }
        event.event_time?.let { Text(text = it) }
    }
}
*/

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: MutableList<Event>,
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
    var expanded = false

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
                        headlineContent = { Text(resultText.toString()) },
                        supportingContent = supportingContent?.let { { it(resultText.toString()) } },
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText.toString())
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
*/