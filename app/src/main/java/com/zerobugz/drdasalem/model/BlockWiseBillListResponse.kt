package com.zerobugz.drdasalem.model

import io.realm.RealmObject

open class BlockWiseBillListResponse : RealmObject() {
    var paymentname: String? = null
    var paymentid: String? = null
    var stageid: String? = null
    var paymentcount: String? = null


}