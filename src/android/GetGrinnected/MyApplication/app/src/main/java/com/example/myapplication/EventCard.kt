package com.example.myapplication

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext

/**
 * A composable function that creates the general look of an event card.
 * It is expandable and collapsable via a clicking interaction.
 *
 * @param event this is an Event object to fill the contents of the card.
 * @param modifier Modifier to be applied to the card layout.
 */

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EventCard(event: Event, modifier: Modifier = Modifier) {
    // Boolean to track whether a card is expanded
    val expanded = remember { mutableStateOf(false) }
    // Boolean to track whether a card is favorited
    val isFavorited = remember(event.is_favorited) { mutableStateOf(event.is_favorited) }
    // Boolean to track if card should cause notification
    val isNotification = remember(event.is_notification) { mutableStateOf(event.is_notification) }
    val context = LocalContext.current
    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
    val typography = MaterialTheme.typography
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val notificationHandler = NotificationHandler(context)

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }
    // Sets up composable to be a card for our info
    Card(
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
        //elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .defaultMinSize(minHeight = 120.dp)
            .padding(horizontal = 8.dp)
            .background(color = colorScheme.primaryContainer)
            .border(2.dp, color = colorScheme.primary)
            .clickable
        {
                expanded.value = !expanded.value
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        // Sets up a column on our card
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize()
        ) {
            // Makes a row within the column
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Makes a column within the row to display the name of the event
                Column(modifier = Modifier.weight(1f)) {
                    event.event_name?.let {
                        Text(
                            text = it,
                            style = typography.titleLarge,
                            color = colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${event.event_date} at ${event.event_time}",
                        style = typography.bodyMedium,
                        color = colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(2.dp))
                    event.event_location?.let { Text(text = it, 
                                                     style = typography.bodyMedium,
                                                      color = colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    // If organizations is empty we won't include the output on the card
                    if (event.organizations?.isNotEmpty() == true) {
                        Text(text = "Hosted by: ${event.organizations.joinToString()}",
                            style = typography.bodyMedium,
                            color = colorScheme.onSurface)
                    }
                }
                
                // this is the Column for icons
                Column {
                    // This is our favorite icon
                    Icon(
                        imageVector = if (isFavorited.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                isFavorited.value = !isFavorited.value
                                // This tells our database to update the events favorited status
                                CoroutineScope(Dispatchers.IO).launch {
                                    AppRepository.toggleFavorite(event.eventid, isFavorited.value)
                                }
                            },
                    )
                    // This is our notification icon
                    Icon(
                        imageVector = if (isNotification.value) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Notification Icon",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                isNotification.value = !isNotification.value
                                // This tells our database to update the events favorited status
                                notificationHandler.showSimpleNotification(event)
                                CoroutineScope(Dispatchers.IO).launch {
                                    AppRepository.toggleNotification(event.eventid, isNotification.value)
                                }
                            },
                    )
                }

            }
            // This is our expanded view if the value is expanded we show the following info
            if (expanded.value) {
                if (event.event_description?.isNotEmpty() == true) {
                    Text(text = "Description: ${event.event_description}",
                        style = typography.bodyMedium,
                        color = colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (event.tags.isNotEmpty()) {
                    Text(text = "Tags: ${event.tags.joinToString()}",
                        style = typography.bodyMedium,
                        color = colorScheme.onSurface)
                }
            }
        }
    }
}


