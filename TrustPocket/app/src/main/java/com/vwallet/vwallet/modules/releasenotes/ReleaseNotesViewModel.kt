package com.vwallet.vwallet.modules.releasenotes

import androidx.lifecycle.ViewModel
import com.vwallet.vwallet.core.IAppConfigProvider
import com.vwallet.vwallet.core.managers.ReleaseNotesManager

class ReleaseNotesViewModel(
        appConfigProvider: IAppConfigProvider,
        releaseNotesManager: ReleaseNotesManager
) : ViewModel() {

    val releaseNotesUrl = releaseNotesManager.releaseNotesUrl
    val twitterUrl = appConfigProvider.appTwitterLink
    val telegramUrl = appConfigProvider.appTelegramLink
    val redditUrl = appConfigProvider.appRedditLink
}
