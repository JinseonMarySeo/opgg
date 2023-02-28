package com.maryseo.opgg_test.ui.item

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.network.data.dto.Game
import com.maryseo.opgg_test.network.data.dto.Item
import com.maryseo.opgg_test.ui.activity.getValidUrl
import com.maryseo.opgg_test.ui.theme.*


@Composable
fun BarView(width: Dp, height: Dp, color: Color) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(color)
            .padding(vertical = 6.dp)
    )
}

@Composable
fun RoundedButtonSoftBlue(
    onClick: () -> Unit,
    modifier: Modifier,
    name: String
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SoftBlue,
            contentColor = White
        ),
        modifier = modifier
            .wrapContentWidth()
            .height(36.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text = name, color = White)
    }
}

@Composable
fun IconCircle(modifier: Modifier, imgUrl: String?, size: Dp) {
    Box(modifier = modifier
        .size(size)
        .clip(CircleShape)
    ) {
        imgUrl?.let {
            Image(
                painter = rememberImagePainter(data = Uri.parse(getValidUrl(imgUrl))),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        } ?: run {
            Box(modifier = Modifier.fillMaxSize().background(PaleGrey2))
        }
    }
}

@Composable
fun IconRadius(modifier: Modifier, imgUrl: String?, size: Dp, radius: Dp) {
    Box(modifier = modifier
        .size(size)
        .clip(RoundedCornerShape(radius))
    ) {
        imgUrl?.let {
            Image(
                painter = rememberImagePainter(data = Uri.parse(getValidUrl(imgUrl))),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        } ?: run {
            Box(modifier = Modifier.fillMaxSize().background(PaleGrey2))
        }
    }
}

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
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Stats(modifier: Modifier, style: TextStyle, data: Array<Int>) {
    val strStats = stringResource(id = R.string.format_stats).format(data[0], data[1], data[2])
    Text(
        modifier = modifier,
        text = AnnotatedTextForStats(text = strStats),
        style = style
    )
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