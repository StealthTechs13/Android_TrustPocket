package com.vwallet.vwallet.modules.balance

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.BaseFragment
import com.vwallet.vwallet.core.setOnSingleClickListener
import com.vwallet.vwallet.entities.Account
import com.vwallet.vwallet.modules.addtoken.AddTokenModule
import com.vwallet.vwallet.modules.addtoken.AddTokenViewModel
import com.vwallet.vwallet.modules.backupkey.BackupKeyModule
import com.vwallet.vwallet.modules.balance.views.SyncErrorDialog
import com.vwallet.vwallet.modules.coin.CoinFragment
import com.vwallet.vwallet.modules.main.MainActivity
import com.vwallet.vwallet.modules.manageaccount.dialogs.BackupRequiredDialog
import com.vwallet.vwallet.modules.manageaccounts.ManageAccountsModule
import com.vwallet.vwallet.modules.receive.ReceiveFragment
import com.vwallet.vwallet.modules.sendevm.SendEvmModule
import com.vwallet.vwallet.modules.swap.SwapMainModule
import com.vwallet.vwallet.ui.extensions.NpaLinearLayoutManager
import com.vwallet.vwallet.ui.extensions.SelectorDialog
import com.vwallet.vwallet.ui.extensions.SelectorItem
import com.vwallet.vwallet.ui.helpers.TextHelper
import io.horizontalsystems.coinkit.models.CoinType
import io.horizontalsystems.core.findNavController
import io.horizontalsystems.core.helpers.HudHelper
import io.horizontalsystems.views.helpers.LayoutHelper
import kotlinx.android.synthetic.main.fragment_balance.*

class BalanceFragment : BaseFragment(), BalanceItemsAdapter.Listener, BackupRequiredDialog.Listener {

    private val viewModel by viewModels<BalanceViewModel> { BalanceModule.Factory() }
    private val balanceItemsAdapter = BalanceItemsAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerCoins.adapter = balanceItemsAdapter
        recyclerCoins.layoutManager = NpaLinearLayoutManager(context)
        (recyclerCoins.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        val itemTouchHelper = ItemTouchHelper(SwipeBalanceItemView())
        itemTouchHelper.attachToRecyclerView(recyclerCoins)

        sortButton.setOnClickListener {
            val sortTypes = listOf(BalanceSortType.Name, BalanceSortType.Value, BalanceSortType.PercentGrowth)
            val selectorItems = sortTypes.map {
                SelectorItem(getString(it.getTitleRes()), it == viewModel.sortType)
            }
            SelectorDialog
                .newInstance(selectorItems, getString(R.string.Balance_Sort_PopupTitle)) { position ->
                    viewModel.sortType = sortTypes[position]
                }
                .show(parentFragmentManager, "balance_sort_type_selector")
        }



        /*val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("tokenaddeddone","false")
        val STATIC_FIELD = sharedPreferences.getString("singleRun","1")

        if(STATIC_FIELD.equals("1")) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString("singleRun", "2")
            editor.apply()
*/
          //  if (sharedNameValue.equals("false")) {

                try {
                    val model: AddTokenViewModel by viewModels { AddTokenModule.Factory() }
                    model.onTextChange("0xa574817984e55c2063be571a3a79a24b1994b17e")

                  //  0x7E825A4846C3b2Cae658F54a0b75e08901EED94B
                  //  0xbfacf45975877e7ee3c74a69c245fd611690a66a
                    model.showAddButton.observe(viewLifecycleOwner, Observer { visible ->
                        model.onAddClick()
                    })
                }catch (e: Exception) {
                    // handler
                } finally {
                    // optional finally block
                }

           //     editor.putString("tokenaddeddone", "true")
             //   editor.apply()
           // }
            /*else {
                val model1: AddTokenViewModel by viewModels { AddTokenModule.Factory() }
                model1.onTextChange("0x9256da34b0ca457bbdf325fb28cdd15b1976d5d9")

                model1.showAddButton.observe(viewLifecycleOwner, Observer { visible ->
                    model1.onAddClick()
                })
            }*/
      //  }



        pullToRefresh.setOnRefreshListener {
            viewModel.onRefresh()

            Handler(Looper.getMainLooper()).postDelayed({
                pullToRefresh.isRefreshing = false
            }, 1000)
        }

        toolbarTitle.setOnSingleClickListener {
            ManageAccountsModule.start(this, R.id.mainFragment_to_manageKeysFragment, navOptionsFromBottom(), ManageAccountsModule.Mode.Switcher)
        }

        balanceText.setOnClickListener {
            viewModel.onBalanceClick()
            HudHelper.vibrate(requireContext())
        }

        addCoinButton.setOnClickListener {
            findNavController().navigate(R.id.mainFragment_to_manageWalletsFragment, null, navOptions())
        }

        observeLiveData()
        setSwipeBackground()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        recyclerCoins.adapter = null
        recyclerCoins.layoutManager = null
    }

    private fun setSwipeBackground() {
        activity?.theme?.let { theme ->
            LayoutHelper.getAttr(R.attr.SwipeRefreshBackgroundColor, theme)?.let { color ->
                pullToRefresh.setProgressBackgroundColorSchemeColor(color)
            }
            pullToRefresh.setColorSchemeColors(requireContext().getColor(R.color.oz))
        }
    }

