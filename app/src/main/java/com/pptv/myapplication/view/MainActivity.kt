package com.pptv.myapplication.view

import com.pptv.myapplication.R
import com.pptv.myapplication.view.base.BaseActivity
import com.pptv.myapplication.viewmodel.WeatherViewModel

class MainActivity : BaseActivity<WeatherViewModel>() {

    override fun layoutId(): Int = R.layout.activity_main
    override fun providerVMClass(): Class<WeatherViewModel> = WeatherViewModel::class.java

    override fun initView() {
//        fab.setOnClickListener {
//            mViewModel?.getWeather(
//                {
//                    progress_bar.visibility = View.VISIBLE
//                },
//                {
//                    tv_hello.visibility = View.VISIBLE
//                    progress_bar.visibility = View.GONE
//                })
//        }
    }

    override fun startObserve() {
//        mViewModel?.apply {
//            mWeather.observe(this@MainActivity, Observer { it ->
//                tv_hello.text = "${it.data}"
//
//            })
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
