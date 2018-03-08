package com.saveurmarche.saveurmarche.ui.main.tabs.markets

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.SaveurApplication
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.base.BaseFragment
import javax.inject.Inject

class MarketsFragment : BaseFragment(), MarketsContract.View {

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    private val mAdapter = MarketAdapter()

    private lateinit var mFilterCta: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mAppCompatEditText: AppCompatEditText
    private lateinit var mLoader: View
    /*
    ************************************************************************************************
    ** Injection
    ************************************************************************************************
    */
    @Inject
    lateinit var mPresenter: MarketsPresenter

    /*
    ************************************************************************************************
    ** Public (static) method
    ************************************************************************************************
    */
    companion object {
        fun newInstance(): MarketsFragment {
            return MarketsFragment()
        }
    }

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
    */
    override val layoutResource: Int
        get() = R.layout.fragment_markets

    override fun init(rootView: View, savedInstanceState: Bundle?) {
        setupPresenter()
        setupWidget(rootView)
        setupView()

        mPresenter.setupView()
    }

    /*
    ************************************************************************************************
    ** MarketsContract.View implementation
    ************************************************************************************************
     */
    override fun setData(newData: List<Market>) {
        mAdapter.replaceData(newData)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            mLoader.visibility = View.VISIBLE
        } else {
            mSwipeRefreshLayout.isRefreshing = false
            mLoader.visibility = View.INVISIBLE
        }
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun setupPresenter() {
        SaveurApplication.dataComponent.presenterComponent().inject(this)
        mPresenter.onAttachView(this)
    }

    private fun setupWidget(rootView: View) {
        mFilterCta = rootView.findViewById(R.id.SearchImageViewFilter)
        mRecyclerView = rootView.findViewById(R.id.MarketsRecyclerView)
        mSwipeRefreshLayout = rootView.findViewById(R.id.MarketsSwipeRefreshLayout)
        mAppCompatEditText = rootView.findViewById(R.id.SearchAppCompatEditText)
        mLoader = rootView.findViewById(R.id.MarketsLoading)
    }

    private fun setupView() {
        //Setup filter CTA
        mFilterCta.setOnClickListener({
            mPresenter.onFilterCtaClicked()
        })

        //Setup adapter
        with(mAdapter) {
            openLoadAnimation(BaseQuickAdapter.ALPHAIN)
            setHasStableIds(true)
            setOnItemClickListener({ _, _, position -> mPresenter.onItemClicked(position) })
        }

        //Setup recycler
        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        //Setup swipe refresh
        mSwipeRefreshLayout.setOnRefreshListener { mPresenter.onSwipeRefreshLayoutActivated() }

        //Setup editText
        mAppCompatEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mPresenter.onTextChanged(s, start, before, count)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mPresenter.onBeforeTextChanged(s, start, count, after)
            }

            override fun afterTextChanged(s: Editable?) {
                mPresenter.onAfterTextChanged(s)
            }
        })
    }
}