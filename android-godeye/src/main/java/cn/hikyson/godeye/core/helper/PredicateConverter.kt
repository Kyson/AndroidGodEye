package cn.hikyson.godeye.core.helper

fun <T> Predicate<T>.convert(): io.reactivex.functions.Predicate<T> {
    return io.reactivex.functions.Predicate {
        this.test(it)
    }
}