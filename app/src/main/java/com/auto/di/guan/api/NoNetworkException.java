package com.auto.di.guan.api;

public final class NoNetworkException extends RuntimeException{

    public NoNetworkException(String message) {
        super(message);
    }

    public NoNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

}
