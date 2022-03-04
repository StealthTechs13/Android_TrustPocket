package com.vwallet.vwallet.modules.swap.settings.oneinch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.providers.Translator
import com.vwallet.vwallet.entities.Address
import com.vwallet.vwallet.modules.swap.SwapMainModule
import com.vwallet.vwallet.modules.swap.oneinch.OneInchTradeService
import com.vwallet.vwallet.modules.swap.settings.AddressResolutionService
import com.vwallet.vwallet.modules.swap.settings.RecipientAddressViewModel
import com.vwallet.vwallet.modules.swap.settings.SwapSlippageViewModel
import java.math.BigDecimal

object OneInchSwapSettingsModule {

    val defaultSlippage = BigDecimal("1")

    data class OneInchSwapSettings(
            var slippage: BigDecimal = defaultSlippage,
            var gasPrice: Long? = null,
            var recipient: Address? = null
    )

    sealed class State {
        class Valid(val swapSettings: OneInchSwapSettings) : State()
        object Invalid : State()
    }

    class Factory(
            private val tradeService: OneInchTradeService,
            private val blockchain: SwapMainModule.Blockchain
    ) : ViewModelProvider.Factory {

        private val service by lazy { OneInchSettingsService(tradeService.swapSettings) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val evmCoin = blockchain.coin!!

            return when (modelClass) {
                OneInchSettingsViewModel::class.java -> OneInchSettingsViewModel(service, tradeService) as T
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
