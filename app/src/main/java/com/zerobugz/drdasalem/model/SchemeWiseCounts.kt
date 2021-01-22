package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SchemeWiseCounts : RealmObject() {
    @PrimaryKey
    var schemeid: String? = null
    var schemename: String? = null
    var totalbeneficiary: String? = null


}