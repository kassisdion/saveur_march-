package com.saveurmarche.saveurmarche.ui.main.tabs.markets

import android.text.Editable
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.ui.base.BasePresenter
import javax.inject.Inject

class MarketsPresenter @Inject constructor(private val marketManager: MarketsManager) :
        BasePresenter<MarketsContract.View>(),
        MarketsContract.Presenter {

    /*
    ************************************************************************************************
    **  Private field
    ************************************************************************************************
    */
    private val TAG = MarketsPresenter::class.java.simpleName
    private val mData: MutableList<Market> = mutableListOf()

    /*
    ************************************************************************************************
    **  MarketsContract.Presenter implementation
    ************************************************************************************************
    */
    override fun setupView() {
        view?.showLoading(true)
        fetchData()
    }

    override fun onFilterCtaClicked() {
    }

    override fun onItemClicked(position: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onAfterTextChanged(s: Editable?) {
        val newData = mData.filter { market ->
            val input = s?.toString()?.toLowerCase() ?: ""

            val nameMatch = market.name.toLowerCase().contains(input)
            val descMatch = market.description.toLowerCase().contains(input)
            val cityMatch = market.address!!.city.toLowerCase().contains(input)
            val countryMatch = market.address!!.country.toLowerCase().contains(input)

            nameMatch || descMatch || cityMatch || countryMatch
        }

        view?.setData(newData)
    }

    override fun onSwipeRefreshLayoutActivated() {
        fetchData()
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun fetchData() {
        registerDisposable(marketManager.getLocalMarket()
                .doAfterTerminate({
                    view?.showLoading(false)
                })
                .subscribe(
                        {
                            mData.clear()
                            mData.addAll(it)
                            view?.setData(it)
                        },
                        {
                            logE(TAG, { "fetchData > fail" }, RxJava2Debug.getEnhancedStackTrace(it))
                            //Should display error
                        }
                ))
    }
}