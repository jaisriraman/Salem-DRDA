package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class YearResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var yearname: String? = null

}