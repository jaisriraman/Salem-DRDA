package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BlockResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var blockname: String? = null
    var description: String? = null

}