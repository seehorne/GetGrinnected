package screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column as Column1
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * A composable function that represents the Home screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen (modifier: Modifier = Modifier){
    var selectedView by remember { mutableIntStateOf(2) }
    val expanded = remember { mutableStateOf(false) }
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val twodays = today.plusDays(2)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val gradient =
        Brush.verticalGradient(
            listOf(Color.Red, Color.Blue, Color.Green),
            0.0f,
            10000.0f,
            TileMode.Repeated
        )
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    Column1(
        modifier = Modifier
            .background(gradient)
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .verticalScroll(state),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ){Spacer(modifier = Modifier.height(150.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 300.dp, height = 100.dp)

        ) {
            Text(
            text = "Event Location, " +
                    "Event Time, " +
                    "Event Description",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
        Spacer(modifier = Modifier.height(8.dp))
            Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        modifier = Modifier
            .size(width = 300.dp, height = 100.dp)

    ) {
            Text(
                text = "Event Location2, " +
                        "Event Time2, " +
                        "Event Description2",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location3, " +
                        "Event Time3, " +
                        "Event Description3",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location4, " +
                        "Event Time4, " +
                        "Event Description4",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location5, " +
                        "Event Time5, " +
                        "Event Description5",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location6, " +
                        "Event Time6, " +
                        "Event Description6",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location7, " +
                        "Event Time7, " +
                        "Event Description7",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location8, " +
                        "Event Time8, " +
                        "Event Description8",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 380.dp, height = 100.dp)

        ) {
            Text(
                text = "Event Location9, " +
                        "Event Time9, " +
                        "Event Description9",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
    Column1(
        modifier = Modifier
            .background(Color.Black)
            .size(width = 450.dp, height = 100.dp)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Top,)
    {
        Box(modifier = Modifier
            .padding(30.dp)) {
            Row {
                Button (onClick = { expanded.value = true }) {
                    if (selectedView == 0){
                        Text(today.format(formatter))
                    } else if (selectedView == 1){
                        Text(tomorrow.format(formatter))
                    } else {
                        Text(twodays.format(formatter))
                    }
                }
            }
            DropdownMenu (expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
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
            }
        }
       /* when (selectedView) {
            0 -> DayViewScreen(Modifier)
            1 -> WeekViewScreen(Modifier)
            2 -> MonthViewScreen(Modifier)
            */
    }
}


@Preview
@Composable
fun HomeScreenPreview (modifier: Modifier = Modifier){
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

        ){
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






