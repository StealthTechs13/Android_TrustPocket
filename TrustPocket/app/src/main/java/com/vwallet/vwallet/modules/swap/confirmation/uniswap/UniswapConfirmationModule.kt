package com.vwallet.vwallet.modules.swap.confirmation.uniswap

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.ethereum.EthereumFeeViewModel
import com.vwallet.vwallet.core.ethereum.EvmCoinServiceFactory
import com.vwallet.vwallet.core.ethereum.EvmTransactionService
import com.vwallet.vwallet.core.factories.FeeRateProviderFactory
import com.vwallet.vwallet.modules.sendevm.SendEvmData
import com.vwallet.vwallet.modules.sendevm.SendEvmModule
import com.vwallet.vwallet.modules.sendevmtransaction.SendEvmTransactionService
import com.vwallet.vwallet.modules.sendevmtransaction.SendEvmTransactionViewModel
import com.vwallet.vwallet.modules.swap.SwapMainModule
import io.horizontalsystems.core.findNavController

object UniswapConfirmationModule {

    class Factory(
        private val blockchain: SwapMainModule.Blockchain,
        private val sendEvmData: SendEvmData
    ) : ViewModelProvider.Factory {

        private val evmKit by lazy { blockchain.evmKit!! }
        private val coin by lazy { blockchain.coin!! }
        private val transactionService by lazy {
            val feeRateProvider = FeeRateProviderFactory.provider(coin)!!
            EvmTransactionService(evmKit, feeRateProvider, 20)
        }
        private val coinServiceFactory by lazy {
            EvmCoinServiceFactory(
                coin,
                App.coinKit,
                App.currencyManager,
                App.xRateManager
            )
        }
        private val sendService by lazy {
            SendEvmTransactionService(
                sendEvmData,
                evmKit,
                transactionService,
                App.activateCoinManager
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                SendEvmTransactionViewModel::class.java -> {
                    SendEvmTransactionViewModel(sendService, coinServiceFactory) as T
                }
                EthereumFeeViewModel::class.java -> {
                    EthereumFeeViewModel(
                        transactionService,
                        coinServiceFactory.baseCoinService
                    ) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun start(
        fragment: Fragment,
        navigateTo: Int,
        navOptions: NavOptions,
        sendEvmData: SendEvmData
    ) {
        val arguments = bundleOf(
            SendEvmModule.transactionDataKey to SendEvmModule.TransactionDataParcelable(sendEvmData.transactionData),
            SendEvmModule.additionalInfoKey to sendEvmData.additionalInfo
        )
        fragment.findNavController().navigate(navigateTo, arguments, navOptions)
    }

}