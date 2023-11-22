package com.shahroze.currencyconvertorandroid.presentation.components.conversionscreen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.source.local.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.appDefaultExchangeRate
import com.shahroze.currencyconvertorandroid.domain.usecases.conversion.ConvertExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerate.ForceSyncExchangeRatesUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerate.GetExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.favorite.GetFavoriteExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.favorite.MarkExchangeRateToFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val markExchangeRateToFavoriteUseCase: MarkExchangeRateToFavoriteUseCase,
    private val getFavoriteExchangeRateUseCase: GetFavoriteExchangeRateUseCase,
    private val convertExchangeRateUseCase: ConvertExchangeRateUseCase,
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val forceSyncExchangeRatesUseCase: ForceSyncExchangeRatesUseCase
) : ViewModel() {

    private val _mutableExchangeRateViewState = mutableStateOf(ExchangeRateScreenState())
    val exchangeRateViewState: State<ExchangeRateScreenState> = _mutableExchangeRateViewState

    private val _eventFlow = MutableSharedFlow<CommonUIEvent>()
    val commonEventFlow = _eventFlow.asSharedFlow()

    private fun setSelectedCurrency(selectedCurrency: String) {
        appPreferences.baseCurrency = selectedCurrency
    }

    init {
        loadFavoriteExchangeRate()
        loadExchangeRate()
    }

    fun onEvent(event: ExchangeRateScreenEvent) {
        when (event) {
            is ExchangeRateScreenEvent.EnteredAmount -> {
                _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                    amount = event.value
                )
            }

            is ExchangeRateScreenEvent.SelectFromCurrency -> {
                val selectedCurrency =
                    exchangeRateViewState.value.listOfCurrency?.find { event.value == it.currency }
                        ?: ExchangeRate.appDefaultExchangeRate
                _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                    fromCurrency = selectedCurrency
                )
            }

            is ExchangeRateScreenEvent.MarkToFavoriteCurrency -> {
                viewModelScope.launch {
                    val newFavoritesList = markExchangeRateToFavoriteUseCase(
                        event.value, exchangeRateViewState.value.favoriteCurrencies
                    )
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        favoriteCurrencies = newFavoritesList
                    )
                }
            }

            is ExchangeRateScreenEvent.RemoveCurrencyFromSelected -> {
                viewModelScope.launch {
                    val newFavoritesList = markExchangeRateToFavoriteUseCase(
                        event.value, exchangeRateViewState.value.favoriteCurrencies, false
                    )
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        favoriteCurrencies = newFavoritesList
                    )
                }
            }

            is ExchangeRateScreenEvent.ForceSyncExchangeRate -> {
                forceSyncExchangeRatesUseCase().onEach {
                    when (it) {
                        is Resource.Success -> {
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isForceSyncingExchangeRate = false,
                                listOfCurrency = it.data,
                                errorMessage = ""
                            )
                            loadFavoriteExchangeRate(onCompleteLoading = {
                                if (!exchangeRateViewState.value.favoriteCurrencies.isNullOrEmpty()) onEvent(
                                    ExchangeRateScreenEvent.ConvertExchangeRate
                                )
                            })
                        }

                        is Resource.Error -> {
                            showSnackBarError(
                                exchangeRateViewState.value.errorMessage,
                                SnackbarDuration.Indefinite
                            )
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isForceSyncingExchangeRate = false
                            )
                        }

                        is Resource.Loading -> {
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isForceSyncingExchangeRate = true
                            )
                        }
                    }
                }.launchIn(viewModelScope)
            }

            is ExchangeRateScreenEvent.ConvertExchangeRate -> {
                viewModelScope.launch {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isConverting = true
                    )
                    setSelectedCurrency(exchangeRateViewState.value.fromCurrency.currency)

                    exchangeRateViewState.value.apply {
                        val listOfConvertedExchangeRate = convertExchangeRateUseCase(
                            amount = amount,
                            fromExchangeRate = fromCurrency,
                            toExchangeRateList = favoriteCurrencies
                        )
                        _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                            listOfConvertedAgainstBase = listOfConvertedExchangeRate,
                            isConverting = false
                        )

                    }
                }
            }
        }
    }

    private fun loadExchangeRate() {
        getExchangeRateUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = false, listOfCurrency = it.data, errorMessage = ""
                    )
                }

                is Resource.Error -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = false, errorMessage = it.message
                    )
                    showSnackBarError(exchangeRateViewState.value.errorMessage)

                }

                is Resource.Loading -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = true, errorMessage = ""
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadFavoriteExchangeRate(onCompleteLoading: () -> Unit = {}) {
        getFavoriteExchangeRateUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isFavoriteLoading = false, favoriteCurrencies = it.data
                    )
                    onCompleteLoading.invoke()
                }

                is Resource.Loading -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isFavoriteLoading = true
                    )
                }

                is Resource.Error -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isFavoriteLoading = false
                    )
                    showSnackBarError(it.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun showSnackBarError(
        message: String, duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        _eventFlow.emit(
            CommonUIEvent.ShowSnackbar(
                message = message, duration = duration
            )
        )
    }
}

