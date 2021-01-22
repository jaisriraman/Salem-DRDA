package com.zerobugz.drdasalem.utils.Spinner

class KeyPairModel {
    var value: String? = null
    var key: String? = null
    var aBoolean = false


    constructor(key: String?, value: String?, b: Boolean) {
        this.value = value
        this.key = key
        this.aBoolean = b
    }


}