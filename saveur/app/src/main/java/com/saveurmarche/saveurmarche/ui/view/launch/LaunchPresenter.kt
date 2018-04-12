package com.saveurmarche.saveurmarche.ui.view.launch

import com.akaita.java.rxjava2debug.RxJava2Debug
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.helper.logD
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.ui.view.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LaunchPresenter @Inject constructor(private val marketManager: MarketsManager) :
        BasePresenter<LaunchContract.View>(),
        LaunchContract.Presenter {
    /*
    ************************************************************************************************
    ** LaunchContract.Presenter implementation
    ************************************************************************************************
    */
    override fun setupView() {
        registerDisposable(marketManager.fetchMarkets()
                .retry(2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            logD("LaunchPresenter", { "setupView > success" })
                            view?.redirectToMainActivity()
                        },
                        {
                            logE("LaunchPresenter", { "setupView > fail" }, RxJava2Debug.getEnhancedStackTrace(it))
                            view?.redirectToMainActivity()
                        }
                )
        )
    }
}
