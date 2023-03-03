@file:OptIn(ExperimentalMaterialApi::class)

package com.maryseo.opgg_test.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Summoner
import com.maryseo.opgg_test.network.other.State
import com.maryseo.opgg_test.ui.item.*
import com.maryseo.opgg_test.ui.theme.OPGG_TESTTheme
import com.maryseo.opgg_test.ui.theme.Transparent
import com.maryseo.opgg_test.ui.theme.Typography
import com.maryseo.opgg_test.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainVM: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OPGG_TESTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }

    private fun getSummonerAndMatchResults() {
        getSummoner()
        getMatches()
    }

    private fun getSummoner() {
        mainVM.getSummoner(name = "genetory", getString(R.string.message_api_error))
    }

    private fun getMatches(lastMatch: Long? = null) {
        mainVM.getMatches(name = "genetory", lastMatch, getString(R.string.message_api_error))
    }

    @Composable
    private fun Content() {
        LaunchedEffect(key1 = mainVM) {
            getSummonerAndMatchResults()
        }

        val summoner = mainVM.summoner.collectAsState()
        val matchResult = mainVM.matches.collectAsState()
        val latestMatches = mainVM.latestMatch.observeAsState().value
        val games = mainVM.gameList.observeAsState().value

        val scrollState = rememberLazyListState()
        // Load more games when reaching the end of the scroll
        val isAtBottom = scrollState.isAtBottom()
        LaunchedEffect(isAtBottom) {
            val gameList = matchResult.value.data?.games
            if (isAtBottom && gameList != null) {
                val lastMatch = gameList[gameList.size - 1].createDate
                getMatches(lastMatch)
            }
        }

        when (summoner.value.state) {
            State.SUCCESS -> {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    summoner.value.data?.let {
                        item {
                            ProfileArea(summoner = it)
                        }
                    }

                    latestMatches?.let {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            MatchHeader(it)
                        }
                    }
                    items(
                        items = games as List<Game>,
                        itemContent = { MatchItem(it) }
                    )
                }
            }
            State.LOADING -> {
                ItemLoading()
            }
            else -> {
                Toast.makeText(this, summoner.value.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    @Composable
    private fun ProfileArea(summoner: Summoner) {
        val defaultPadding = dimensionResource(id = R.dimen.default_horizontal_margin)
        val imageWidth = dimensionResource(id = R.dimen.profile_circle_size)
        val height = imageWidth + 10.dp

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

            RoundedButtonSoftBlue(onClick = { getSummonerAndMatchResults() }, modifier =Modifier
                .constrainAs(button) {
                    start.linkTo(profile.end, margin = defaultPadding)
                    top.linkTo(text.bottom, margin = 8.dp)
                }, name = stringResource(id = R.string.button_reload))

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
}

@Composable
fun ItemLoading(modifier: Modifier = Modifier, ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LazyListState.isAtBottom(): Boolean {

    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                        lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}