package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SchemeResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var schemename: String? = null
    var description: String? = null
    var isdeleted: String? = null

}