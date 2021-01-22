package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject

open class SchemeWiseBeneficiaryResponse : RealmObject() {
    var status: Int = 0
    var message: String? = null
    var totalbeneficiarycount: String? = null
    var Stagewise: RealmList<StageWise>? = null
    var paymentwise: RealmList<PaymentWise>? = null

}