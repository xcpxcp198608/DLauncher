package com.px.dlauncher.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private final Subject<Object ,Object> rxBus = new SerializedSubject<>(PublishSubject.create());

    private RxBus (){

    }
    private static volatile RxBus instance;
    public static RxBus getDefault(){
        if(instance == null){
            synchronized (RxBus.class){
                if(instance == null){
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public void post(Object object){
        rxBus.onNext(object);
    }

    public <T> Observable<T> toObservable(final Class<T> event){
        return rxBus
                .subscribeOn(Schedulers.io())
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return event.isInstance(o);
                    }
                })
                .cast(event);
    }

}
