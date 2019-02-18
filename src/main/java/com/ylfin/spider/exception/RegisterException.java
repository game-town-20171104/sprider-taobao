package com.ylfin.spider.exception;

import com.ylfin.spider.register.enums.SonyRegisterStep;

public class RegisterException extends RuntimeException {

    private SonyRegisterStep sonyRegisterStep;

    public RegisterException(String s, Throwable throwable, SonyRegisterStep sonyRegisterStep) {
        super(s, throwable);
        this.sonyRegisterStep = sonyRegisterStep;
    }

    public RegisterException(String s, SonyRegisterStep sonyRegisterStep) {
        super(s);
        this.sonyRegisterStep = sonyRegisterStep;
    }

    public SonyRegisterStep getSonyRegisterStep() {
        return sonyRegisterStep;
    }
}
