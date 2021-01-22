package com.zerobugz.drdasalem.model

import io.realm.RealmObject

open class AddBeneficiaryPaymentStageResponse : RealmObject() {
    var status: Int = 0
    var message: String? = null
}