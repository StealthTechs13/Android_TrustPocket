package com.vwallet.vwallet.modules.balanceonboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vwallet.vwallet.core.IAccountManager
import com.vwallet.vwallet.core.IWalletManager
import io.reactivex.disposables.CompositeDisposable

class BalanceOnboardingViewModel(
        private val accountManager: IAccountManager,
        private val walletManager: IWalletManager
) : ViewModel() {

    val balanceViewTypeLiveData = MutableLiveData<BalanceViewType>()

    val disposables = CompositeDisposable()

    init {
        accountManager.accountsFlowable
                .subscribe { syncBalanceViewType() }
                .let { disposables.add(it) }

        walletManager.activeWalletsUpdatedObservable
                .subscribe { syncBalanceViewType() }
                .let { disposables.add(it) }

        syncBalanceViewType()
    }

    private fun syncBalanceViewType() {
        val balanceViewType = when {
            accountManager.accounts.isEmpty() -> BalanceViewType.NoAccounts
            walletManager.activeWallets.isEmpty() -> BalanceViewType.NoCoins(accountManager.activeAccount?.name)
            else -> BalanceViewType.Balance
        }
        balanceViewTypeLiveData.postValue(balanceViewType)
    }

    override fun onCleared() {
        disposables.clear()
    }

    sealed class BalanceViewType {
        object NoAccounts: BalanceViewType()
        class NoCoins(val accountName: String?): BalanceViewType()
        object Balance: BalanceViewType()
    }

}
