package com.example.myapplication

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates the general look of an organization card.
 * It is expandable and collapsable via a clicking interaction.
 *
 * @param account this is an account object to fill the contents of the card.
 * @param modifier Modifier to be applied to the card layout.
 */

@Composable
fun OrgCard(account: User, modifier: Modifier = Modifier) {
    // Boolean to track whether a card is expanded
    val expanded = remember { mutableStateOf(false) }
    // Boolean to track whether a card is favorited
    val isFollowed = remember { mutableStateOf(account.is_followed) }
    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
    val typography = MaterialTheme.typography

    // Sets up composable to be a card for our info
    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 120.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                expanded.value = !expanded.value // Toggles expanded state when card is clicked
            },
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        // Sets up a column on our card
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize(), // Animates the content size change when expanded
            verticalArrangement = Arrangement.Center
        ) {
            // Makes a row within the column
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Makes a column within the row to display the name of the org
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.account_name,
                        style = typography.titleLarge,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
                // This is our favorite icon that is align with the column of info but beside it
                // as it is in a row.
                Icon(
                    imageVector = if (isFollowed.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Follow Icon",
                    tint = colorScheme.tertiary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            isFollowed.value =
                                !isFollowed.value // Toggles follow status when clicked
                        },
                )
            }
            // This is our expanded view if the value is expanded we show the following info
            if (expanded.value) {
                if (account.account_description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "About:",
                        style = typography.titleMedium,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = account.account_description,
                        style = typography.bodyMedium,
                        color = colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Preview of event card for UI adjustment purposes only.
 */

@Preview (showBackground = true)
@Composable
fun OrgCardPreview(){
    OrgCard(account = User(1, "test", "test@test.com",  "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1))
}
