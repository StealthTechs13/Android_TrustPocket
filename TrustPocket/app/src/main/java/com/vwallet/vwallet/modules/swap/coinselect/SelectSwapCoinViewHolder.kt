package com.vwallet.vwallet.modules.swap.coinselect

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.setCoinImage
import com.vwallet.vwallet.core.setOnSingleClickListener
import com.vwallet.vwallet.modules.swap.SwapMainModule.CoinBalanceItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_swap_coin_select.*

class SelectSwapCoinViewHolder(
        override val containerView: View,
        val onClick: (item: CoinBalanceItem) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var coinItem: CoinBalanceItem? = null

    init {
        containerView.setOnSingleClickListener {
            coinItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(coinItem: CoinBalanceItem, showBottomBorder: Boolean) {
        this.coinItem = coinItem
        bottomShade.isVisible = showBottomBorder

        coinItem.apply {
            coinIcon.setCoinImage(coin.type)
            coinTitle.text = coin.title
            coinSubtitle.text = coin.code

            coinBalance.text = balance?.let {
                App.numberFormatter.formatCoin(it, coin.code, 0, 8)
            }

            fiatBalance.text = fiatBalanceValue?.let {
                App.numberFormatter.formatFiat(fiatBalanceValue.value, fiatBalanceValue.currency.symbol, 0, 2)
            }
        }
    }

}
