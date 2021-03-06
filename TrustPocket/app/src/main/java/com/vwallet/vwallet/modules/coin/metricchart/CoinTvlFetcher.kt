package com.vwallet.vwallet.modules.coin.metricchart

import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.IRateManager
import com.vwallet.vwallet.modules.metricchart.MetricChartModule
import io.horizontalsystems.coinkit.models.CoinType
import io.horizontalsystems.xrateskit.entities.TimePeriod
import io.reactivex.Single

class CoinTvlFetcher(
        private val rateManager: IRateManager,
        private val coinType: CoinType
):MetricChartModule.IMetricChartFetcher, MetricChartModule.IMetricChartConfiguration {

    override val title = R.string.CoinPage_Tvl

    override val description = R.string.CoinPage_TvlDescription

    override val valueType: MetricChartModule.ValueType
        get() = MetricChartModule.ValueType.CompactCurrencyValue

    override fun fetchSingle(currencyCode: String, timePeriod: TimePeriod): Single<List<MetricChartModule.Item>> {
        return rateManager.defiTvlPoints(coinType, currencyCode, timePeriod)
                .map { points ->
                    points.map { MetricChartModule.Item(it.tvl, it.timestamp) }
                }
    }
}
