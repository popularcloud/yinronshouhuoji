package cn.dlc.yinrongshouhuoji.pad.utils.rx;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by John on 2018/4/10.
 */

public class EmptyCompletableObserver implements CompletableObserver {
    @Override
    public void onSubscribe(Disposable d) {
        
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
