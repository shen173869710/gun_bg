package com.auto.di.guan.api;

import androidx.annotation.NonNull;

import com.auto.di.guan.utils.LogUtils;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


public class RetryWhenNetworkException implements Function<Observable<Throwable>, ObservableSource<?>> {
    private int count;
    private long delay = 3;
    private long increaseDelay = 5;

    public RetryWhenNetworkException() {
    }

    public RetryWhenNetworkException(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public RetryWhenNetworkException(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }
    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
        count =0;
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
                    LogUtils.i("----------","wifi no open"+throwable.toString());
                    if(count == delay) {
                        return Observable.error(new Throwable("wifi no open"));
                    }
                    count++;
                    return Observable.timer(increaseDelay, TimeUnit.SECONDS);
                }else {
                    return Observable.error(new Throwable(throwable.toString()));
                }
            }
        });
    }
}
