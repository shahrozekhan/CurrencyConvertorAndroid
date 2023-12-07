//package com.shahroze.shared.common.views
//
//import androidx.annotation.DrawableRes
//import androidx.annotation.StringRes
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.height
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ButtonElevation
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.Dp
//import com.shahroze.currencyconvertorandroid.R
//
//@Composable
//fun ComposeButton(
//    modifier: Modifier = Modifier
//        .background(colorResource(R.color.purple_200)),
//    @StringRes text: Int,
//    colors: ButtonColors = ButtonDefaults.buttonColors(
//        contentColor = colorResource(R.color.white)
//    ),
//    shape: Shape = MaterialTheme.shapes.small,
//    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
//    enabled: Boolean = true,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
//    border: BorderStroke? = null,
//    onClick: () -> Unit = {}
//) {
//    Button(
//        modifier = modifier,
//        onClick = onClick,
//        colors = colors,
//        shape = shape,
//        contentPadding = contentPadding,
//        enabled = enabled,
//        interactionSource = interactionSource,
//        elevation = elevation,
//        border = border,
//    ) {
//        Body2Medium(
//            stringResource(id = text),
//            color = R.color.white
//        )
//    }
//}
//
//@Composable
//fun VerticalDivider(
//    modifier: Modifier = Modifier,
//    dp: Dp,
//    color: Color = Color.Transparent
//) {
//    Divider(
//        modifier
//            .height(dp),
//        color = color
//    )
//}
//
//@Composable
//fun ComposeIcon(
//    modifier: Modifier = Modifier,
//    @DrawableRes icon: Int,
//    contentDescription: String? = null,
//    tint: Color = Color.Unspecified
//) {
//    Icon(
//        modifier = modifier,
//        painter = painterResource(icon),
//        contentDescription = contentDescription,
//        tint = tint
//    )
//}
