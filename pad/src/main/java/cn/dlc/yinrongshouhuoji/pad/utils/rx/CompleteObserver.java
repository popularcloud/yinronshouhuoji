package cn.dlc.yinrongshouhuoji.pad.utils.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by John on 2018/4/12.
 */

public abstract class CompleteObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }
}
