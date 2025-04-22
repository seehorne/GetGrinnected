package screens

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Event
import kotlinx.coroutines.launch

private const val CALENDAR_ROWS = 5
private const val CALENDAR_COLUMNS = 7

/**
 * This is a composable function that represents our Month View in the
 * Calendar.
 *
 * @param modifier Modifier to be applied to the screen layout.
 * @param calendarInput a list of CalendarInputs to put into the calendar with values
 * @param onDayClick is a lambda function that handles what we should do when a day is clicked
 * @param strokeWidth is a float used for our stokeWidth of our brush for making the calendar lines
 * @param month is the current month as a string
 *
 * This was majorly adapted from tutorial: https://www.youtube.com/watch?v=QS-iOdm0pMs
 */

@Composable
fun MonthViewScreen(
    modifier: Modifier = Modifier,
    calendarInput: List<CalendarInput>,
    onDayClick:(Int)->Unit,
    strokeWidth:Float = 15f,
    month:String
){

    var canvasSize by remember {
        mutableStateOf(Size.Zero)
    }
    var clickAnimationOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    var animationRadius by remember {
        mutableStateOf(0f)
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = month,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(key1 = true) {
                    detectTapGestures(
                        onTap = {offset ->
                            val column = (offset.x / canvasSize.width * CALENDAR_COLUMNS).toInt() + 1
                            val row = (offset.y / canvasSize.height * CALENDAR_ROWS).toInt() + 1
                            val day = column + (row - 1) * CALENDAR_COLUMNS
                            if (day <= calendarInput.size){
                                onDayClick(day)
                                clickAnimationOffset = offset
                                scope.launch {
                                    animate(0f, 225f, animationSpec = tween(300)) {value, _ ->
                                        animationRadius = value
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            val canvasHeight = size.height
            val canvasWidth = size.width
            canvasSize = Size(canvasWidth, canvasHeight)
            val ySteps = canvasHeight/ CALENDAR_ROWS
            val xSteps = canvasWidth/ CALENDAR_COLUMNS
            val column = (clickAnimationOffset.x / canvasSize.width * CALENDAR_COLUMNS).toInt() + 1
            val row = (clickAnimationOffset.y / canvasSize.height * CALENDAR_ROWS).toInt() + 1

            val path = Path().apply {
                moveTo((column - 1) * xSteps, (row - 1) * ySteps)
                lineTo(column * xSteps, (row - 1) * ySteps)
                lineTo(column * xSteps, row * ySteps)
                lineTo((column - 1) * xSteps, row * ySteps)
                close()
            }

            clipPath(path){
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color.White.copy(0.8f), Color.White.copy(0.2f)),
                        center = clickAnimationOffset,
                        radius = animationRadius + 0.1f
                    ),
                    radius = animationRadius + 0.1f,
                    center = clickAnimationOffset
                )

            }

            drawRoundRect(
                Color.White,
                cornerRadius = CornerRadius(25f, 25f),
                style = Stroke(
                    width = strokeWidth
                )
            )

            for(i in 1 until CALENDAR_ROWS){
                drawLine(
                    color = Color.White,
                    start = Offset(0f, ySteps * i),
                    end =  Offset(canvasWidth, ySteps * i),
                    strokeWidth = strokeWidth
                    )
            }
            for(i in 1 until CALENDAR_COLUMNS){
                drawLine(
                    color = Color.White,
                    start = Offset(xSteps * i, 0f),
                    end =  Offset(xSteps * i, canvasHeight),
                    strokeWidth = strokeWidth
                )
            }
            val textHeight = 17.dp.toPx()
            for(i in calendarInput.indices){
                val textPositionX = xSteps * (i% CALENDAR_COLUMNS) + strokeWidth
                val textPositionY = (i / CALENDAR_COLUMNS) * ySteps + textHeight + strokeWidth/2

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${i + 1}",
                        textPositionX,
                        textPositionY,
                        android.graphics.Paint().apply{
                            textSize = textHeight
                            color = Color.White.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

data class CalendarInput(
    val day: Int,
    val events: List<Event>
)
