@file:OptIn(ExperimentalMaterialApi::class)

package com.maryseo.opgg_test.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Summoner
import com.maryseo.opgg_test.network.other.Status
import com.maryseo.opgg_test.ui.item.*
import com.maryseo.opgg_test.ui.theme.*
import com.maryseo.opgg_test.util.formatGameLength
import com.maryseo.opgg_test.util.formatTimestamp
import com.maryseo.opgg_test.util.strIsWin
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
        mainVM.getSummoner(name = "genetory")
    }

    private fun getMatches(lastMatch: Long? = null) {
        mainVM.getMatches(name = "genetory", lastMatch)
    }

    @Composable
    private fun Content() {
        val summoner = mainVM.summoner.observeAsState().value
        val latestMatches = mainVM.latestMatch.observeAsState().value
        val matchResult = mainVM.matches.observeAsState().value
        val games = mainVM.gameList.observeAsState().value

        LaunchedEffect(key1 = mainVM) {
            getSummoner()
            getMatches()
        }

        val scrollState = rememberLazyListState()
        // Load more games when reaching the end of the scroll
        val isAtBottom = scrollState.isAtBottom()
        LaunchedEffect(isAtBottom) {
            val gameList = matchResult?.data?.games
            if (isAtBottom && gameList != null) {
                val lastMatch = gameList[gameList.size - 1].createDate
                Log.e("Content()", "lastMatch = $lastMatch")
                getMatches(lastMatch)
            }
        }

        when (summoner?.status) {
            Status.SUCCESS -> {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        ProfileArea(summoner = summoner.data!!)
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
fun ItemLoading(
    modifier: Modifier = Modifier,
) {
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

@Composable
private fun PreviewTest(name: String) {

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
                    .size(dimensionResource(id = R.dimen.profile_circle_size) + 10.dp),
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
                    .wrapContentWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = stringResource(id = R.string.button_reload), color = White)
            }


        }

        Spacer(modifier = Modifier.height(8.dp))

        val defaultHorizontalPadding = dimensionResource(id = R.dimen.default_horizontal_margin)
        val defaultVerticalPadding = dimensionResource(id = R.dimen.default_vertical_margin)

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = defaultHorizontalPadding, vertical = defaultVerticalPadding)
        ) {
            val (titleRecent, result, stats, kda, rightLayout) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(titleRecent) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                text = stringResource(id = R.string.title_recent_matches),
                style = Typography.body2,
                color = CoolGrey
            )

            Text(
                modifier = Modifier.constrainAs(kda) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
                text = "3.65:1 (66%)",
                style = Typography.body2
            )

            val strStats = stringResource(id = R.string.format_stats).format(5, 6, 7) //"5.9 / 5.8 / 14.1"
            Text(
                modifier = Modifier.constrainAs(stats) {
                    bottom.linkTo(kda.top, margin = 3.dp)
                    start.linkTo(parent.start)
                }, text = AnnotatedTextForStats(text = strStats),
                style = Typography.subtitle1
            )

            Text(
                modifier = Modifier.constrainAs(result) {
                    top.linkTo(titleRecent.bottom, margin = 10.dp)
                    bottom.linkTo(stats.top, margin = 2.dp)
                    start.linkTo(parent.start)
                }, text = stringResource(id = R.string.format_record_base).format(11, 9),
                style = Typography.body2,
                color = CoolGrey
            )

            val (titlePosition, iconPosition, ratePosition) = createRefs()
            val (titleMost, iconChampion1, rateChampion1, iconChampion2, rateChampion2) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(titleMost) {
                        top.linkTo(parent.top)
                        end.linkTo(titlePosition.start)
                    }
                    .width(dimensionResource(id = R.dimen.header_most_rate_width)),
                text = stringResource(id = R.string.title_most_rate),
                style = Typography.body2,
                color = CoolGrey,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .constrainAs(titlePosition) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .width(dimensionResource(id = R.dimen.header_position_width)),
                text = stringResource(id = R.string.title_position),
                style = Typography.body2,
                color = CoolGrey,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(getPositionIcon(Position.valueOf("SUP"))),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(iconPosition) {
                        top.linkTo(titlePosition.bottom, margin = 10.dp)
                        start.linkTo(titlePosition.start)
                        end.linkTo(titlePosition.end)
                    },
                alignment = Alignment.Center
            )

            Text(
                modifier = Modifier
                    .constrainAs(ratePosition) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(titlePosition.start)
                        end.linkTo(titlePosition.end)
                    }
                    .width(dimensionResource(id = R.dimen.header_position_width)),
                text = stringResource(id = R.string.format_percent).format(61),
                style = Typography.body2,
                color = DarkGrey,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
        ) {
            Column(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.match_result_left_width))
                    .defaultMinSize(minHeight = 104.dp)
                    .background(if (false) DarkishPink else SoftBlue),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = strIsWin(isWin = false), style = Typography.h2, color = White)
                BarView(width = 16.dp, height = 1.dp, color = White_40)
                Text(text = formatGameLength(gameLength = 2720), style = Typography.body1, color = White)
            }

            Column {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(defaultPadding)
                ) {
                    val (champion, grid, type, timestamp, stat) = createRefs()
                    val championImgUrl = null //game.champion.imageUrl
                    IconChampionWithBadge(modifier = Modifier.constrainAs(champion) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }, imgUrl = championImgUrl, name = null)

//                    GridSpellAndPeak(modifier = Modifier.constrainAs(grid) {
//                        top.linkTo(parent.top)
//                        start.linkTo(champion.end, margin = 4.dp)
//                    })

                    Text(modifier = Modifier.constrainAs(type) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }, text = "솔랭", style = Typography.body1, color = CoolGrey)

                    Text(modifier = Modifier.constrainAs(timestamp) {
                        top.linkTo(type.bottom)
                        end.linkTo(parent.end)
                    }, text = formatTimestamp(timestamp = 1677548381), style = Typography.body1, color = CoolGrey)
                }
            }


        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OPGG_TESTTheme {
        PreviewTest(name = "genetory")
    }
}