package com.example.ra283478.testdemoapplication;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by RA283478 on 1/12/2018.
 */

public class NewsPaperTypeModel extends Observable {

    Context appContext;
    NewsPaperList newsPaperList = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    NewsPaperTypeModel(Context context) {
        this.appContext = context;
        newsPaperList = new NewsPaperList();
    }

    public void fetchNewsPaperList() {
        NewsApp newsApp = NewsApp.create(appContext);
        APIInterface apiInterfaceService = newsApp.getApiInterfaceService();

        Disposable disposable = apiInterfaceService.fetchNewsPaperSources(APIClient.APIKey)
                .subscribeOn(newsApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsPaperList>() {
                    @Override
                    public void accept(NewsPaperList newsPaperList) throws Exception {
                        notifyDownload(newsPaperList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        compositeDisposable.add(disposable);
    }

    private void notifyDownload(NewsPaperList newsPaperList) {
        this.newsPaperList = newsPaperList;
        setChanged();
        notifyObservers();
    }

    public NewsPaperList getNewsPaperList() {
        return newsPaperList;
    }


    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        appContext = null;
    }
}
