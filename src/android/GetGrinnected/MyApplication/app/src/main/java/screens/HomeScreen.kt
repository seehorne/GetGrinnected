package screens

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.annotation.RequiresApi as RequiresApi1
import androidx.compose.foundation.layout.Column as Column1



/**
 * Anthony Schwindt, Ethan Hughes
 *
 * A composable function that represents the Home screen of our app. (More to come)
 */

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi1(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(event: Event) {
    // remembers what page the app is on
    var selectedView by remember { mutableIntStateOf(2) }
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
    // currently for numbering events will get rid of with real events
    var cardnum = 1
    // background color for the page
    val gradient =
        Brush.verticalGradient(
            listOf(Color.Red, Color.Blue, Color.Green),
            0.0f,
            10000.0f,
            TileMode.Repeated
        )
    // remembers where we are scrolled to
    val state = rememberScrollState()
    // stores whether checkboxes for tags are checked
    val check1 = remember { mutableStateOf(false)}
    val check2 = remember { mutableStateOf(false)}
    val check3 = remember { mutableStateOf(false)}
    // path to API data


    
    
    
    // makes the page scrollable
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    // creates the UI field for the events
    Column1(
        modifier = Modifier
            // sets the color of the background
            .background(gradient)
            .fillMaxSize()
            .verticalScroll(state),
        // centers the event cards
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // creates a visual spacer for the top of the page
        Spacer(modifier = Modifier.height(150.dp))
        // populates the page with model cards
        repeat(3) {
            Card(
                modifier = Modifier
                    .size(width = 380.dp, height = 100.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Black)
            ) {
                Text(
                    text =  event.toString(),
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
            // creates space between cards
            Spacer(modifier = Modifier.height(8.dp))
            // currently helps number model events - remove later
            cardnum += 1
        }
        // creates a space at the bottom for visual appeal
        Spacer(modifier = Modifier.height(120.dp))
    }
    // creates the top bar for the home page (I think might be erroneous with the row below
    Box(
        modifier = Modifier
            .background(Color.Black)
            .size(width = 450.dp, height = 100.dp)
            .padding(horizontal = 8.dp),
        )
    {
        //creates a row to align the logo and buttons on the home page
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            // adds the logo to the top
            Image(
                painter = painterResource(id = R.drawable.gg_logo_2),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(25.dp)
                    .size(50.dp)
            )
            // centers the bottons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // creates day menu
                Button(onClick = { expanded.value = true }) {
                    // displays selected day on the button
                    if (selectedView == 0) {
                        Text(today.format(formatter))
                    } else if (selectedView == 1) {
                        Text(tomorrow.format(formatter))
                    } else if (selectedView == 2) {
                        Text(twodays.format(formatter))
                    } else if (selectedView == 3) {
                        Text(threedays.format(formatter))
                    } else if (selectedView == 4) {
                        Text(fourdays.format(formatter))
                    } else if (selectedView == 5) {
                        Text(fivedays.format(formatter))
                    } else {
                        Text(sixdays.format(formatter))
                    }
                }
                // creates dropdown menu when button is clicked
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    DropdownMenuItem(text = { Text(today.format(formatter)) }, onClick = {
                        selectedView = 0
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(tomorrow.format(formatter)) }, onClick = {
                        selectedView = 1
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(twodays.format(formatter)) }, onClick = {
                        selectedView = 2
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(threedays.format(formatter)) }, onClick = {
                        selectedView = 3
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(fourdays.format(formatter)) }, onClick = {
                        selectedView = 4
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(fivedays.format(formatter)) }, onClick = {
                        selectedView = 5
                        expanded.value = false
                    })
                    DropdownMenuItem(text = { Text(sixdays.format(formatter)) }, onClick = {
                        selectedView = 6
                        expanded.value = false
                    })
                }}
                // I don't know why this is necessary but wan needed to properly space tag menu
                Row(
                    modifier = Modifier
                        .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    // creates tags menu
                    Button(onClick = { expanded2.value = true }) {
                        Text("Tags")
                    }
                    DropdownMenu(
                        expanded = expanded2.value,
                        onDismissRequest = { expanded2.value = false }) {
                        DropdownMenuItem(text = {
                            // formats checkbox and text on same line
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                            )
                            {
                                //creates a checkbox
                                Checkbox(
                                    checked = check1.value,
                                    onCheckedChange = {
                                        if (check1.value) {
                                            check1.value = false }
                                        else { check1.value = true}
                                    })
                                // checkbox 1 label
                                Text ("Student Activity")}},
                            onClick = {
                                selectedView = 0
                                expanded2.value = false
                        })
                        DropdownMenuItem(text = {
                            // formats checkbox and text on same line
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                            )
                            {
                                //creates a checkbox
                                Checkbox(
                                    checked = check2.value,
                                    onCheckedChange = {
                                        if (check2.value) {
                                            check2.value = false }
                                        else { check2.value = true}
                                })
                                // checkbox 2 label
                                Text("CLS") }},
                            onClick = {
                                selectedView = 1
                                expanded2.value = false
                            })
                        DropdownMenuItem(text = {
                            // formats checkbox and text on same line
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                            )
                            {
                                //creates a checkbox
                                Checkbox(
                                    checked = check3.value,
                                    onCheckedChange = {
                                        if (check3.value) {
                                            check3.value = false }
                                        else { check3.value = true}
                                    })
                                // checkbox 3 label
                                Text("Misc") }},
                            onClick = {
                                selectedView = 2
                                expanded2.value = false
                        })
                    }
                }
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
