package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CheckBox(check: check) {
    val checked = remember { mutableStateOf(check.checked) }

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    )
    {
        //creates a checkbox
        Checkbox(
            checked = checked.value,
            onCheckedChange = {
                checked.value = !checked.value
                check.checked = !check.checked
            })
        // checkbox 1 label
        Text (check.label)
    }

}