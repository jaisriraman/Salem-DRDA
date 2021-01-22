package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PanjayatResponseResults : RealmObject() {
    @PrimaryKey
    var panjayatid: String? = null
    var panjayatname: String? = null

}