package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class StageListResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var stagename: String? = null
    var stageorder: String? = null
    var payment: String? = null
    var stagestatus: Boolean = false
    var clickablestatus: Boolean = false

}