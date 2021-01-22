package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject

open class PanchayatWiseListResponse : RealmObject() {
    var _id: String? = null
    var panjayatname: String? = null
    var totalbeneficiary: String? = null
    var stagelist: RealmList<PanchayatWiseStageListResponse>? = null


}