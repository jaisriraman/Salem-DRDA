package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BeneficiaryListResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var schemeid: String? = null
    var blockid: String? = null
    var panjayatid: String? = null
    var habitantid: String? = null
    var workorderdate: String? = null
    var habitationname: String? = null
    var panjayatname: String? = null
    var blockname: String? = null
    var schemename: String? = null
    var firstname: String? = null
    var lastname: String? = null

}