package org.senju.eshopeule.utils;

public class FunctionUtil {

    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    public interface QuadFunction<T, U, V, W, X> {
        X apply(T t, U u, V v, W w);
    }
}
