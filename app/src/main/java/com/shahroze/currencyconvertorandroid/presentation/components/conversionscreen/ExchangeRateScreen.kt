package com.shahroze.currencyconvertorandroid.presentation.components.conversionscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shahroze.currencyconvertorandroid.R
import com.shahroze.currencyconvertorandroid.common.utils.containsDigitsAndDecimalOnly
import com.shahroze.currencyconvertorandroid.common.utils.empty
import com.shahroze.currencyconvertorandroid.common.view.Body1Normal
import com.shahroze.currencyconvertorandroid.common.view.Body2Medium
import com.shahroze.currencyconvertorandroid.common.view.Body2Normal
import com.shahroze.currencyconvertorandroid.common.view.ComposeButton
import com.shahroze.currencyconvertorandroid.common.view.ComposeIcon
import com.shahroze.currencyconvertorandroid.common.view.HeadingMedium
import com.shahroze.currencyconvertorandroid.common.view.VerticalDivider
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.presentation.theme.Purple80
import kotlinx.coroutines.launch
import java.math.BigDecimal

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun ExchangeRateScreen() {
    val viewModel: ExchangeRateViewModel = hiltViewModel()
    val currencyRateState = viewModel.exchangeRateViewState.value

    val snackBarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.commonEventFlow.collect {
            when (it) {
                is CommonUIEvent.ShowSnackbar -> {
                    localCoroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.message,
                            withDismissAction = true,
                            duration = it.duration
                        )
                    }
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Box(
            modifier = Modifier

                .fillMaxSize()
                .padding(
                    horizontal = 16.dp
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxSize(),
                state = rememberLazyListState()
            ) {
                item {
                    HeadingMedium(
                        modifier = Modifier
                            .padding(top = 16.dp),
                        text = stringResource(id = R.string.title_currency_convertor),
                        color = R.color.blue
                    )
                }
                item {
                    if (!currencyRateState.isCurrenciesLoading && !currencyRateState.listOfCurrency.isNullOrEmpty()) {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column {
                                Body1Normal(text = stringResource(id = R.string.from))
                                VerticalDivider(dp = 8.dp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val currencyName =
                                        if (currencyRateState.fromCurrency.currencyName.isNotEmpty()) "(${currencyRateState.fromCurrency.currencyName})" else String.empty
                                    CurrencyExposedDropdownMenuBox(
                                        modifier = Modifier.weight(0.85f),
                                        defaultText = "${currencyRateState.fromCurrency.currency} " + currencyName,
                                        currencyRateState = currencyRateState
                                    ) { selectedCurrency ->
                                        viewModel.onEvent(
                                            ExchangeRateScreenEvent.SelectFromCurrency(
                                                selectedCurrency.currency
                                            )
                                        )
                                    }
                                    ComposeIcon(
                                        icon = R.drawable.ic_sync,
                                        modifier = Modifier
                                            .weight(.15f)
                                            .size(30.dp)
                                            .clickable(enabled = !currencyRateState.isForceSyncingExchangeRate) {
                                                viewModel.onEvent(ExchangeRateScreenEvent.ForceSyncExchangeRate)
                                            }
                                    )
                                }
                            }

                            Column {
                                Body1Normal(text = stringResource(id = R.string.to))
                                VerticalDivider(dp = 8.dp)
                                val context = LocalContext.current
                                val textState by remember {
                                    mutableStateOf(
                                        if (currencyRateState.favoriteCurrencies.isNullOrEmpty()) {
                                            context.getString(R.string.select_currency)
                                        } else {
                                            context.getString(R.string.select_more_currency)
                                        }
                                    )
                                }
                                CurrencyExposedDropdownMenuBox(
                                    defaultText = textState,
                                    currencyRateState = currencyRateState
                                ) { selectedCurrency ->
                                    viewModel.onEvent(
                                        ExchangeRateScreenEvent.MarkToFavoriteCurrency(
                                            selectedCurrency
                                        )
                                    )
                                }
                                VerticalDivider(dp = 8.dp)
                                FlowRow(currencyRateState.favoriteCurrencies) { index, exchangeRate ->
                                    viewModel.onEvent(
                                        ExchangeRateScreenEvent.RemoveCurrencyFromSelected(
                                            index = index,
                                            value = exchangeRate
                                        )
                                    )
                                }
                                VerticalDivider(dp = 8.dp)
                            }
                        }
                    }
                }
                stickyHeader {
                    Column(
                        modifier = Modifier.background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VerticalDivider(dp = 8.dp)
                        TextField(
                            value = currencyRateState.amount,
                            onValueChange = {
                                if (it.containsDigitsAndDecimalOnly() || it.isEmpty()) {
                                    viewModel.onEvent(ExchangeRateScreenEvent.EnteredAmount(it))
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray),
                            label = {
                                Text(stringResource(id = R.string.amount))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        VerticalDivider(dp = 12.dp)

                        ComposeButton(
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            text = R.string.convert,
                            enabled = !currencyRateState.isCurrenciesLoading &&
                                    !currencyRateState.isFavoriteLoading &&
                                    !currencyRateState.isConverting &&
                                    !currencyRateState.isForceSyncingExchangeRate &&
                                    currencyRateState.amount.isNotEmpty(),
                            onClick = {
                                viewModel.onEvent(ExchangeRateScreenEvent.ConvertExchangeRate)
                            }
                        )
                        VerticalDivider(dp = 12.dp)

                    }
                }
                if (currencyRateState.listOfConvertedAgainstBase.isNotEmpty()) {
                    items(
                        items = currencyRateState.listOfConvertedAgainstBase
                    ) { pair: Pair<ExchangeRate, BigDecimal> ->

                        CurrenciesListItem(pair)
                    }
                }

            }

            if (currencyRateState.isCurrenciesLoading ||
                currencyRateState.isFavoriteLoading ||
                currencyRateState.isConverting ||
                currencyRateState.isForceSyncingExchangeRate
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CurrenciesListItem(pair: Pair<ExchangeRate, BigDecimal>) {
    val exchangeRate = pair.first
    val convertedAmount = pair.second
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Purple80,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Body2Medium(text = exchangeRate.currency)
            if (exchangeRate.currencyName.isNotEmpty())
                Body2Normal(
                    text = " (${exchangeRate.currencyName})",
                    color = R.color.white
                )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Body1Normal(text = "${stringResource(id = R.string.rate)} :")
            Body2Normal(text = exchangeRate.rate.toString())
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Body1Normal(text = "${stringResource(id = R.string.amount)} :")
            Body2Medium(text = convertedAmount.toString())
        }
    }
    VerticalDivider(dp = 8.dp)

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    toSelectedCurrencyList: List<ExchangeRate>?,
    onRemove: (index: Int, item: ExchangeRate) -> Unit
) {
    FlowRow(
        modifier = Modifier.padding(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        toSelectedCurrencyList?.forEachIndexed { index, item ->
            val itemModifier = Modifier
                .wrapContentWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF9681EB))

            Row(
                modifier = itemModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currencyName =
                    if (item.currencyName.isNotEmpty()) "(${item.currencyName})" else String.empty
                Text(
                    modifier = Modifier.weight(9f).padding(8.dp),
                    text = "${item.currency} " + currencyName
                )
                ComposeIcon(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .size(20.dp)
                        .clickable {
                            onRemove.invoke(index, item)
                        },
                    tint = Color.White,
                    icon = R.drawable.ic_cross
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
    defaultText: String,
    currencyRateState: ExchangeRateScreenState,
    onItemSelected: (selectedCurrency: ExchangeRate) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(defaultText) }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencyRateState.listOfCurrency?.forEach { item ->
                    val currencyName =
                        if (item.currencyName.isNotEmpty()) "(${item.currencyName})" else String.empty
                    DropdownMenuItem(
                        text = { Text(text = "${item.currency} " + currencyName) },
                        onClick = {
                            selectedText = "${item.currency} " + currencyName
                            onItemSelected.invoke(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
