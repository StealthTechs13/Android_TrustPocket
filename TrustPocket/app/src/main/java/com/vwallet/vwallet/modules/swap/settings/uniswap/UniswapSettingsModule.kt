package com.vwallet.vwallet.modules.swap.settings.uniswap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.providers.Translator
import com.vwallet.vwallet.modules.swap.SwapMainModule
import com.vwallet.vwallet.modules.swap.settings.AddressResolutionService
import com.vwallet.vwallet.modules.swap.settings.RecipientAddressViewModel
import com.vwallet.vwallet.modules.swap.settings.SwapDeadlineViewModel
import com.vwallet.vwallet.modules.swap.settings.SwapSlippageViewModel
import com.vwallet.vwallet.modules.swap.uniswap.UniswapTradeService

object UniswapSettingsModule {

    sealed class State {
        class Valid(val tradeOptions: SwapTradeOptions) : State()
        object Invalid : State()
    }

    class Factory(
            private val tradeService: UniswapTradeService,
            private val blockchain: SwapMainModule.Blockchain
    ) : ViewModelProvider.Factory {

        private val service by lazy { UniswapSettingsService(tradeService.tradeOptions) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val evmCoin = blockchain.coin ?: throw IllegalArgumentException()

            return when (modelClass) {
                UniswapSettingsViewModel::class.java -> UniswapSettingsViewModel(service, tradeService) as T
                SwapDeadlineViewModel::class.java -> SwapDeadlineViewModel(service) as T
                SwapSlippageViewModel::class.java -> SwapSlippageViewModel(service) as T
                RecipientAddressViewModel::class.java -> {
                    val addressParser = App.addressParserFactory.parser(evmCoin)
                    val resolutionService = AddressResolutionService(evmCoin.code, true)
                    val placeholder = Translator.getString(R.string.SwapSettings_RecipientPlaceholder)
                    RecipientAddressViewModel(service, resolutionService, addressParser, placeholder, listOf(service, resolutionService)) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}
