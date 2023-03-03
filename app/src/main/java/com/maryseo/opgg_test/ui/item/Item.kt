package com.maryseo.opgg_test.ui.item

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Item
import com.maryseo.opgg_test.network.data.dto.League
import com.maryseo.opgg_test.network.data.response.MatchesResponse
import com.maryseo.opgg_test.ui.theme.*
import com.maryseo.opgg_test.util.formatGameLength
import com.maryseo.opgg_test.util.formatTimestamp
import com.maryseo.opgg_test.util.getValidUrl
import com.maryseo.opgg_test.util.strIsWin
import java.text.NumberFormat


@Composable
fun IconProfileWithTxt(modifier: Modifier, imgUrl: String?, name: String?) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
    ) {
        IconCircle(
            modifier = Modifier.align(alignment = Alignment.TopCenter),
            imgUrl = imgUrl,
            size = dimensionResource(id = R.dimen.profile_circle_size)
        )

        if (!name.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGrey)
                    .defaultMinSize(minWidth = 33.dp, minHeight = 20.dp)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = name,
                    style = Typography.caption,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun IconChampionWithBadge(modifier: Modifier, imgUrl: String?, name: String?) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
    ) {
        IconCircle(
            modifier = Modifier.align(alignment = Alignment.TopCenter),
            imgUrl = imgUrl,
            size = dimensionResource(id = R.dimen.champion_circle_size)
        )

        if (!name.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, White), shape = RoundedCornerShape(8.dp))
                    .background(OrangeYellow)
                    .defaultMinSize(minWidth = 30.dp, minHeight = 16.dp)
                    .padding(horizontal = 4.dp, vertical = 3.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = name,
                    style = Typography.caption,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun IconChampionWithRate(modifier: Modifier, imgUrl: String?, rate: Int) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconCircle(
            modifier = Modifier,
            imgUrl = imgUrl,
            size = dimensionResource(id = R.dimen.champion_most_size)
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = stringResource(id = R.string.format_percent).format(rate),
            color = Color.DarkGray,
            style = Typography.body2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LeagueItem(league: League) {
    val defaultPadding = 8.dp
    val textPadding = 2.dp

    Box(
        modifier = Modifier
            .size(240.dp, 82.dp)
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, PaleGrey2),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 13.dp, start = 12.dp, end = 16.dp)
        ) {
            val (image, name, tier, lp, result, button) = createRefs()

            Image(
                painter = rememberImagePainter(data = Uri.parse(league.tierRank.imageUrl)),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(dimensionResource(id = R.dimen.challenger_img_size)),
                contentScale = ContentScale.FillWidth
            )

            Text(
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end, margin = defaultPadding)
                },
                text = league.tierRank.name,
                style = Typography.body2,
                color = SoftBlue
            )

            Text(
                modifier = Modifier.constrainAs(tier) {
                    top.linkTo(name.bottom, margin = textPadding)
                    start.linkTo(image.end, margin = defaultPadding)
                },
                text = league.tierRank.tier,
                style = Typography.subtitle1
            )

            Text(
                modifier = Modifier.constrainAs(lp) {
                    top.linkTo(tier.bottom, margin = textPadding)
                    start.linkTo(image.end, margin = defaultPadding)
                },
                text = NumberFormat.getNumberInstance()
                    .format(league.tierRank.lp)
                    .plus(" LP"),
                style = Typography.body2,
                color = Gunmetal
            )

            Text(
                modifier = Modifier.constrainAs(result) {
                    top.linkTo(lp.bottom, margin = textPadding)
                    start.linkTo(image.end, margin = defaultPadding)
                },
                text = stringResource(id = R.string.format_record_percent).format(
                    league.wins,
                    league.losses,
                    league.rate
                ),
                style = Typography.body2,
                color = CoolGrey
            )

            ButtonCircleArrow(modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .size(dimensionResource(id = R.dimen.right_arrow_back_size)))
        }
    }
}

