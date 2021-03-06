package com.vwallet.vwallet.modules.swap.providerselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.modules.swap.SwapMainService

object SelectSwapProviderModule {

    class Factory(
            private val service: SwapMainService
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                SelectSwapProviderViewModel::class.java -> {
                    SelectSwapProviderViewModel(service) as T
                }
                else -> throw IllegalArgumentException()
            }
        }

    }

}
