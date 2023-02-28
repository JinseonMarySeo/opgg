package com.maryseo.opgg_test.ui.item

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.network.data.dto.League
import com.maryseo.opgg_test.ui.theme.*
import java.text.NumberFormat


@Composable
fun IconProfileWithTxt(modifier: Modifier, imgUrl: String?, name: String?) {
    Log.e("IconProfileWithTxt()", "imgUrl = $imgUrl")
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
    ) {
        Image(
            painter = if (imgUrl != null) rememberImagePainter(data = Uri.parse(imgUrl)) else painterResource(
                R.drawable.ic_launcher_background
            ),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.profile_circle_size))
                .align(alignment = Alignment.Center)
                .clip(CircleShape),
            contentScale = ContentScale.FillWidth
        )

        if (!name.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGrey)
                    .defaultMinSize(minWidth = 33.dp)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = name,
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AnnotatedTextForStats(text: String): AnnotatedString {
    return buildAnnotatedString {
        val assists = text.trim().split("/")[1]
        val startIndex = text.indexOf(assists)
        val endIndex = startIndex + assists.length
        append(text)
        withStyle(style = SpanStyle(color = DarkishPink)) {
            addStyle(SpanStyle(color = DarkishPink), startIndex, endIndex)
        }
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
            val (image, name, tier, lp, result) = createRefs()

            Image(
                painter = if (league.tierRank.imageUrl != null)
                    rememberImagePainter(data = Uri.parse(league.tierRank.imageUrl))
                else painterResource(R.drawable.ic_launcher_background),
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
                style = Typography.caption,
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
                style = Typography.caption,
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
                style = Typography.caption,
                color = CoolGrey
            )
        }
    }
}