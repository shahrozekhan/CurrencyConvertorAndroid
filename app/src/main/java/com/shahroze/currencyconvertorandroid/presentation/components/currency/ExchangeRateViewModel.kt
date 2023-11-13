package com.shahroze.currencyconvertorandroid.presentation.components.currency

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.enums.TimeStampState
import com.shahroze.currencyconvertorandroid.common.utils.TimeStampUtils
import com.shahroze.currencyconvertorandroid.data.localdatasource.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.di.modules.Default
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.appDefaultExchangeRate
import com.shahroze.currencyconvertorandroid.domain.usecases.ConvertExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.GetFavoriteExchangeRateUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.LoadExchangeRateUsesCase
import com.shahroze.currencyconvertorandroid.domain.usecases.MarkExchangeRateToFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val loadExchangeRateUsesCase: LoadExchangeRateUsesCase,
    private val markExchangeRateToFavoriteUseCase: MarkExchangeRateToFavoriteUseCase,
    getFavoriteExchangeRateUseCase: GetFavoriteExchangeRateUseCase,
    private val convertExchangeRateUseCase: ConvertExchangeRateUseCase,
    @Default private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _mutableExchangeRateViewState = mutableStateOf(ExchangeRateScreenState())
    val exchangeRateViewState: State<ExchangeRateScreenState> = _mutableExchangeRateViewState

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

            is ExchangeRateScreenEvent.SelectedToCurrency -> {
                viewModelScope.launch {
                    markExchangeRateToFavoriteUseCase(event.value)
                    val mutableFavoriteCurrencyList =
                        exchangeRateViewState.value.favoriteCurrencies?.toMutableList()
                    withContext(defaultDispatcher) {
                        if (mutableFavoriteCurrencyList?.filter { event.value == it }
                                .isNullOrEmpty()) {
                            mutableFavoriteCurrencyList?.add(event.value)
                        }
                    }
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        favoriteCurrencies = mutableFavoriteCurrencyList
                    )
                }
            }

            is ExchangeRateScreenEvent.RemoveCurrencyFromSelected -> {
                viewModelScope.launch {
                    markExchangeRateToFavoriteUseCase(event.value, false)
                    val mutableFavoriteCurrencyList =
                        exchangeRateViewState.value.favoriteCurrencies?.toMutableList()
                    mutableFavoriteCurrencyList?.removeAt(event.index)
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        favoriteCurrencies = mutableFavoriteCurrencyList
                    )
                }
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
        getFavoriteExchangeRateUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                        isFavoriteLoading = false,
                        favoriteCurrencies = it.data
                    )
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
                }
            }
        }.launchIn(viewModelScope)

        when (TimeStampUtils.isToday(appPreferences.timeStamp)) {
            TimeStampState.TODAY -> {
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
                                listOfCurrency = null,
                                errorMessage = it.message
                            )
                        }

                        is Resource.Loading -> {
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isCurrenciesLoading = true, listOfCurrency = null, errorMessage = ""
                            )
                        }
                    }
                }.onCompletion {
                    _mutableExchangeRateViewState.value =
                        exchangeRateViewState.value.copy(fromCurrency = exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                            ?: ExchangeRate.appDefaultExchangeRate)
                }.launchIn(viewModelScope)

            }

            TimeStampState.NOT_TODAY -> {
                loadExchangeRateUsesCase.remoteExchangeRate().onEach {
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
                                listOfCurrency = null,
                                errorMessage = it.message
                            )
                        }

                        is Resource.Loading -> {
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isCurrenciesLoading = true, listOfCurrency = null, errorMessage = ""
                            )
                        }
                    }
                }.onCompletion {
                    _mutableExchangeRateViewState.value =
                        exchangeRateViewState.value.copy(fromCurrency = exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                            ?: ExchangeRate.appDefaultExchangeRate)
                }.launchIn(viewModelScope)
            }

            TimeStampState.NOT_EXIST -> {
                loadExchangeRateUsesCase.remoteExchangeRate().onEach {
                    when (it) {
                        is Resource.Success -> {
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isCurrenciesLoading = false,
                                listOfCurrency = it.data,
                                errorMessage = ""
                            )
                            _mutableExchangeRateViewState.value =
                                exchangeRateViewState.value.copy(fromCurrency = exchangeRateViewState.value.listOfCurrency?.find { appPreferences.baseCurrency == it.currency }
                                    ?: ExchangeRate.appDefaultExchangeRate)

                        }

                        is Resource.Error -> {
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
                                                listOfCurrency = null,
                                                errorMessage = it.message
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

                        is Resource.Loading -> {
                            _mutableExchangeRateViewState.value = exchangeRateViewState.value.copy(
                                isCurrenciesLoading = true, listOfCurrency = null, errorMessage = ""
                            )
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
}

