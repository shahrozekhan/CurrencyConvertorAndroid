package com.shahroze.currencyconvertorandroid.presentation.components.conversionscreen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.enums.TimeStampState
import com.shahroze.currencyconvertorandroid.common.utils.TimeStampUtils
import com.shahroze.currencyconvertorandroid.data.source.local.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.appDefaultExchangeRate
import com.shahroze.currencyconvertorandroid.domain.usecases.ConvertExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.GetFavoriteExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.LoadExchangeRateUsesCase
import com.shahroze.currencyconvertorandroid.domain.usecases.MarkExchangeRateToFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val loadExchangeRateUsesCase: LoadExchangeRateUsesCase,
    private val markExchangeRateToFavoriteUseCase: MarkExchangeRateToFavoriteUseCase,
    private val getFavoriteExchangeRateUseCase: GetFavoriteExchangeRateUseCase,
    private val convertExchangeRateUseCase: ConvertExchangeRateUseCase
) : ViewModel() {

    private val _mutableExchangeRateViewState = mutableStateOf(ExchangeRateScreenState())
    val exchangeRateViewState: State<ExchangeRateScreenState> = _mutableExchangeRateViewState

    private val _eventFlow = MutableSharedFlow<CommonUIEvent>()
    val commonEventFlow = _eventFlow.asSharedFlow()

    private fun setSelectedCurrency(selectedCurrency: String) {
        appPreferences.baseCurrency = selectedCurrency
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
                        event.value,
                        exchangeRateViewState.value.favoriteCurrencies
                    )
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        favoriteCurrencies = newFavoritesList
                    )
                }
            }

            is ExchangeRateScreenEvent.RemoveCurrencyFromSelected -> {
                viewModelScope.launch {
                    val newFavoritesList = markExchangeRateToFavoriteUseCase(
                        event.value,
                        exchangeRateViewState.value.favoriteCurrencies,
                        false
                    )
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        favoriteCurrencies = newFavoritesList
                    )
                }
            }

            is ExchangeRateScreenEvent.ForceSyncExchangeRate -> {
                _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                    isForceSyncingExchangeRate = true
                )
                loadExchangeRateFromRemote(
                    onSuccess = {
                        loadFavoriteExchangeRate(onCompleteLoading = {
                            if (!exchangeRateViewState.value.favoriteCurrencies.isNullOrEmpty())
                                onEvent(ExchangeRateScreenEvent.ConvertExchangeRate)
                        })
                        _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                            isForceSyncingExchangeRate = false
                        )
                    },
                    onError = {
                        showSnackBarError(
                            exchangeRateViewState.value.errorMessage,
                            SnackbarDuration.Indefinite
                        )
                        _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                            isForceSyncingExchangeRate = false
                        )
                    })
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


    init {
        loadFavoriteExchangeRate()
        loadExchangeRates()
    }

    private fun loadExchangeRates() {
        when (TimeStampUtils.isToday(appPreferences.timeStamp)) {
            TimeStampState.TODAY -> {
                loadExchangeRateFromDatabase()
            }

            TimeStampState.NOT_TODAY -> {
                loadExchangeRateFromRemote(onError = {
                    showSnackBarError(
                        exchangeRateViewState.value.errorMessage,
                        SnackbarDuration.Indefinite
                    )
                    loadExchangeRateFromDatabase()
                })
            }

            TimeStampState.NOT_EXIST -> {
                loadExchangeRateFromRemote(
                    onSuccess = {
                        _mutableExchangeRateViewState.value =
                            exchangeRateViewState.value.copy(fromCurrency =
                            exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                                ?: ExchangeRate.appDefaultExchangeRate)
                    }, onError = {
                        showSnackBarError(it.message, SnackbarDuration.Indefinite)
                        loadExchangeRateFromFile()
                    }
                )
            }
        }
    }

    private fun loadExchangeRateFromFile() {
        loadExchangeRateUsesCase.assetExchangeRate().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value =
                        exchangeRateViewState.value.copy(
                            isCurrenciesLoading = false,
                            listOfCurrency = it.data,
                            errorMessage = ""
                        )
                }

                is Resource.Error -> {
                    _mutableExchangeRateViewState.value =
                        exchangeRateViewState.value.copy(
                            isCurrenciesLoading = false,
                            errorMessage = it.message
                        )
                    showSnackBarError(
                        exchangeRateViewState.value.errorMessage,
                        SnackbarDuration.Indefinite
                    )
                }

                is Resource.Loading -> {
                    _mutableExchangeRateViewState.value =
                        exchangeRateViewState.value.copy(
                            isCurrenciesLoading = true,
                            listOfCurrency = null,
                            errorMessage = ""
                        )
                }
            }
        }.onCompletion {
            _mutableExchangeRateViewState.value =
                exchangeRateViewState.value.copy(fromCurrency = exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                    ?: ExchangeRate.appDefaultExchangeRate)
        }.launchIn(viewModelScope)
    }

    private fun loadExchangeRateFromRemote(
        onSuccess: suspend (Resource<List<ExchangeRate>>) -> Unit = {},
        onError: suspend (Resource<List<ExchangeRate>>) -> Unit = {},
        onLoading: suspend (Resource<List<ExchangeRate>>) -> Unit = {}
    ) {
        loadExchangeRateUsesCase.remoteExchangeRate().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = false,
                        listOfCurrency = it.data,
                        errorMessage = ""
                    )
                    onSuccess.invoke(it)
                }

                is Resource.Error -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = false,
                        errorMessage = it.message
                    )
                    onError.invoke(it)
                }

                is Resource.Loading -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = true,
                        errorMessage = ""
                    )
                    onLoading.invoke(it)
                }
            }
        }.onCompletion {
            _mutableExchangeRateViewState.value =
                exchangeRateViewState.value.copy(fromCurrency = exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                    ?: ExchangeRate.appDefaultExchangeRate)
        }.launchIn(viewModelScope)
    }

    private fun loadFavoriteExchangeRate(onCompleteLoading: () -> Unit = {}) {
        getFavoriteExchangeRateUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isFavoriteLoading = false,
                        favoriteCurrencies = it.data
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

    private fun loadExchangeRateFromDatabase() {
        loadExchangeRateUsesCase.databaseExchangeRate().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = false,
                        listOfCurrency = it.data,
                        errorMessage = ""
                    )
                }

                is Resource.Error -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = false,
                        errorMessage = it.message
                    )
                }

                is Resource.Loading -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isCurrenciesLoading = true,
                        errorMessage = ""
                    )
                }
            }
        }.onCompletion {
            _mutableExchangeRateViewState.value =
                exchangeRateViewState.value.copy(fromCurrency = exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                    ?: ExchangeRate.appDefaultExchangeRate)
        }.launchIn(viewModelScope)
    }

    private suspend fun showSnackBarError(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        _eventFlow.emit(
            CommonUIEvent.ShowSnackbar(
                message = message,
                duration = duration
            )
        )
    }
}

