package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class HabitentResponseResults : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var blockid: String? = null
    var panjayatid: String? = null
    var habitationname: String? = null
    var description: String? = null

}