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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * A composable function that creates a checkbox and associated text
 *
 * @param check this is an Event object to fill the contents of the card.
 */

@Composable
fun CheckBox(check: Check) {

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    )
    {
        //creates a checkbox
        Checkbox(
            checked = check.checked.value,
            onCheckedChange = { check.checked.value = it },
            modifier = Modifier
                .semantics { contentDescription = "Tag: ${check.label}, ${if (check.checked.value) "selected" else "not selected"}" }
        )
        // checkbox label
        Text (check.label)
    }

}