package com.vwallet.vwallet.modules.balance

import com.vwallet.vwallet.core.Clearable
import com.vwallet.vwallet.core.IAccountManager
import com.vwallet.vwallet.core.subscribeIO
import com.vwallet.vwallet.entities.Account
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class ActiveAccountService(private val accountManager: IAccountManager) : Clearable {
    private val disposables = CompositeDisposable()

    private val activeAccountSubject = BehaviorSubject.create<Account>()
    val activeAccountObservable: Flowable<Account> = activeAccountSubject.toFlowable(BackpressureStrategy.DROP)

    init {
        refreshActiveAccount()

        accountManager.activeAccountObservable
            .subscribeIO {
                refreshActiveAccount()
            }
            .let {
                disposables.add(it)
            }

        accountManager.accountsFlowable
            .subscribeIO {
                refreshActiveAccount()
            }
            .let {
                disposables.add(it)
            }

    }

    private fun refreshActiveAccount() {
        accountManager.activeAccount?.let {
            activeAccountSubject.onNext(it)
        }
    }

    override fun clear() {
        disposables.clear()
    }

}
