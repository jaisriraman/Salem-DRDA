package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class OTPResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var roleid: String? = null
    var desiginationid: String? = null
    var name: String? = null
    var mobile: String? = null
    var blockid: String? = null
    var rolename: String? = null
    var desiginationname: String? = null
}