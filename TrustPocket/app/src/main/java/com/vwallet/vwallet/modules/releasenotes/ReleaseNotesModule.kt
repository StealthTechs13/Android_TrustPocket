package com.vwallet.vwallet.modules.releasenotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.core.App

object ReleaseNotesModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReleaseNotesViewModel(App.appConfigProvider, App.releaseNotesManager) as T
        }
    }
}
