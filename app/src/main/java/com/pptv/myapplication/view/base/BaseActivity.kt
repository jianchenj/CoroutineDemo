package com.pptv.myapplication.view.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import com.pptv.myapplication.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    protected var mViewModel: VM? = null//泛型

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        prepareBeforeInitView()
        setToolBar()
        initView()
        initVM()
        startObserve()
    }

    private fun setToolBar() {
        provideToolBar()?.let { setSupportActionBar(it) }
    }

    /*
    *  布局文件 id
    * */
    abstract fun layoutId(): Int

    protected fun initVM() {
        providerVMClass()?.let { it ->
            mViewModel = ViewModelProviders.of(this).get(it)//初始化 ViewModel
            lifecycle.addObserver(mViewModel!!)
        }
    }

    /**
     * 需要复写，设置[Toolbar]
     */
    open fun provideToolBar(): Toolbar? = null

    /**
     * [BaseViewModel]的实现类
     */
    open fun providerVMClass(): Class<VM>? = null

    open fun prepareBeforeInitView() {}
    open fun initView() {}
    open fun startObserve() {}

    override fun onDestroy() {
        //let 这段相当于，判断 mViewModel 是否为空 ，非空就让执行{}代码内，it代表调用let的对象
        mViewModel?.let {
            lifecycle.removeObserver(it)
        }
        super.onDestroy()
    }
}