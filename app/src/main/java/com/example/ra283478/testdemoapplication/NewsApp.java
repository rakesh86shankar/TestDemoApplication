package com.example.ra283478.testdemoapplication;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by RA283478 on 1/12/2018.
 */

public class NewsApp extends Application {

    private APIInterface apiInterfaceService;
    private Scheduler scheduler;

    private static NewsApp get(Context context) {
        return (NewsApp) context.getApplicationContext();
    }

    public static NewsApp create(Context context) {
        return NewsApp.get(context);
    }

    public APIInterface getApiInterfaceService() {
        if (apiInterfaceService == null) {
            apiInterfaceService = APIClient.getClient().create(APIInterface.class);
        }

        return apiInterfaceService;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

    public void setPeopleService(APIInterface apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
