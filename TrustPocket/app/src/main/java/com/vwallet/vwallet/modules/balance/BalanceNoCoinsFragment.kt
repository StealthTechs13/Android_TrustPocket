package com.vwallet.vwallet.modules.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.BaseFragment
import com.vwallet.vwallet.modules.manageaccounts.ManageAccountsModule
import io.horizontalsystems.core.findNavController
import kotlinx.android.synthetic.main.fragment_no_coins.*

class BalanceNoCoinsFragment(private val accountName: String?) : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_coins, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarTitle.text = accountName ?: getString(R.string.Balance_Title)
        toolbarTitle.setOnClickListener {
            ManageAccountsModule.start(this, R.id.manageAccountsFragment, navOptionsFromBottom(), ManageAccountsModule.Mode.Switcher)
        }

        addCoinsButton.setOnClickListener {
            findNavController().navigate(R.id.manageWalletsFragment, null, navOptions())
        }
    }

}