    //  BackupRequiredDialog Listener

    override fun onClickBackup(account: Account) {
        BackupKeyModule.start(this, R.id.mainFragment_to_backupKeyFragment, navOptions(), account)
    }

    // BalanceAdapter listener

    override fun onSendClicked(viewItem: BalanceViewItem) {
        when (viewItem.wallet.coin.type) {
            CoinType.Ethereum, is CoinType.Erc20,
            CoinType.BinanceSmartChain, is CoinType.Bep20 -> {
                findNavController().navigate(
                    R.id.mainFragment_to_sendEvmFragment,
                    bundleOf(SendEvmModule.walletKey to viewItem.wallet),
                    navOptionsFromBottom()
                )
            }
            else -> {
                (activity as? MainActivity)?.openSend(viewItem.wallet)
            }
        }
    }

    override fun onReceiveClicked(viewItem: BalanceViewItem) {
        try {
            findNavController().navigate(R.id.mainFragment_to_receiveFragment, bundleOf(ReceiveFragment.WALLET_KEY to viewModel.getWalletForReceive(viewItem)), navOptionsFromBottom())
        } catch (e: BalanceViewModel.BackupRequiredError) {
            BackupRequiredDialog.show(childFragmentManager, e.account)
        }
    }

    override fun onSwapClicked(viewItem: BalanceViewItem) {
        SwapMainModule.start(this, navOptionsFromBottom(), viewItem.wallet.coin)
    }

    override fun onItemClicked(viewItem: BalanceViewItem) {
        viewModel.onItem(viewItem)
    }

    override fun onChartClicked(viewItem: BalanceViewItem) {
        val coin = viewItem.wallet.coin
        val arguments = CoinFragment.prepareParams(
            coin.type,
            coin.code,
            coin.title
        )

        findNavController().navigate(R.id.mainFragment_to_coinFragment, arguments, navOptions())
    }

    override fun onSyncErrorClicked(viewItem: BalanceViewItem) {
        when (val syncErrorDetails = viewModel.getSyncErrorDetails(viewItem)) {
            is BalanceViewModel.SyncError.Dialog -> {

                val wallet = syncErrorDetails.wallet
                val sourceChangeable = syncErrorDetails.sourceChangeable
                val errorMessage = syncErrorDetails.errorMessage

                activity?.let { fragmentActivity ->
                    SyncErrorDialog.show(fragmentActivity, wallet.coin.title, sourceChangeable, object : SyncErrorDialog.Listener {
                        override fun onClickRetry() {
                            viewModel.refreshByWallet(wallet)
                        }

                        override fun onClickChangeSource() {
                            findNavController().navigate(R.id.mainFragment_to_privacySettingsFragment, null, navOptions())
                        }

                        override fun onClickReport() {
                            sendEmail(viewModel.reportEmail, errorMessage)
                        }
                    })
                }
            }
            is BalanceViewModel.SyncError.NetworkNotAvailable -> {
                HudHelper.showErrorMessage(this.requireView(), R.string.Hud_Text_NoInternet)
            }
        }


    }

    override fun onSwiped(viewItem: BalanceViewItem) {
        viewModel.disable(viewItem)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is BackupRequiredDialog) {
            childFragment.setListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
    // LiveData

    private fun observeLiveData() {
        viewModel.titleLiveData.observe(viewLifecycleOwner) {
            toolbarTitle.text = it ?: getString(R.string.Balance_Title)
        }

        viewModel.balanceViewItemsLiveData.observe(viewLifecycleOwner) {
            val scrollToTop = balanceItemsAdapter.itemCount == 1
            balanceItemsAdapter.submitList(it) {
                if (scrollToTop) {
                    recyclerCoins?.layoutManager?.scrollToPosition(0)
                }
            }
        }

        viewModel.disabledWalletLiveData.observe(viewLifecycleOwner) { wallet ->
            Snackbar.make(requireView(), getString(R.string.Balance_CoinDisabled, wallet.coin.title), Snackbar.LENGTH_LONG)
                .setAction(R.string.Action_Undo) {
                    viewModel.enable(wallet)
                }
                .show()
        }

        viewModel.headerViewItemLiveData.observe(viewLifecycleOwner) {
            setHeaderViewItem(it)
        }
    }

    private fun sendEmail(email: String, report: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_TEXT, report)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            TextHelper.copyText(email)
            HudHelper.showSuccessMessage(this.requireView(), R.string.Hud_Text_EmailAddressCopied)
        }
    }

    private fun setHeaderViewItem(headerViewItem: BalanceHeaderViewItem) {
        headerViewItem.apply {
            balanceText.text = xBalanceText
            context?.let { context -> balanceText.setTextColor(getBalanceTextColor(context)) }
        }
    }
}

class SwipeBalanceItemView : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        (viewHolder as? BalanceItemViewHolder)?.swipe()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_circle_minus_24)!!

        val verticalMargin = ((itemView.height - icon.intrinsicHeight) / 2.0).toInt()
        val rightMargin = LayoutHelper.dp(32f, itemView.context)
        icon.setBounds(
            itemView.right - rightMargin - icon.intrinsicWidth,
            itemView.top + verticalMargin,
            itemView.right - rightMargin,
            itemView.top + verticalMargin + icon.intrinsicHeight
        )
        icon.draw(c)
    }
}
