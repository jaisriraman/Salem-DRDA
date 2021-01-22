package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PaymentStagesResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var paymentorder: String? = null
    var paymentname: String? = null
    var paymentstages: Boolean = false
    var clickablestatus: Boolean = false
    var stageid: String? = null

}