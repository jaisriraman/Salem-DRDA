package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject

open class HabitantResponse : RealmObject() {
    var status: Int = 0
    var message: String? = null
    var result: RealmList<HabitentResponseResults>? = null
}