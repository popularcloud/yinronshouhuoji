package cn.dlc.yinrongshouhuoji.pad.utils.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by John on 2018/3/30.
 */

public class EmptyObserver implements Observer<Object> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(Object o) {

    }
}
