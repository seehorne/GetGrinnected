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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that creates the general look of an organization card.
 * It is expandable and collapsable via a clicking interaction.
 *
 * @param account this is an account object to fill the contents of the card.
 * @param modifier Modifier to be applied to the card layout.
 */

@Composable
fun OrgCard(account: User, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val isFollowed = remember { mutableStateOf(account.is_followed) }

    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 120.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                expanded.value = !expanded.value
            }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.Center

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.account_name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
                Icon(
                    imageVector = if (isFollowed.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            isFollowed.value = !isFollowed.value
                        },
                )
            }
            if (expanded.value) {
                if (account.account_description.isNotEmpty()) {
                    Text(text = "About:", fontWeight = FontWeight.SemiBold)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Description: ${account.account_description}")
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
    OrgCard(account = User(1, "test", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1))
}
