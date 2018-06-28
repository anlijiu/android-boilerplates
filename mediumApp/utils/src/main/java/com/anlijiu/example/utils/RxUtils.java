package com.anlijiu.example.utils;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anlijiu on 16-8-12.
 */
public class RxUtils {

    /**
     * 跟compose()配合使用,比如ObservableUtils.wrap(obj).compose(toMain())
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> observableToMainTransformer() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
//                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    public static <T> FlowableTransformer<T, T> flowabletoMainTransformer() {

        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}