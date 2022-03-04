package com.vwallet.vwallet.core.managers

import android.app.Activity
import com.vwallet.vwallet.modules.keystore.KeyStoreActivity
import com.vwallet.vwallet.modules.lockscreen.LockScreenActivity
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.core.IKeyStoreManager
import io.horizontalsystems.core.IPinComponent
import io.horizontalsystems.core.ISystemInfoManager
import io.horizontalsystems.core.security.KeyStoreValidationResult

class BackgroundStateChangeListener(
        private val systemInfoManager: ISystemInfoManager,
        private val keyStoreManager: IKeyStoreManager,
        private val pinComponent: IPinComponent)
    : BackgroundManager.Listener {

    override fun willEnterForeground(activity: Activity) {
        if (systemInfoManager.isSystemLockOff){
            KeyStoreActivity.startForNoSystemLock(activity)
            return
        }

        when(keyStoreManager.validateKeyStore()) {
            KeyStoreValidationResult.UserNotAuthenticated -> {
                KeyStoreActivity.startForUserAuthentication(activity)
                return
            }
            KeyStoreValidationResult.KeyIsInvalid -> {
                KeyStoreActivity.startForInvalidKey(activity)
                return
            }
            KeyStoreValidationResult.KeyIsValid -> { /* Do nothing */}
        }

        pinComponent.willEnterForeground(activity)

        if (pinComponent.shouldShowPin(activity)){
            LockScreenActivity.start(activity)
        }
    }

    override fun didEnterBackground() {
        pinComponent.didEnterBackground()
    }
}
