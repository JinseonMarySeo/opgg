package com.maryseo.opgg_test.ui.item

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.ui.theme.*
import com.maryseo.opgg_test.util.getValidUrl


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
            Box(modifier = Modifier
                .fillMaxSize()
                .background(PaleGrey2))
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
            Box(modifier = Modifier
                .fillMaxSize()
                .background(PaleGrey2))
        }
    }
}

@Composable
fun BorderWithTxt(modifier: Modifier, text: String, color: Color, radius: Dp) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .background(White)
            .border(BorderStroke(1.dp, color = color), shape = RoundedCornerShape(radius))
            .defaultMinSize(minWidth = 65.dp, minHeight = 20.dp)
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = Typography.body2,
            color = color,
            textAlign = TextAlign.Center
        )
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