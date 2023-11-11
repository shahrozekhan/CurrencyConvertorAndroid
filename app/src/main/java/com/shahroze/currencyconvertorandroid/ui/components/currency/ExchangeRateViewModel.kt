package com.shahroze.currencyconvertorandroid.ui.components.currency

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahroze.currencyconvertorandroid.common.Response
import com.shahroze.currencyconvertorandroid.domain.usecases.CopyExchangeRateFromAssetsToDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    getCurrencyRateFromAssetsUseCase: CopyExchangeRateFromAssetsToDatabaseUseCase
) : ViewModel() {

    private val _mutableCurrencyRateState = mutableStateOf(CurrencyRateState())
    val currencyRateState: State<CurrencyRateState> = _mutableCurrencyRateState

    init {

        getCurrencyRateFromAssetsUseCase()
            .onEach {
                when (it) {
                    is Response.Success -> {
                        _mutableCurrencyRateState.value =
                            currencyRateState.value.copy(
                                isLoading = false,
                                listOfCurrency = it.data,
                                errorMessage = ""
                            )
                    }

                    is Response.Error -> {
                        _mutableCurrencyRateState.value =
                            currencyRateState.value.copy(
                                isLoading = false,
                                listOfCurrency = null,
                                errorMessage = it.message
                            )
                    }

                    is Response.Loading -> {
                        _mutableCurrencyRateState.value =
                            currencyRateState.value.copy(
                                isLoading = true,
                                listOfCurrency = null,
                                errorMessage = ""
                            )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}