@Composable
fun MatchHeader(match: MatchesResponse) {
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

        val data = arrayOf(match.summary.kills, match.summary.assists, match.summary.deaths)
        Stats(modifier = Modifier.constrainAs(stats) {
            bottom.linkTo(kda.top, margin = 3.dp)
            start.linkTo(parent.start)
        }, style = Typography.subtitle1, data = data)

        Text(
            modifier = Modifier.constrainAs(result) {
                top.linkTo(titleRecent.bottom, margin = 10.dp)
                bottom.linkTo(stats.top, margin = 2.dp)
                start.linkTo(parent.start)
            }, text = stringResource(id = R.string.format_record_base).format(match.summary.wins, match.summary.losses),
            style = Typography.body2,
            color = CoolGrey
        )

        val (titlePosition, iconPosition, ratePosition) = createRefs()
        val (titleMost, championArea) = createRefs()

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

        MostChampionsUI(modifier = Modifier
            .constrainAs(championArea) {
                top.linkTo(titleMost.bottom, margin = 8.dp)
                start.linkTo(titleMost.start)
                end.linkTo(titleMost.end)
            }, match)

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
            style = Typography.body2,
            color = DarkGrey,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MatchItem(game: Game) {
    val defaultPadding = dimensionResource(id = R.dimen.default_horizontal_margin)
    val stats = game.stats

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
                .background(if (game.isWin) DarkishPink else SoftBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = strIsWin(isWin = game.isWin), style = Typography.h2, color = White)
            BarView(width = 16.dp, height = 1.dp, color = White_40)
            Text(text = formatGameLength(gameLength = game.gameLength), style = Typography.body1, color = White)
        }

        Column {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding)
            ) {
                val (champion, grid, type, timestamp, stat, item, multiKill) = createRefs()
                val championImgUrl = game.champion.imageUrl
                val imageWidth = dimensionResource(id = R.dimen.champion_circle_size)
                val height = imageWidth + 5.dp
                IconChampionWithBadge(
                    modifier = Modifier
                        .constrainAs(champion) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .size(imageWidth, height),
                    imgUrl = championImgUrl,
                    name = game.stats.general.opScoreBadge
                )

                GridSpellAndPeak(modifier = Modifier.constrainAs(grid) {
                    top.linkTo(parent.top)
                    start.linkTo(champion.end, margin = 4.dp)
                }, game)

                Column(modifier = Modifier
                    .constrainAs(stat) {
                        top.linkTo(parent.top)
                        start.linkTo(grid.end, margin = defaultPadding)
                    }
                    .padding(vertical = 2.dp)) {
                    val data = arrayOf(stats.general.kill, stats.general.assist, stats.general.death)
                    Stats(
                        modifier = Modifier.padding(bottom = 2.dp),
                        style = Typography.h2,
                        data = data
                    )
                    Text(
                        text = stringResource(id = R.string.format_kill_rate).format(stats.general.contributionForKillRate),
                        style = Typography.body1
                    )
                }

                RowItems(modifier = Modifier
                    .constrainAs(item) {
                        top.linkTo(grid.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }, items = game.items
                )

                Text(modifier = Modifier.constrainAs(type) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }, text = game.gameType, style = Typography.body1, color = CoolGrey)

                Text(modifier = Modifier.constrainAs(timestamp) {
                    top.linkTo(type.bottom)
                    end.linkTo(parent.end)
                }, text = formatTimestamp(timestamp = game.createDate), style = Typography.body1, color = CoolGrey)

                if (!stats.general.largestMultiKillString.isNullOrEmpty()) {
                    BorderWithTxt(modifier = Modifier.constrainAs(multiKill) {
                        bottom.linkTo(parent.bottom, margin = 2.dp)
                        end.linkTo(parent.end)
                    }, text = stats.general.largestMultiKillString, DarkishPink, 12.dp)
                }
            }
        }
    }
}

@Composable
fun MostChampionsUI(modifier: Modifier, match: MatchesResponse) {
    val image1 = getValidUrl(match.getMostChampions()[0].imageUrl)
    val image2 = if (match.getMostChampions().size < 2) null else getValidUrl(match.getMostChampions()[1].imageUrl)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconChampionWithRate(
            modifier = Modifier,
            imgUrl = image1,
            rate = match.getMostChampions()[0].getRate()
        )
        image2?.let {
            Spacer(modifier = Modifier.width(16.dp))
            IconChampionWithRate(
                modifier = Modifier,
                imgUrl = image2,
                rate = match.getMostChampions()[1].getRate()
            )
        }
    }
}

@Composable
fun GridSpellAndPeak(modifier: Modifier, game: Game) {
    val space = 2.dp
    val size = 19.dp

    Row(modifier,
        horizontalArrangement = Arrangement.spacedBy(space)) {
        Column(verticalArrangement = Arrangement.spacedBy(space)) {
            IconRadius(modifier = Modifier, imgUrl = game.spells[0].imageUrl, size = size, radius = 4.dp)
            IconRadius(modifier = Modifier, imgUrl = game.spells[1].imageUrl, size = size, radius = 4.dp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(space)) {
            IconCircle(modifier = Modifier, imgUrl = game.peak[0], size = size)
            IconCircle(modifier = Modifier, imgUrl = game.peak[1], size = size)
        }
    }
}

@Composable
fun RowItems(modifier: Modifier, items: List<Item>) {
    val space = 2.dp
    val size = 24.dp

    Row(modifier,
        horizontalArrangement = Arrangement.spacedBy(space)) {
        val iterator = items.iterator()
        var index = 0
        while (index < 6) {
            val imgUrl = if (iterator.hasNext()) {
                iterator.next().imageUrl
            } else {
                null
            }
            IconRadius(modifier = Modifier, imgUrl = imgUrl, size = size, radius = 4.dp)
            index++
        }
        IconCircle(modifier = Modifier, imgUrl = null, size = size)
    }
}