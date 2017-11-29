package com.ylfin.spider.register;

public interface Register<T> {
    void initRegister();

    void handle(T t);

    void close();
}
