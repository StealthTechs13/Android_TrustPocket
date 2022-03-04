package com.vwallet.vwallet.modules.swap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vwallet.vwallet.core.subscribeIO
import com.vwallet.vwallet.modules.swap.SwapMainModule.ISwapProvider
import io.reactivex.disposables.Disposable

class SwapMainViewModel(
        val service: SwapMainService
) : ViewModel() {

    private val disposable: Disposable

    val dex: SwapMainModule.Dex
        get() = service.dex

    val provider: ISwapProvider
        get() = service.dex.provider

    val providerLiveData = MutableLiveData<ISwapProvider>()

    var providerState by service::providerState

    init {
        disposable = service.providerObservable
                .subscribeIO {
                    providerLiveData.postValue(it)
                }
    }

    override fun onCleared() {
        disposable.dispose()
    }

}
