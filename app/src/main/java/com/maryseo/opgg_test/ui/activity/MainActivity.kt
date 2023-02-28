@file:OptIn(ExperimentalMaterialApi::class)

package com.maryseo.opgg_test.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.network.model.Summoner
import com.maryseo.opgg_test.network.other.Status
import com.maryseo.opgg_test.ui.item.IconProfileWithTxt
import com.maryseo.opgg_test.ui.item.LeagueItem
import com.maryseo.opgg_test.ui.theme.*
import com.maryseo.opgg_test.ui.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainVM: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OPGG_TESTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    mainVM.getSummoner(name = "genetory")
                    Content()
                }
            }
        }
    }

    private fun getSummoner() {
        mainVM.getSummoner(name = "genetory")
    }

    private fun getMatches() {
        mainVM.getMatches(name = "genetory")
    }

    @Composable
    private fun Content() {
        val summoner = mainVM.summoner.observeAsState().value

        LaunchedEffect(key1 = mainVM) {
            getSummoner()
            getMatches()
        }

        when (summoner?.status) {
            Status.SUCCESS -> {
                LazyColumn {
                    item {
                        ProfileArea(summoner = summoner.data!!)
                    }
                }
            }
            Status.LOADING -> {

            }
            else -> {

            }
        }
    }

    @Composable
    private fun ProfileArea(summoner: Summoner) {
        val defaultPadding = dimensionResource(id = R.dimen.default_horizontal_margin)
        val imageWidth = dimensionResource(id = R.dimen.profile_circle_size)
        val height = imageWidth + 14.dp

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = defaultPadding)
                .padding(top = 36.dp, bottom = 20.dp)
        ) {
            val (profile, text, button, leagues) = createRefs()

            IconProfileWithTxt(
                modifier = Modifier
                    .constrainAs(profile) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .background(Transparent)
                    .size(imageWidth, height),
                imgUrl = summoner.profileImageUrl, name = summoner.level.toString()
            )

            Text(
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(profile.end, margin = defaultPadding)
                },
                text = summoner.name,
                style = Typography.h1
            )

            Button(
                onClick = {
                    getSummoner()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoftBlue,
                    contentColor = White
                ),
                modifier = Modifier
                    .constrainAs(button) {
                        start.linkTo(profile.end, margin = defaultPadding)
                        top.linkTo(text.bottom, margin = 8.dp)
                    }
                    .wrapContentSize(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = stringResource(id = R.string.button_reload))
            }

            LazyRow(modifier = Modifier.constrainAs(leagues) {
                top.linkTo(profile.bottom, margin = 31.dp)
                bottom.linkTo(parent.bottom)
            }, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    items = summoner.leagues,
                    itemContent = { LeagueItem(it) }
                )
            }
        }
    }

    @Composable
    private fun MatchList() {

    }
}

@Composable
private fun MatchItem() {

}


@Composable
fun MyListView(items: List<String>) {
    // Create a state to hold the selected item
    val selectedItem = remember { mutableStateOf(-1) }

    // Use a LazyColumn to create the ListView
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items) { index, item ->
            // Create a ListItem for each item in the list
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { selectedItem.value = index },
                text = { Text(text = item) },
                secondaryText = { Text(text = "Item ${index + 1}") },
                icon = {
                    if (index == selectedItem.value) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            )
            Divider()
        }
    }
}

@Composable
private fun ToolbarTest(name: String) {

    Column {
        val defaultPadding = dimensionResource(id = R.dimen.default_horizontal_margin)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = defaultPadding)
                .padding(top = 36.dp, bottom = 20.dp)
        ) {
            val (profile, text, button, leagues) = createRefs()

            IconProfileWithTxt(
                modifier = Modifier
                    .constrainAs(profile) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .background(Transparent)
                    .size(dimensionResource(id = R.dimen.profile_circle_size) + 14.dp),
                imgUrl = "https://opgg-static.akamaized.net/images/profile_icons/profileIcon1625.jpg",
                name = "247")

            Text(
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(profile.end, margin = defaultPadding)
                },
                text = "TEXT",
                style = Typography.h1
            )

            Button(
                onClick = { /* Do something */ },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoftBlue,
                    contentColor = White
                ),
                modifier = Modifier
                    .constrainAs(button) {
                        start.linkTo(profile.end, margin = defaultPadding)
                        top.linkTo(text.bottom, margin = 8.dp)
                    }
                    .wrapContentSize(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = stringResource(id = R.string.button_reload))
            }


        }

//        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            items(2) {
//                LeagueItem()
//            }
//        }
        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4")
        MyListView(items = items)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OPGG_TESTTheme {
        ToolbarTest(name = "genetory")
    }
}