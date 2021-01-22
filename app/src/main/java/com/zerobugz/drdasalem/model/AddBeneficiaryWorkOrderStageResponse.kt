package com.zerobugz.drdasalem.model

import io.realm.RealmObject

open class AddBeneficiaryWorkOrderStageResponse : RealmObject() {
    var status: Int = 0
    var message: String? = null
}