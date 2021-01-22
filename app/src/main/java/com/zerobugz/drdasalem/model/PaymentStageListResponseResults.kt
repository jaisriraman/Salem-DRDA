package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PaymentStageListResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var workorderdate: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var remarks: String? = null
    var userid: String? = null

    var schemeid: String? = null
    var blockid: String? = null
    var panjayatid: String? = null
    var habitantid: String? = null
    var habitationname: String? = null
    var panjayatname: String? = null
    var blockname: String? = null
    var schemename: String? = null
    var stages: RealmList<PaymentStagesResults>? = null


}