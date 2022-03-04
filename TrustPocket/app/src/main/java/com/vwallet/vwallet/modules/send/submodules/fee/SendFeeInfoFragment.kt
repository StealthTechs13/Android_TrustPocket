package com.vwallet.vwallet.modules.send.submodules.fee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.BaseFragment
import kotlinx.android.synthetic.main.fragment_fee_info.*

class SendFeeInfoFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_fee_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuClose -> {
                    parentFragmentManager.popBackStack()
                    true
                }
                else -> false
            }
        }
    }
}
