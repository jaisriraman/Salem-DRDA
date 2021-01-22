package com.zerobugz.drdasalem.api.presenter

abstract class APIView {
    abstract fun onSuccess()
    abstract fun onException()
    open fun onError() {}
    open fun onMessage(message: String) {}

}