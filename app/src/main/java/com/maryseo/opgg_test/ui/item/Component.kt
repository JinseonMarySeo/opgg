package com.maryseo.opgg_test.ui.item

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.ui.theme.DarkGrey
import com.maryseo.opgg_test.ui.theme.DarkishPink

@Composable
fun IconCircle(modifier: Modifier, imgUrl: String?, size: Dp) {
    Image(
        painter = if (imgUrl != null) rememberImagePainter(data = Uri.parse(imgUrl)) else painterResource(
            R.drawable.ic_launcher_background
        ),
        contentDescription = null,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentScale = ContentScale.FillWidth
    )
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