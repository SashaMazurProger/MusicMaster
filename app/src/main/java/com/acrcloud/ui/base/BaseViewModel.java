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

package com.acrcloud.ui.base;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.acrcloud.data.DataManager;
import com.acrcloud.data.IDataManager;
import com.acrcloud.utils.rx.AppSchedulerProvider;
import com.acrcloud.utils.rx.SchedulerProvider;

import java.lang.ref.WeakReference;

import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by amitshekhar on 07/07/17.
 */

public abstract class BaseViewModel<N> extends ViewModel {

    private final IDataManager mDataManager;

    private final ObservableBoolean loading = new ObservableBoolean(false);

    private final DispatchWorkSubject<String> message;

    private final SchedulerProvider mSchedulerProvider;

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<N> mNavigator;

    public BaseViewModel() {
        this.mDataManager = new DataManager();
        this.mSchedulerProvider = new AppSchedulerProvider();
        this.mCompositeDisposable = new CompositeDisposable();

        message = DispatchWorkSubject.create(getSchedulerProvider().ui());
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public IDataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public ObservableBoolean getLoading() {
        return loading;
    }

    public DispatchWorkSubject<String> getMessage() {
        return message;
    }

    public N getNavigator() {
        return mNavigator == null ? null : mNavigator.get();
    }

    public void setNavigator(N mNavigator) {
        this.mNavigator = new WeakReference<N>(mNavigator);
    }
}
