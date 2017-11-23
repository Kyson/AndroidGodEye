package cn.hikyson.godeye;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class Engine2<T,D> {
    private T module;

    public Engine2(T m){
        module = m;
    }

    public void produce(){

    }

    public Observable<D> consume(){
        return Observable.create()
    }
}
