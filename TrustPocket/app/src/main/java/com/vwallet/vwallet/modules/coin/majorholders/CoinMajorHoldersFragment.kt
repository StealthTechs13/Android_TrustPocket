package com.vwallet.vwallet.modules.coin.majorholders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.BaseFragment
import com.vwallet.vwallet.modules.coin.CoinViewModel
import com.vwallet.vwallet.modules.coin.MajorHolderItem
import com.vwallet.vwallet.ui.helpers.LinkHelper
import com.vwallet.vwallet.ui.helpers.TextHelper
import io.horizontalsystems.core.findNavController
import io.horizontalsystems.core.helpers.HudHelper
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class CoinMajorHoldersFragment : BaseFragment(), CoinMajorHoldersAdapter.Listener {

    private val coinViewModel by navGraphViewModels<CoinViewModel>(R.id.coinFragment)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = getString(R.string.CoinPage_MajorHolders)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        coinViewModel.coinMajorHolders.observe(viewLifecycleOwner, { holders ->
            val adapterChart = CoinMajorHoldersPieAdapter(holders.filterIsInstance(MajorHolderItem.Item::class.java))
            val adapterItems = CoinMajorHoldersAdapter(holders, this)
            recyclerView.adapter = ConcatAdapter(adapterChart, adapterItems)
        })
    }

    override fun onAddressClick(address: String) {
        TextHelper.copyText(address)
        HudHelper.showSuccessMessage(requireView(), R.string.Hud_Text_Copied)
    }

    override fun onDetailsClick(address: String) {
        context?.let { ctx ->
            LinkHelper.openLinkInAppBrowser(ctx, "https://etherscan.io/address/$address")
        }
    }
}
