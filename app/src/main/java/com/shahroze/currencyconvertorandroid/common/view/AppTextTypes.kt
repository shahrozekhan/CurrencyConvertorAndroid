package com.shahroze.currencyconvertorandroid.common.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.shahroze.currencyconvertorandroid.R

@Composable
fun HeadingMedium(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Int = R.color.black,
    isSingleLine: Boolean = false
) {
    return Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.titleMedium,
        color = colorResource(id = color),
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Body1Medium(
    text: String,
    modifier: Modifier = Modifier,
    color: Int = R.color.black,
    isSingleLine: Boolean = false
) {
    return Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = color),
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Body1Normal(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Int = R.color.black,
    isSingleLine: Boolean = false
) {
    return Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        color = colorResource(id = color),
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Body2Medium(
    text: String,
    modifier: Modifier = Modifier,
    color: Int = R.color.black,
    isSingleLine: Boolean = false
) {
    return Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = color),
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Body2Normal(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    color: Int = R.color.black,
    isSingleLine: Boolean = false
) {
    return Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Normal,
        color = colorResource(id = color),
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}