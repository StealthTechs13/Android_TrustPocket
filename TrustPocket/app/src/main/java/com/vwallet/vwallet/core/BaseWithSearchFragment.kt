package com.vwallet.vwallet.core

import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.vwallet.vwallet.R


abstract class BaseWithSearchFragment : BaseFragment(), SearchViewHelper.Listener {

    abstract override fun updateFilter(query: String)
    override fun searchExpanded(menu: Menu) {}
    override fun searchCollapsed(menu: Menu) {}

    private var searchView: SearchView? = null

    protected fun configureSearchMenu(menu: Menu, hint: Int = R.string.ManageCoins_Search) {
        context?.let { context ->
            SearchViewHelper.configureSearchMenu(context, menu, context.getString(hint), this)
        }
    }

    override fun onDetach() {
        super.onDetach()

        searchView?.setOnQueryTextListener(null)
    }
}
