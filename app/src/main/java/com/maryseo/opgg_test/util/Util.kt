package com.maryseo.opgg_test.util

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maryseo.opgg_test.R
import com.maryseo.opgg_test.ui.item.AnnotatedTextForStats
import com.maryseo.opgg_test.ui.theme.Typography
import kotlin.math.roundToInt

@Composable
fun formatTimestamp(timestamp: Long): String {
    val MINUTE = 60
    val HOUR = MINUTE * 60
    val DAY = HOUR * 24

    val currentTime = System.currentTimeMillis() / 1000
    val difference = currentTime - timestamp

    return when {
        difference < HOUR -> stringResource(id = R.string.format_timestamp_minute).format((difference / MINUTE).toDouble().roundToInt())
        difference < DAY -> stringResource(id = R.string.format_timestamp_hour).format((difference / HOUR).toDouble().roundToInt())
        else -> stringResource(id = R.string.format_timestamp_day).format((difference / DAY).toDouble().roundToInt())
    }
}

@Composable
fun formatGameLength(gameLength: Int): String {
    val minutes = gameLength / 60
    val seconds = gameLength % 60
    return "$minutes:$seconds"
}

@Composable
fun strIsWin(isWin: Boolean): String = if (isWin) "승" else "패"