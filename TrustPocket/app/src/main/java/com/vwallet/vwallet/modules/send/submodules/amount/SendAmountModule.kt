package com.vwallet.vwallet.modules.send.submodules.amount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.entities.CoinValue
import com.vwallet.vwallet.entities.CurrencyValue
import com.vwallet.vwallet.entities.Wallet
import com.vwallet.vwallet.modules.send.SendModule
import com.vwallet.vwallet.modules.send.SendModule.AmountInfo
import com.vwallet.vwallet.ui.extensions.AmountInputView
import java.math.BigDecimal
import kotlin.math.min

object SendAmountModule {

    interface IView {

        fun setLoading(loading: Boolean)
        fun setAmount(amount: String)
        fun setAvailableBalance(availableBalance: String)
        fun setHint(hint: String)
        fun setValidationError(error: ValidationError?)
        fun revertAmount(amount: String)
        fun setMaxButtonVisible(visible: Boolean)
        fun setInputFields(inputParams: AmountInputView.InputParams)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onAmountChange(amountString: String)
        fun onSwitchClick()
        fun onMaxClick()
    }

    interface IInteractor {
        var defaultInputType: SendModule.InputType
        fun getRate(): BigDecimal?
        fun onCleared()
    }

    interface IInteractorDelegate {
        fun didUpdateRate(rate: BigDecimal)
        fun willEnterForeground()
    }

    interface IAmountModule {

        val xRate: BigDecimal?
        val sendAmountInfo: SendAmountInfo
        val currentAmount: BigDecimal
        val inputType: SendModule.InputType
        val coinAmount: CoinValue
        val fiatAmount: CurrencyValue?

        @Throws
        fun primaryAmountInfo(): AmountInfo

        @Throws
        fun secondaryAmountInfo(): AmountInfo?

        @Throws
        fun validAmount(): BigDecimal

        fun setLoading(loading: Boolean)
        fun setAmount(amount: BigDecimal)
        fun setAvailableBalance(availableBalance: BigDecimal)
        fun setMinimumAmount(minimumAmount: BigDecimal)
        fun setMaximumAmount(maximumAmount: BigDecimal?)
        fun setMinimumRequiredBalance(minimumRequiredBalance: BigDecimal)
    }

    interface IAmountModuleDelegate {
        fun onChangeAmount()
        fun onChangeInputType(inputType: SendModule.InputType)
        fun onRateUpdated(rate: BigDecimal?) {}
    }

    open class ValidationError : Exception() {
        class EmptyValue(val field: String) : ValidationError()
        class InsufficientBalance(val availableBalance: AmountInfo?) : ValidationError()
        class NotEnoughForMinimumRequiredBalance(val minimumRequiredBalance: CoinValue) : ValidationError()
        class TooFewAmount(val minimumAmount: AmountInfo?) : ValidationError()
        class MaxAmountLimit(val maximumAmount: AmountInfo?) : ValidationError()
    }

    class Factory(private val wallet: Wallet,
                  private val sendHandler: SendModule.ISendHandler) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val view = SendAmountView()
            val coinDecimal = min(wallet.coin.decimal, App.appConfigProvider.maxDecimal)
            val currencyDecimal = App.appConfigProvider.fiatDecimal
            val baseCurrency = App.currencyManager.baseCurrency

            val interactor = SendAmountInteractor(baseCurrency, App.xRateManager, App.localStorage, wallet.coin, App.backgroundManager)
            val sendAmountPresenterHelper =
                    SendAmountPresenterHelper(App.numberFormatter, wallet.coin, baseCurrency, coinDecimal,
                            currencyDecimal)
            val presenter = SendAmountPresenter(view, interactor, sendAmountPresenterHelper, wallet.coin, baseCurrency)

            sendHandler.amountModule = presenter
            interactor.delegate = presenter

            return presenter as T
        }
    }

}

sealed class SendAmountInfo {
    object Max : SendAmountInfo()
    class Entered(val amount: BigDecimal) : SendAmountInfo()
    object NotEntered : SendAmountInfo()
}
