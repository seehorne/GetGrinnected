package screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.OrgCard
import com.example.myapplication.R
import com.example.myapplication.User

/**
 * A composable function that represents the Settings screen of our app.
 *
 * @param modifier Modifier to be applied to the screen layout.
 */
@Composable
fun SettingsScreen(modifier: Modifier = Modifier, orgs: List<User>) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { scrollState.animateScrollTo(0) }

    val isFollowed = orgs.filter { it.is_followed }

    Box(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Profile", fontSize = 28.sp)

            Spacer(modifier = modifier.weight(1f))

            IconButton(
                onClick = { /* TODO handle switch account */ },
                modifier = modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Switch Account",
                )
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 80.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blank_profile_picture),
                    contentDescription = "Profile Image",
                    modifier = modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                IconButton(
                    onClick = { /* TODO handle image change */ },
                    modifier = modifier
                        .offset(x = (-8).dp, y = (-8).dp)
                        .background(Color.White, CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Image",
                        tint = Color.Black,
                        modifier = modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Username123", fontSize = 16.sp)
                IconButton(onClick = { /* TODO handle username edit */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Username",
                        modifier = modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            Text("Organizations you follow: ", fontSize = 20.sp)

            Spacer(modifier = modifier.height(4.dp))

            if (isFollowed.isEmpty()) {
                Text("You haven't favorited any events yet.", modifier = Modifier.padding(16.dp))
            } else {
                isFollowed.forEach { account ->
                    OrgCard(
                        account = account, modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun SettingsScreenPreview(){
    val sampleOrgs = listOf(
        User(1, "test", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),
        User(1, "test2", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test3", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test4", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test5", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        )
    SettingsScreen(orgs = sampleOrgs)
}
