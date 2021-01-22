package com.zerobugz.drdasalem.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LoginResponse : RealmObject() {
    var status: Int = 0
    var message: String? = null
    var token: String? = null

    @PrimaryKey
    var id: String? = null
}