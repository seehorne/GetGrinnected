package screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.Column as Column1
import com.example.myapplication.assets.


/**
 * A composable function that represents the Home screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 */

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen (modifier: Modifier = Modifier) {
    var selectedView by remember { mutableIntStateOf(2) }
    val expanded = remember { mutableStateOf(false) }
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val twodays = today.plusDays(2)
    val threedays = today.plusDays(3)
    val fourdays = today.plusDays(4)
    val fivedays = today.plusDays(5)
    val sixdays = today.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var cardnum = 1
    val gradient =
        Brush.verticalGradient(
            listOf(Color.Red, Color.Blue, Color.Green),
            0.0f,
            10000.0f,
            TileMode.Repeated
        )
    val state = rememberScrollState()
    var expanded2 = remember { mutableStateOf(false) }
    var check1 = remember { mutableStateOf(false)}
    var check2 = remember { mutableStateOf(false)}
    var check3 = remember { mutableStateOf(false)}
    ReadJSONFromAssets(baseContext, test.json)
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    Column1(
        modifier = Modifier
            .background(gradient)
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .verticalScroll(state),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.height(150.dp))

        repeat(20) {
            Card(
                modifier = Modifier
                    .size(width = 380.dp, height = 100.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Black)

            ) {
                Text(
                    text = "Event " + cardnum,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            cardnum += 1
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
    Box(
        modifier = Modifier
            .background(Color.Black)
            .size(width = 450.dp, height = 100.dp)
            .padding(horizontal = 8.dp),

        )
    {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.gg_logo_2),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(25.dp)
                    .size(50.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { expanded.value = true }) {
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
                Row(modifier = Modifier
                    .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(onClick = { expanded2.value = true }) {
                        Text("Tags")
                    }
                    DropdownMenu(
                        expanded = expanded2.value,
                        onDismissRequest = { expanded2.value = false }) {
                        DropdownMenuItem(text = {
                        Row(modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,){
                            Checkbox(
                                checked = check1.value,
                                onCheckedChange = {
                                    if (check1.value == true) {
                                        check1.value = false }
                                    else { check1.value = true}
                                })

                            Text ("Student Activity")}},
                            onClick = {
                            selectedView = 0
                            expanded2.value = false
                        })
                        DropdownMenuItem(text = {Row(modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,){
                            Checkbox(
                                checked = check2.value,
                                onCheckedChange = {
                                    if (check2.value == true) {
                                        check2.value = false }
                                    else { check2.value = true}
                                })

                            Text("CLS") }},
                            onClick = {
                            selectedView = 1
                            expanded2.value = false
                            })

                        DropdownMenuItem(text = {
                            Row(modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,){
                                Checkbox(
                                    checked = check3.value,
                                    onCheckedChange = {
                                        if (check3.value == true) {
                                            check3.value = false }
                                        else { check3.value = true}
                                    })

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


    @Preview
    @Composable
    fun HomeScreenPreview(modifier: Modifier = Modifier) {
        val gradient =
            Brush.verticalGradient(
                listOf(Color.Red, Color.Blue, Color.Green),
                0.0f,
                10000.0f,
                TileMode.Repeated
            )
        val state = rememberScrollState()
        var cardnum = 1
        LaunchedEffect(Unit) { state.animateScrollTo(100) }
        Column1(
            modifier = Modifier
                .background(gradient)
                .size(800.dp)
                .padding(horizontal = 8.dp)
                .verticalScroll(state),
            verticalArrangement = Arrangement.Center,

            ) {
            repeat(20) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .size(width = 380.dp, height = 100.dp)
                        .background(Color.White)
                        .border(2.dp, Color.Black)

                ) {
                    Text(
                        text = "Event " + cardnum,
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                cardnum += 1
            }
        }
    }






