/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.acrcloud.ui.base

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean

import com.acrcloud.data.DataManager
import com.acrcloud.data.IDataManager
import com.acrcloud.utils.rx.AppSchedulerProvider
import com.acrcloud.utils.rx.SchedulerProvider

import java.lang.ref.WeakReference

import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by amitshekhar on 07/07/17.
 */

abstract class BaseViewModel<N> : ViewModel() {

    val dataManager: IDataManager

    val loading = ObservableBoolean(false)

    val message: DispatchWorkSubject<String>

    val schedulerProvider: SchedulerProvider

    val compositeDisposable: CompositeDisposable

    private var mNavigator: WeakReference<N>? = null

    var navigator: N?
        get() = if (mNavigator == null) null else mNavigator!!.get()
        set(mNavigator) {
            this.mNavigator = WeakReference<N>(mNavigator)
        }

    init {
        this.dataManager = DataManager()
        this.schedulerProvider = AppSchedulerProvider()
        this.compositeDisposable = CompositeDisposable()

        message = DispatchWorkSubject.create(schedulerProvider.ui())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
