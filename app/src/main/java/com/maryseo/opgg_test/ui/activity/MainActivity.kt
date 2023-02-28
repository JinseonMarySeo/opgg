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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.network.data.dto.Summoner
import com.maryseo.opgg_test.network.other.Result
import com.maryseo.opgg_test.network.other.Status
import com.maryseo.opgg_test.ui.item.*
import com.maryseo.opgg_test.ui.theme.*
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
        val matchResult = mainVM.matches.observeAsState().value

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

                    item {
                        setMatchesResult(matchResult = matchResult)
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
    private fun setMatchesResult(matchResult: Result<MatchesResponse>?) {
        when (matchResult?.status) {
            Status.SUCCESS -> {

                        Spacer(modifier = Modifier.height(8.dp))
                        MatchHeader(matchResult.data!!)
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
private fun MatchHeader(match: MatchesResponse) {
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
            style = Typography.caption,
            color = CoolGrey
        )

        Text(
            modifier = Modifier.constrainAs(kda) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            },
            text = "3.65:1 (66%)",
            style = Typography.caption
        )

        val strStats = stringResource(id = R.string.format_stats).format(match.summary.kills, match.summary.assists, match.summary.deaths) //"5.9 / 5.8 / 14.1"
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
            }, text = stringResource(id = R.string.format_record_base).format(match.summary.wins, match.summary.losses),
            style = Typography.caption,
            color = CoolGrey
        )

        val (titlePosition, iconPosition, ratePosition) = createRefs()
        val (titleMost, iconChampion1, rateChampion1, iconChampion2, rateChampion2) = createRefs()
//        Text(
//            modifier = Modifier
//                .constrainAs(titleMost) {
//                    top.linkTo(parent.top)
//                    end.linkTo(titlePosition.start)
//                }
//                .width(dimensionResource(id = R.dimen.header_most_rate_width)),
//            text = stringResource(id = R.string.title_most_rate),
//            style = Typography.caption,
//            color = CoolGrey,
//            textAlign = TextAlign.Center
//        )

        Text(
            modifier = Modifier
                .constrainAs(titlePosition) {
                    top.linkTo(parent.top)
                    start.linkTo(titleMost.end)
                    end.linkTo(parent.end)
                }
                .width(dimensionResource(id = R.dimen.header_position_width)),
            text = stringResource(id = R.string.title_position),
            style = Typography.caption,
            color = CoolGrey,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(getPositionIcon(Position.valueOf(match.getMostPosition().position))),
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
            text = stringResource(id = R.string.format_percent).format(match.getMostPosition().getRate()),
            style = Typography.caption,
            color = DarkGrey,
            textAlign = TextAlign.Center
        )
    }
}



@Composable
private fun MatchItem() {

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

        Spacer(modifier = Modifier.height(8.dp))
//        MatchHeader()
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OPGG_TESTTheme {
        PreviewTest(name = "genetory")
    }
}