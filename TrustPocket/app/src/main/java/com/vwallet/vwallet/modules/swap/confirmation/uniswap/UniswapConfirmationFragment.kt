package com.vwallet.vwallet.modules.swap.confirmation.uniswap

import androidx.fragment.app.viewModels
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.AppLogger
import com.vwallet.vwallet.core.ethereum.EthereumFeeViewModel
import com.vwallet.vwallet.modules.sendevm.SendEvmData
import com.vwallet.vwallet.modules.sendevm.SendEvmModule
import com.vwallet.vwallet.modules.sendevmtransaction.SendEvmTransactionViewModel
import com.vwallet.vwallet.modules.swap.confirmation.BaseSwapConfirmationFragment
import io.horizontalsystems.core.findNavController
import io.horizontalsystems.ethereumkit.models.Address
import io.horizontalsystems.ethereumkit.models.TransactionData

class UniswapConfirmationFragment : BaseSwapConfirmationFragment() {
    override val logger = AppLogger("swap_1inch")

    private val transactionData: TransactionData
        get() {
            val transactionDataParcelable = arguments?.getParcelable<SendEvmModule.TransactionDataParcelable>(SendEvmModule.transactionDataKey)!!
            return TransactionData(
                    Address(transactionDataParcelable.toAddress),
                    transactionDataParcelable.value,
                    transactionDataParcelable.input
            )
        }

    private val additionalInfo: SendEvmData.AdditionalInfo?
        get() = arguments?.getParcelable(SendEvmModule.additionalInfoKey)

    private val vmFactory by lazy { UniswapConfirmationModule.Factory(dex.blockchain, SendEvmData(transactionData, additionalInfo)) }
    override val sendViewModel by viewModels<SendEvmTransactionViewModel> { vmFactory }
    override val feeViewModel by viewModels<EthereumFeeViewModel> { vmFactory }

    override fun navigateToFeeInfo() {
        findNavController().navigate(R.id.uniswapConfirmationFragment_to_feeSpeedInfo, null, navOptions())
    }

}
