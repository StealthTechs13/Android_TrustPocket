package com.vwallet.vwallet.modules.swap.settings

import androidx.navigation.navGraphViewModels
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.BaseFragment
import com.vwallet.vwallet.modules.swap.SwapMainModule
import com.vwallet.vwallet.modules.swap.SwapMainViewModel

abstract class SwapSettingsBaseFragment : BaseFragment() {
    private val mainViewModel by navGraphViewModels<SwapMainViewModel>(R.id.swapFragment)

    val dex: SwapMainModule.Dex
        get() = mainViewModel.dex

}
