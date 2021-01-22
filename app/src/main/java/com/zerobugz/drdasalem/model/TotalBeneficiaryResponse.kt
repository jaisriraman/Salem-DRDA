package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject

open class TotalBeneficiaryResponse : RealmObject() {
    var status: Int = 0
    var message: String? = null
    var totalbeneficiarycount: String? = null
    var totalblock: String? = null
    var totalpanjayat: String? = null
    var schemewisecounts: RealmList<SchemeWiseCounts>? = null


}