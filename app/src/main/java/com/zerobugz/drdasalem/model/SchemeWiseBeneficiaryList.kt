package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject

open class SchemeWiseBeneficiaryList : RealmObject() {
    var status: Int = 0
    var message: String? = null
    var totalbeneficiarycount: String? = null
    var Stagewise: RealmList<SchemeWiseCounts>? = null


}