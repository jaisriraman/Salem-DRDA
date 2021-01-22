package com.zerobugz.drdasalem.model

import io.realm.RealmObject

open class PanchayatWiseStageListResponse : RealmObject() {

    var _id: String? = null
    var stagename: String? = null
    var blockid: String? = null
    var panjayatid: String? = null
    var stageid: String? = null
    var stagecount: String? = null
    var paymentname: String? = null
    var paymentcount: String? = null


}