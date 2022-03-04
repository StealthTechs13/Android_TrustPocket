package com.vwallet.vwallet.modules.swap.settings.oneinch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.providers.Translator
import com.vwallet.vwallet.core.subscribeIO
import com.vwallet.vwallet.modules.swap.oneinch.OneInchTradeService
import com.vwallet.vwallet.modules.swap.settings.SwapSettingsModule.SwapSettingsError
import com.vwallet.vwallet.modules.swap.settings.oneinch.OneInchSwapSettingsModule.State
import io.reactivex.disposables.CompositeDisposable

class OneInchSettingsViewModel(
        private val service: OneInchSettingsService,
        private val tradeService: OneInchTradeService
) : ViewModel() {

    val actionStateLiveData = MutableLiveData<ActionState>(ActionState.Enabled)

    private val disposable = CompositeDisposable()

    init {
        service.stateObservable
                .subscribeIO {
                    syncAction()
                }.let {
                    disposable.add(it)
                }
    }

    private fun syncAction() {
        when (service.state) {
            is State.Valid -> {
                actionStateLiveData.postValue(ActionState.Enabled)
            }
            State.Invalid -> {
                val error = service.errors.firstOrNull() ?: return
                var errorText: String? = null

                when (error) {
                    is SwapSettingsError.InvalidAddress -> {
                        errorText = Translator.getString(R.string.SwapSettings_Error_InvalidAddress)
                    }
                    is SwapSettingsError.InvalidSlippage -> {
                        errorText = Translator.getString(R.string.SwapSettings_Error_InvalidSlippage)
                    }
                    is SwapSettingsError.ZeroDeadline -> {
                        errorText = Translator.getString(R.string.SwapSettings_Error_InvalidDeadline)
                    }
                }

                errorText?.let {
                    actionStateLiveData.postValue(ActionState.Disabled(it))
                }
            }
        }
    }

    override fun onCleared() {
        disposable.clear()
    }

    fun onDoneClick(): Boolean {
        return when (val state = service.state) {
            is State.Valid -> {
                tradeService.swapSettings = state.swapSettings
                true
            }
            is State.Invalid -> {
                false
            }
        }
    }

    sealed class ActionState {
        object Enabled : ActionState()
        class Disabled(val title: String) : ActionState()
    }
}
