package com.vwallet.vwallet.modules.swap.confirmation.oneinch

import androidx.fragment.app.viewModels
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.AppLogger
import com.vwallet.vwallet.core.ethereum.EthereumFeeViewModel
import com.vwallet.vwallet.modules.sendevmtransaction.SendEvmTransactionViewModel
import com.vwallet.vwallet.modules.swap.confirmation.BaseSwapConfirmationFragment
import io.horizontalsystems.core.findNavController

class OneInchSwapConfirmationFragment : BaseSwapConfirmationFragment() {
    override val logger = AppLogger("swap_1inch")

    private val vmFactory by lazy { OneInchConfirmationModule.Factory(dex.blockchain, requireArguments()) }
    override val sendViewModel by viewModels<SendEvmTransactionViewModel> { vmFactory }
    override val feeViewModel by viewModels<EthereumFeeViewModel> { vmFactory }

    override fun navigateToFeeInfo() {
        findNavController().navigate(R.id.oneInchConfirmationFragment_to_feeSpeedInfo, null, navOptions())
    }

}
