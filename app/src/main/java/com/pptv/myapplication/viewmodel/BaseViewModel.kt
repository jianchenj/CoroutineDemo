package com.pptv.myapplication.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/*
*
*   ViewModel基类
*   继承自 LifeCycle 的ViewModel
*   实现 LifecycleObserver LifeCycle观察者 和 CoroutineScope 协程接口
* */
open class BaseViewModel : ViewModel(), LifecycleObserver, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    /*
    *  http://www.voidcn.com/article/p-ucnrtdut-btg.html
    *  ArrayList MutableList 区别，如果你指定想要 ArrayList 那就使用它，
    *  mutableListOf 有可能会出现次优的list， MutableList更泛
    * */
    //用于记录管理协程
    private val mLaunchManager: MutableList<Job> = mutableListOf()

    /*
    * @param tryBlock try代码块
    * @param catchBlock catch代码块
    * */
    protected fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUi { tryCatch(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually) }
    }

    private fun launchOnUi(block: suspend CoroutineScope.() -> Unit) {
        val job = launch { block() } //启动协程，执行传入的代码块,执行在主线程，因为前面指定了 CoroutineContext 是mian
        mLaunchManager.add(job) //加入记录
        job.invokeOnCompletion { mLaunchManager.remove(job) }//协程代码执行完成的回调，从manager中移除
    }

    //CoroutineScope 拓展方法 tryCatch
    private suspend fun CoroutineScope.tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        try {
            tryBlock()
        } catch (e: Throwable) {
            if (e !is CancellationException || handleCancellationExceptionManually) {
                catchBlock(e)
            } else {
                throw e
            }
        } finally {
            finallyBlock()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        clearLaunchTask()
    }

    private fun clearLaunchTask() {
        mLaunchManager.clear()
    }
}