package com.anlijiu.example.utils.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


public class RetryWithTime<T> implements Function<Observable<Throwable>, ObservableSource> {
    int current = -1;
    //延时时间随心所欲，媳妇再也不用担心时间太短了，只需在数组任意添加延时时间即可
    int[] timeDely = new int[]{1, 3, 5, 4, 2, 3, 5, 4, 2, 1};

    @Override
    public Observable apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap(new Function<Throwable, Observable<Long>>() {
            @Override
            public Observable<Long> apply(Throwable throwable) throws Exception {
                ++current;
                if (current < timeDely.length) {
                    return Observable.timer(timeDely[current], TimeUnit.SECONDS);
                } else {
                    return Observable.error(throwable);
                }
            }
        });
    }
}