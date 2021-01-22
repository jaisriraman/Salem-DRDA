package com.zerobugz.drdasalem.model

import io.realm.RealmObject

open class PaymentWise : RealmObject() {
    var paymentname: String? = null
    var paymentcount: String? = null
    var paymentid: String? = null
    var stageid: String? = null

}